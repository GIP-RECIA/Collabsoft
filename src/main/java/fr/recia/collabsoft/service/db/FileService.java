/*
 * Copyright (C) 2023 GIP-RECIA, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.recia.collabsoft.service.db;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import fr.recia.collabsoft.db.entity.AssociatedApp;
import fr.recia.collabsoft.db.entity.File;
import fr.recia.collabsoft.db.entity.QAssociatedApp;
import fr.recia.collabsoft.db.entity.QCollaboration;
import fr.recia.collabsoft.db.entity.QFile;
import fr.recia.collabsoft.db.entity.QMetadata;
import fr.recia.collabsoft.db.entity.User;
import fr.recia.collabsoft.db.repository.AssociatedAppRepository;
import fr.recia.collabsoft.db.repository.FileRepository;
import fr.recia.collabsoft.interceptor.bean.SoffitHolder;
import fr.recia.collabsoft.model.enums.Authority;
import fr.recia.collabsoft.model.pojo.JsonFileBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FileService {

  @Autowired
  private AssociatedAppRepository<AssociatedApp> associatedAppRepository;
  @Autowired
  private FileRepository<File> fileRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private SoffitHolder soffitHolder;

  public List<File> getFiles() {
    final List<File> files = IteratorUtils.toList(
      fileRepository.findAll(QFile.file.creator.casUid.eq(soffitHolder.getSub())).iterator()
    );

    if (files.isEmpty()) log.debug("No files found for user with sub \"{}\"", soffitHolder.getSub());

    return files;
  }

  public List<File> getStarredFiles() {
    final List<File> files = IteratorUtils.toList(
      fileRepository.findAll(
        QFile.file.id.in(
          JPAExpressions.select(QMetadata.metadata.file.id)
            .from(QMetadata.metadata)
            .where(QMetadata.metadata.starred.eq(true))
        ).and(QFile.file.creator.casUid.eq(soffitHolder.getSub()))
      ).iterator()
    );

    if (files.isEmpty()) log.debug("No starred files found for user with sub \"{}\"", soffitHolder.getSub());

    return files;
  }

  public List<File> getSharedFiles() {
    final List<File> files = IteratorUtils.toList(
      fileRepository.findAll(
        QFile.file.id.in(
          JPAExpressions.select(QCollaboration.collaboration.file.id)
            .from(QCollaboration.collaboration)
            .where(QCollaboration.collaboration.user.casUid.eq(soffitHolder.getSub()))
        )
      ).iterator()
    );

    if (files.isEmpty()) log.debug("No shared files found for user with sub \"{}\"", soffitHolder.getSub());

    return files;
  }

  public List<File> getPublicFiles() {
    final List<File> files = IteratorUtils.toList(
      fileRepository.findAll(
        QFile.file.pub.eq(true)
      ).iterator()
    );

    if (files.isEmpty()) log.debug("No public files found");

    return files;
  }

  public File saveFile(JsonFileBody body) {
    User user = userService.getCurrentUser();
    if (user == null) {
      log.info("No user found => Create user");
      user = userService.createUser();
      if (user == null) return null;
    }
    final AssociatedApp associatedApp = associatedAppRepository.findOne(
      QAssociatedApp.associatedApp.id.eq(body.getAssociatedAppId())
    ).orElse(null);
    if (associatedApp == null) return null;

    File file = new File();
    file.setTitle(body.getTitle());
    file.setDescription(body.getDescription());
    file.setBlob(body.getBlob().getBytes());
    file.setCreator(user);
    file.setLastEditor(user);
    file.setAssociatedApp(associatedApp);
    file.setPub(body.getPub());
    fileRepository.saveAndFlush(file);

    return file;
  }

  public File getFile(Long id, Authority authority) {
    final Predicate predicate;
    switch (authority) {
      case OWNER:
        predicate = QFile.file.id.eq(id).and(QFile.file.creator.casUid.eq(soffitHolder.getSub()));
        break;
      case OWNER_OR_COLLABORATOR:
        predicate = QFile.file.id.eq(id).and(
          QFile.file.creator.casUid.eq(soffitHolder.getSub()).or(
            QFile.file.id.in(
              JPAExpressions.select(QCollaboration.collaboration.file.id)
                .from(QCollaboration.collaboration)
                .where(QCollaboration.collaboration.user.casUid.eq(soffitHolder.getSub()))
            )
          )
        );
        break;
      case OWNER_OR_COLLABORATOR_OR_PUBLIC:
      default:
        predicate = QFile.file.id.eq(id).and(
          QFile.file.creator.casUid.eq(soffitHolder.getSub()).or(
            QFile.file.id.in(
              JPAExpressions.select(QCollaboration.collaboration.file.id)
                .from(QCollaboration.collaboration)
                .where(QCollaboration.collaboration.user.casUid.eq(soffitHolder.getSub()))
            )
          ).or(QFile.file.pub.eq(true))
        );
        break;
    }
    final File file = fileRepository.findOne(predicate).orElse(null);

    if (file == null)
      log.debug("No file found for user with sub \"{}\" and file with id \"{}\"", soffitHolder.getSub(), id);

    return file;
  }

  public File updateFile(Long id, JsonFileBody body) {
    final User user = userService.getCurrentUser();
    if (user == null) return null;
    final File file = getFile(id, Authority.OWNER_OR_COLLABORATOR);
    if (file == null) return null;

    if (body.getTitle() != null) file.setTitle(body.getTitle());
    if (body.getDescription() != null)
      file.setDescription(body.getDescription().isEmpty() ? null : body.getDescription());
    if (body.getBlob() != null) file.setBlob(body.getBlob().getBytes());
    if (user != file.getLastEditor()) file.setLastEditor(user);
    if (body.getPub() != null) file.setPub(body.getPub());
    fileRepository.saveAndFlush(file);

    return file;
  }

  public boolean deleteFile(Long id) {
    final File file = getFile(id, Authority.OWNER);
    if (file == null) return false;

    fileRepository.delete(file);
    return true;
  }

}
