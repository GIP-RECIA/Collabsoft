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
package fr.recia.collabsoft.services;

import fr.recia.collabsoft.db.entities.Collaboration;
import fr.recia.collabsoft.db.entities.File;
import fr.recia.collabsoft.db.entities.QCollaboration;
import fr.recia.collabsoft.db.entities.User;
import fr.recia.collabsoft.db.repositories.CollaborationRepository;
import fr.recia.collabsoft.pojo.JsonCollaborationBody;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollaborationService {

  @Autowired
  private CollaborationRepository<Collaboration> collaborationRepository;

  @Autowired
  private FileService fileService;
  @Autowired
  private UserService userService;

  public List<Collaboration> getCollaborations(Long fileId) {
    return IteratorUtils.toList(
      collaborationRepository.findAll(QCollaboration.collaboration.file.id.eq(fileId)).iterator()
    );
  }

  public Collaboration getCollaboration(Long fileId, Long userId) {
    return collaborationRepository.findOne(
      QCollaboration.collaboration.file.id.eq(fileId).and(QCollaboration.collaboration.user.id.eq(userId))
    ).orElse(null);
  }

  public boolean saveCollaboration(Long fileId, JsonCollaborationBody body) {
    final User user = userService.getUser(body.getUserId());
    if (user == null) return false;
    final File file = fileService.getFile(fileId);
    if (file == null) return false;

    Collaboration collaboration = new Collaboration();
    collaboration.setUser(user);
    collaboration.setFile(file);
    collaboration.setRole(body.getRole());
    collaborationRepository.saveAndFlush(collaboration);

    return true;
  }

  public boolean updateCollaboration(Long fileId, Long userId, JsonCollaborationBody body) {
    final Collaboration collaboration = getCollaboration(fileId, userId);
    if (collaboration == null) return false;
    collaboration.setRole(body.getRole());
    collaborationRepository.saveAndFlush(collaboration);

    return true;
  }

  public boolean deleteCollaboration(Long fileId, Long userId) {
    final Collaboration collaboration = getCollaboration(fileId, userId);
    if (collaboration == null) return false;
    collaborationRepository.delete(collaboration);

    return true;
  }

  public boolean deleteCollaborations(Long fileId) {
    final List<Collaboration> collaborations = getCollaborations(fileId);
    if (collaborations.isEmpty()) return false;
    collaborationRepository.deleteAll(collaborations);

    return true;
  }

}
