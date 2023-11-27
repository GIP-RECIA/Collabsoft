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
package fr.recia.collabsoft.services.db;

import com.querydsl.jpa.JPAExpressions;
import fr.recia.collabsoft.db.entities.AssociatedApp;
import fr.recia.collabsoft.db.entities.File;
import fr.recia.collabsoft.db.entities.QAssociatedApp;
import fr.recia.collabsoft.db.entities.QCollaboration;
import fr.recia.collabsoft.db.entities.QFile;
import fr.recia.collabsoft.db.entities.QMetadata;
import fr.recia.collabsoft.db.entities.User;
import fr.recia.collabsoft.db.repositories.AssociatedAppRepository;
import fr.recia.collabsoft.db.repositories.FileRepository;
import fr.recia.collabsoft.interceptors.beans.SoffitHolder;
import fr.recia.collabsoft.pojo.JsonFileBody;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

  @Autowired
  private AssociatedAppRepository<AssociatedApp> associatedAppRepository;
  @Autowired
  private FileRepository<File> fileRepository;

  @Autowired
  private UserService userService;

  private final SoffitHolder soffitHolder;

  public FileService(SoffitHolder soffitHolder) {
    this.soffitHolder = soffitHolder;
  }

  public List<File> getFiles() {
    return IteratorUtils.toList(
      fileRepository.findAll(QFile.file.creator.casUid.eq(soffitHolder.getSub())).iterator()
    );
  }

  public List<File> getStarredFiles() {
    return IteratorUtils.toList(
      fileRepository.findAll(
        QFile.file.id.in(
          JPAExpressions.select(QMetadata.metadata.file.id)
            .from(QMetadata.metadata)
            .where(QMetadata.metadata.starred.eq(true))
        ).and(QFile.file.creator.casUid.eq(soffitHolder.getSub()))
      ).iterator()
    );
  }

  public List<File> getSharedFiles() {
    return IteratorUtils.toList(
      fileRepository.findAll(
        QFile.file.id.in(
          JPAExpressions.select(QCollaboration.collaboration.file.id)
            .from(QCollaboration.collaboration)
            .where(QCollaboration.collaboration.user.casUid.eq(soffitHolder.getSub()))
        )
      ).iterator()
    );
  }

  public List<File> getPublicFiles() {
    return IteratorUtils.toList(
      fileRepository.findAll(
        QFile.file.pub.eq(true)
      ).iterator()
    );
  }

  public File saveFile(JsonFileBody body) {
    final User user = userService.getCurrentUser();
    if (user == null) return null;
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

  public File getFile(Long id) {
    return fileRepository.findOne(
      QFile.file.id.eq(id)
    ).orElse(null);
  }

  public File updateFile(Long id, JsonFileBody body) {
    final User user = userService.getCurrentUser();
    if (user == null) return null;
    final File file = getFile(id);
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
    final File file = getFile(id);
    if (file == null) return false;

    fileRepository.delete(file);
    return true;
  }

}
