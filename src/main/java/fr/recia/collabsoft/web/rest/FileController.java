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
package fr.recia.collabsoft.web.rest;

import com.querydsl.jpa.JPAExpressions;
import fr.recia.collabsoft.db.entities.AssociatedApp;
import fr.recia.collabsoft.db.entities.Collaboration;
import fr.recia.collabsoft.db.entities.File;
import fr.recia.collabsoft.db.entities.FileHistory;
import fr.recia.collabsoft.db.entities.Metadata;
import fr.recia.collabsoft.db.entities.QAssociatedApp;
import fr.recia.collabsoft.db.entities.QCollaboration;
import fr.recia.collabsoft.db.entities.QFile;
import fr.recia.collabsoft.db.entities.QFileHistory;
import fr.recia.collabsoft.db.entities.QMetadata;
import fr.recia.collabsoft.db.entities.QUser;
import fr.recia.collabsoft.db.entities.User;
import fr.recia.collabsoft.db.repositories.AssociatedAppRepository;
import fr.recia.collabsoft.db.repositories.CollaborationRepository;
import fr.recia.collabsoft.db.repositories.FileHistoryRepository;
import fr.recia.collabsoft.db.repositories.FileRepository;
import fr.recia.collabsoft.db.repositories.MetadataRepository;
import fr.recia.collabsoft.db.repositories.UserRepository;
import fr.recia.collabsoft.pojo.JsonCollaborationBody;
import fr.recia.collabsoft.pojo.JsonFileBody;
import fr.recia.collabsoft.pojo.JsonHistoryBody;
import fr.recia.collabsoft.pojo.JsonMetadataBody;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/file")
public class FileController {

  @Inject
  private AssociatedAppRepository<AssociatedApp> associatedAppRepository;
  @Inject
  private CollaborationRepository<Collaboration> collaborationRepository;
  @Inject
  private FileRepository<File> fileRepository;
  @Inject
  private FileHistoryRepository<FileHistory> fileHistoryRepository;
  @Inject
  private MetadataRepository<Metadata> metadataRepository;
  @Inject
  private UserRepository<User> userRepository;

  // TODO: only for tests
  private final Long myId = 1L;

  private User getCurrentUser() {
    return userRepository.findOne(
      QUser.user.id.eq(myId)
    ).orElse(null);
  }

  /*
   * File
   */

