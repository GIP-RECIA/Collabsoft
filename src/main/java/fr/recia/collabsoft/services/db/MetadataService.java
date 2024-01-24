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

import fr.recia.collabsoft.db.entities.File;
import fr.recia.collabsoft.db.entities.Metadata;
import fr.recia.collabsoft.db.entities.QMetadata;
import fr.recia.collabsoft.db.entities.User;
import fr.recia.collabsoft.db.repositories.MetadataRepository;
import fr.recia.collabsoft.interceptors.beans.SoffitHolder;
import fr.recia.collabsoft.pojo.JsonMetadataBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetadataService {

  @Autowired
  private MetadataRepository<Metadata> metadataRepository;

  @Autowired
  private FileService fileService;
  @Autowired
  private UserService userService;

  @Autowired
  private SoffitHolder soffitHolder;

  public boolean updateMetadata(Long fileId, JsonMetadataBody body) {
    final User user = userService.getCurrentUser();
    if (user == null) return false;
    Metadata metadata = metadataRepository.findOne(
      QMetadata.metadata.file.id.eq(fileId).and(QMetadata.metadata.user.casUid.eq(soffitHolder.getSub()))
    ).orElse(null);
    if (metadata == null) {
      final File file = fileService.getFile(fileId);
      if (file == null) return false;
      metadata = new Metadata();
      metadata.setUser(user);
      metadata.setFile(file);
    }

    if (body.getStarred() != null) metadata.setStarred(body.getStarred());
    metadataRepository.saveAndFlush(metadata);

    return true;
  }

}
