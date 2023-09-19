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
import fr.recia.collabsoft.db.dto.FileDto;
import fr.recia.collabsoft.db.entities.Collaboration;
import fr.recia.collabsoft.db.entities.File;
import fr.recia.collabsoft.db.entities.FileHistory;
import fr.recia.collabsoft.db.entities.QCollaboration;
import fr.recia.collabsoft.db.entities.QFile;
import fr.recia.collabsoft.db.entities.QFileHistory;
import fr.recia.collabsoft.db.repositories.CollaborationRepository;
import fr.recia.collabsoft.db.repositories.FileHistoryRepository;
import fr.recia.collabsoft.db.repositories.FileRepository;
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
import java.util.Map;

@RestController
@RequestMapping(value = "/api/file")
@Slf4j
public class FileController {

  @Inject
  private CollaborationRepository<Collaboration> collaborationRepository;
  @Inject
  private FileRepository<File> fileRepository;
  @Inject
  private FileHistoryRepository<FileHistory> fileHistoryRepository;

  // TODO: only for tests
  private final Long myId = 1L;

  /*
   * File
   */

  @GetMapping
  public ResponseEntity<List<File>> getFiles() {
    final List<File> files = IteratorUtils.toList(
      fileRepository.findAll(QFile.file.creator.id.eq(myId)).iterator()
    );
    if (files.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(files, HttpStatus.OK);
  }

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

  @PostMapping
  public ResponseEntity<Object> postFile(@RequestBody Map<String, Object> body) {

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<File> getFile(@PathVariable Long id) {
    final File file = fileRepository.findOne(
      QFile.file.id.eq(id)
    ).orElse(null);
    if (file == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(file, HttpStatus.OK);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<Object> putFile(@PathVariable Long id, @RequestBody Map<String, Object> body) {
    final File file = fileRepository.findOne(
      QFile.file.id.eq(id)
    ).orElse(null);
    if (file == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(HttpStatus.OK);
  }

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
   * Share
   */

  @GetMapping(value = "/{id}/share")
  public ResponseEntity<List<Collaboration>> getShare(@PathVariable Long id) {
    final List<Collaboration> collaborations = IteratorUtils.toList(
      collaborationRepository.findAll(QCollaboration.collaboration.file.id.eq(id)).iterator()
    );
    if (collaborations.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(collaborations, HttpStatus.OK);
  }

  @PostMapping(value = "/{id}/share")
  public ResponseEntity<Object> postShare(@PathVariable Long id, @RequestBody Map<String, Object> body) {

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(value = "/{id}/share/{userId}")
  public ResponseEntity<Object> putShare(@PathVariable Long id, @PathVariable Long userId, @RequestBody Map<String, Object> body) {
    final Collaboration collaboration = collaborationRepository.findOne(
      QCollaboration.collaboration.file.id.eq(id).and(QCollaboration.collaboration.user.id.eq(userId))
    ).orElse(null);
    if (collaboration == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}/share/{userId}")
  public ResponseEntity<Object> deleteShare(@PathVariable Long id, @PathVariable Long userId) {
    final Collaboration collaboration = collaborationRepository.findOne(
      QCollaboration.collaboration.file.id.eq(id).and(QCollaboration.collaboration.user.id.eq(userId))
    ).orElse(null);
    if (collaboration == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    collaborationRepository.delete(collaboration);

    return new ResponseEntity<>(HttpStatus.OK);
  }

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

  @GetMapping(value = "/{id}/history")
  public ResponseEntity<List<FileHistory>> getHistories(@PathVariable Long id) {
    final List<FileHistory> fileHistories = IteratorUtils.toList(
      fileHistoryRepository.findAll(QFileHistory.fileHistory.file.id.eq(id)).iterator()
    );
    if (fileHistories.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(fileHistories, HttpStatus.OK);
  }

  @PostMapping(value = "/{id}/history")
  public ResponseEntity<Object> postHistory(@PathVariable Long id, @RequestBody Map<String, Object> body) {

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(value = "/{id}/history/{historyId}")
  public ResponseEntity<FileHistory> getHistory(@PathVariable Long id, @PathVariable Long historyId) {
    final FileHistory fileHistory = fileHistoryRepository.findOne(
      QFileHistory.fileHistory.file.id.eq(id).and(QFileHistory.fileHistory.id.eq(historyId))
    ).orElse(null);
    if (fileHistory == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(fileHistory, HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}/history/{historyId}")
  public ResponseEntity<Object> deleteHistory(@PathVariable Long id, @PathVariable Long historyId) {
    final FileHistory fileHistory = fileHistoryRepository.findOne(
      QFileHistory.fileHistory.file.id.eq(id).and(QFileHistory.fileHistory.id.eq(historyId))
    ).orElse(null);
    if (fileHistory == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    fileHistoryRepository.delete(fileHistory);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping(value = "/{id}/history/{historyId}/revert")
  public ResponseEntity<FileDto> revertHistory(@PathVariable Long id, @PathVariable Long historyId) {

    return new ResponseEntity<>(HttpStatus.OK);
  }

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