  /**
   * List my files
   * @return
   */
  @GetMapping
  public ResponseEntity<List<File>> getFiles() {
    final List<File> files = IteratorUtils.toList(
      fileRepository.findAll(QFile.file.creator.id.eq(myId)).iterator()
    );
    if (files.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(files, HttpStatus.OK);
  }

  /**
   * List files starred
   * @return
   */
  @GetMapping(value = "/starred")
  public ResponseEntity<List<File>> getStarredFiles() {
    final List<File> files = IteratorUtils.toList(
      fileRepository.findAll(
        QFile.file.id.in(
          JPAExpressions.select(QMetadata.metadata.file.id)
            .from(QMetadata.metadata)
            .where(QMetadata.metadata.starred.eq(true))
        )
      ).iterator()
    );
    if (files.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(files, HttpStatus.OK);
  }

  /**
   * List files shared with me
   * @return
   */
  @GetMapping(value = "/shared")
  public ResponseEntity<List<File>> getSharedFiles() {
    final List<File> files = IteratorUtils.toList(
      fileRepository.findAll(
        QFile.file.id.in(
          JPAExpressions.select(QCollaboration.collaboration.file.id)
            .from(QCollaboration.collaboration)
            .where(QCollaboration.collaboration.user.id.eq(myId))
        )
      ).iterator()
    );
    if (files.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(files, HttpStatus.OK);
  }

  /**
   * List files public
   * @return
   */
  @GetMapping(value = "/public")
  public ResponseEntity<List<File>> getPublicFiles() {
    final List<File> files = IteratorUtils.toList(
      fileRepository.findAll(
        QFile.file.pub.eq(true)
      ).iterator()
    );
    if (files.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(files, HttpStatus.OK);
  }

  /**
   * Save file
   * @param body
   */
  @PostMapping
  public ResponseEntity<Object> postFile(@NonNull @RequestBody JsonFileBody body) {
    if (!body.postDataOk()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    final User user = getCurrentUser();
    final AssociatedApp associatedApp = associatedAppRepository.findOne(
      QAssociatedApp.associatedApp.id.eq(body.getAssociatedAppId())
    ).orElse(null);
    if (associatedApp == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    File file = new File();
    file.setTitle(body.getTitle());
    file.setDescription(body.getDescription());
    file.setBlob(body.getBlob());
    file.setCreator(user);
    file.setLastEditor(user);
    file.setAssociatedApp(associatedApp);
    file.setPub(body.getPub());
    fileRepository.saveAndFlush(file);

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * Get file
   * @param id  File id
   * @return
   */
  @GetMapping(value = "/{id}")
  public ResponseEntity<File> getFile(@PathVariable Long id) {
    final File file = fileRepository.findOne(
      QFile.file.id.eq(id)
    ).orElse(null);
    if (file == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(file, HttpStatus.OK);
  }

  /**
   * Update file
   * @param id    File id
   * @param body
   */
  @PutMapping(value = "/{id}")
  public ResponseEntity<Object> putFile(@PathVariable Long id, @NonNull @RequestBody JsonFileBody body) {
    if (!body.putDataOk()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    final User user = getCurrentUser();
    final File file = fileRepository.findOne(
      QFile.file.id.eq(id)
    ).orElse(null);
    if (file == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    if (body.getTitle() != null)
      file.setTitle(body.getTitle());
    if (body.getDescription() != null)
      file.setDescription(body.getDescription().isEmpty() ? null : body.getDescription());
    if (body.getBlob() != null)
      file.setBlob(body.getBlob());
    if (user != file.getLastEditor())
      file.setLastEditor(user);
    if (body.getPub() != null)
      file.setPub(body.getPub());
    fileRepository.saveAndFlush(file);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Delete file
   * @param id  File id
   */
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Object> deleteFile(@PathVariable Long id) {
    final File file = fileRepository.findOne(
      QFile.file.id.eq(id)
    ).orElse(null);
    if (file == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    fileRepository.delete(file);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /*
   * Metadata
   */

  /**
   * Update metadata
   * @param id  File id
   */
  @PutMapping(value = "/{id}/metadata")
  public ResponseEntity<List<Collaboration>> putMetadata(@PathVariable Long id, @NonNull @RequestBody JsonMetadataBody body) {
    if (!body.putDataOk()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    final User user = getCurrentUser();
    Metadata metadata = metadataRepository.findOne(
      QMetadata.metadata.file.id.eq(id).and(QMetadata.metadata.user.id.eq(myId))
    ).orElse(null);
    if (metadata == null) {
      final File file = fileRepository.findOne(
        QFile.file.id.eq(id)
      ).orElse(null);
      if (file == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      metadata = new Metadata();
      metadata.setUser(user);
      metadata.setFile(file);
    }

    if (body.getStarred() != null)
      metadata.setStarred(body.getStarred());
    metadataRepository.saveAndFlush(metadata);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /*
   * Share
   */

  /**
   * List sharing
   * @param id  File id
   * @return
   */
  @GetMapping(value = "/{id}/share")
  public ResponseEntity<List<Collaboration>> getShare(@PathVariable Long id) {
    final List<Collaboration> collaborations = IteratorUtils.toList(
      collaborationRepository.findAll(QCollaboration.collaboration.file.id.eq(id)).iterator()
    );
    if (collaborations.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(collaborations, HttpStatus.OK);
  }

  /**
   * Share to someone
   * @param id    File id
   * @param body
   */
  @PostMapping(value = "/{id}/share")
  public ResponseEntity<Object> postShare(@PathVariable Long id, @NonNull @RequestBody JsonCollaborationBody body) {
    if (!body.postDataOk()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    final User user = userRepository.findOne(
      QUser.user.id.eq(body.getUserId())
    ).orElse(null);
    if (user == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    final File file = fileRepository.findOne(
      QFile.file.id.eq(id)
    ).orElse(null);
    if (file == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    Collaboration collaboration = new Collaboration();
    collaboration.setUser(user);
    collaboration.setFile(file);
    collaboration.setRole(body.getRole());
    collaborationRepository.saveAndFlush(collaboration);

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * Update a share
   * @param id      File id
   * @param userId  User id
   * @param body
   */
  @PutMapping(value = "/{id}/share/{userId}")
  public ResponseEntity<Object> putShare(@PathVariable Long id, @PathVariable Long userId, @NonNull @RequestBody JsonCollaborationBody body) {
    if (!body.putDataOk()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    final Collaboration collaboration = collaborationRepository.findOne(
      QCollaboration.collaboration.file.id.eq(id).and(QCollaboration.collaboration.user.id.eq(userId))
    ).orElse(null);
    if (collaboration == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    collaboration.setRole(body.getRole());
    collaborationRepository.saveAndFlush(collaboration);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Delete a share
   * @param id      File id
   * @param userId  User id
   */
  @DeleteMapping(value = "/{id}/share/{userId}")
  public ResponseEntity<Object> deleteShare(@PathVariable Long id, @PathVariable Long userId) {
    final Collaboration collaboration = collaborationRepository.findOne(
      QCollaboration.collaboration.file.id.eq(id).and(QCollaboration.collaboration.user.id.eq(userId))
    ).orElse(null);
    if (collaboration == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    collaborationRepository.delete(collaboration);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Delete sharing
   * @param id  File id
   */
  @DeleteMapping(value = "/{id}/share")
  public ResponseEntity<Object> deleteShared(@PathVariable Long id) {
    final List<Collaboration> collaborations = IteratorUtils.toList(
      collaborationRepository.findAll(QCollaboration.collaboration.file.id.eq(id)).iterator()
    );
    if (collaborations.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    collaborationRepository.deleteAll(collaborations);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /*
   * History
   */

  /**
   * List histories
   * @param id  File id
   * @return
   */
  @GetMapping(value = "/{id}/history")
  public ResponseEntity<List<FileHistory>> getHistories(@PathVariable Long id) {
    final List<FileHistory> fileHistories = IteratorUtils.toList(
      fileHistoryRepository.findAll(QFileHistory.fileHistory.file.id.eq(id)).iterator()
    );
    if (fileHistories.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(fileHistories, HttpStatus.OK);
  }

  /**
   * Create a history
   * @param id    File id
   * @param body
   */
  @PostMapping(value = "/{id}/history")
  public ResponseEntity<Object> postHistory(@PathVariable Long id, @NonNull @RequestBody JsonHistoryBody body) {
    if (!body.postDataOk()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    final File file = fileRepository.findOne(
      QFile.file.id.eq(id)
    ).orElse(null);
    if (file == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    FileHistory fileHistory = new FileHistory();
    fileHistory.setFile(file);
    fileHistory.setBlob(body.getBlob());

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * Get a history
   * @param id        File id
   * @param historyId FileHistory id
   * @return
   */
  @GetMapping(value = "/{id}/history/{historyId}")
  public ResponseEntity<FileHistory> getHistory(@PathVariable Long id, @PathVariable Long historyId) {
    final FileHistory fileHistory = fileHistoryRepository.findOne(
      QFileHistory.fileHistory.file.id.eq(id).and(QFileHistory.fileHistory.id.eq(historyId))
    ).orElse(null);
    if (fileHistory == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(fileHistory, HttpStatus.OK);
  }

  /**
   * Delete a history
   * @param id        File id
   * @param historyId FileHistory id
   */
  @DeleteMapping(value = "/{id}/history/{historyId}")
  public ResponseEntity<Object> deleteHistory(@PathVariable Long id, @PathVariable Long historyId) {
    final FileHistory fileHistory = fileHistoryRepository.findOne(
      QFileHistory.fileHistory.file.id.eq(id).and(QFileHistory.fileHistory.id.eq(historyId))
    ).orElse(null);
    if (fileHistory == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    fileHistoryRepository.delete(fileHistory);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Revert a history
   * @param id        File id
   * @param historyId FileHistory id
   */
  @PostMapping(value = "/{id}/history/{historyId}/revert")
  public ResponseEntity<Object> revertHistory(@PathVariable Long id, @PathVariable Long historyId) {
    final FileHistory fileHistory = fileHistoryRepository.findOne(
      QFileHistory.fileHistory.file.id.eq(id).and(QFileHistory.fileHistory.id.eq(historyId))
    ).orElse(null);
    if (fileHistory == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    // create new file history
    FileHistory newFileHistory = new FileHistory();
    newFileHistory.setFile(fileHistory.getFile());
    newFileHistory.setBlob(fileHistory.getFile().getBlob());
    fileHistoryRepository.saveAndFlush(newFileHistory);

    // update file blob with history
    File file = fileHistory.getFile();
    file.setBlob(fileHistory.getBlob());
    fileRepository.saveAndFlush(file);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Delete histories
   * @param id  File id
   */
  @DeleteMapping(value = "/{id}/history")
  public ResponseEntity<Object> deleteHistories(@PathVariable Long id) {
    final List<FileHistory> fileHistories = IteratorUtils.toList(
      fileHistoryRepository.findAll(QFileHistory.fileHistory.file.id.eq(id)).iterator()
    );
    if (fileHistories.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    fileHistoryRepository.deleteAll(fileHistories);

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
