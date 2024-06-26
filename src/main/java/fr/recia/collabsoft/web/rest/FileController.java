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

import fr.recia.collabsoft.db.entity.Collaboration;
import fr.recia.collabsoft.db.entity.File;
import fr.recia.collabsoft.db.entity.FileHistory;
import fr.recia.collabsoft.model.enums.Authority;
import fr.recia.collabsoft.model.pojo.JsonCollaborationBody;
import fr.recia.collabsoft.model.pojo.JsonFileBody;
import fr.recia.collabsoft.model.pojo.JsonHistoryBody;
import fr.recia.collabsoft.model.pojo.JsonMetadataBody;
import fr.recia.collabsoft.service.db.CollaborationService;
import fr.recia.collabsoft.service.db.FileHistoryService;
import fr.recia.collabsoft.service.db.FileService;
import fr.recia.collabsoft.service.db.MetadataService;
import fr.recia.collabsoft.service.storage.ResourceService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/file")
public class FileController {

  @Autowired
  private CollaborationService collaborationService;
  @Autowired
  private FileService fileService;
  @Autowired
  private FileHistoryService fileHistoryService;
  @Autowired
  private MetadataService metadataService;
  @Autowired
  private ResourceService resourceService;

  /*
   * File
   */

  /**
   * List my files
   *
   * @return
   */
  @GetMapping
  public ResponseEntity<List<File>> getFiles() {
    return new ResponseEntity<>(fileService.getFiles(), HttpStatus.OK);
  }

  /**
   * List files starred
   *
   * @return
   */
  @GetMapping(value = "/starred")
  public ResponseEntity<List<File>> getStarredFiles() {
    return new ResponseEntity<>(fileService.getStarredFiles(), HttpStatus.OK);
  }

  /**
   * List files shared with me
   *
   * @return
   */
  @GetMapping(value = "/shared")
  public ResponseEntity<List<File>> getSharedFiles() {
    return new ResponseEntity<>(fileService.getSharedFiles(), HttpStatus.OK);
  }

  /**
   * List files public
   *
   * @return
   */
  @GetMapping(value = "/public")
  public ResponseEntity<List<File>> getPublicFiles() {
    return new ResponseEntity<>(fileService.getPublicFiles(), HttpStatus.OK);
  }

  /**
   * Save file
   *
   * @param body
   */
  @PostMapping
  public ResponseEntity<Object> postFile(@NonNull @RequestBody JsonFileBody body) {
    if (!body.postDataOk()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    final File file = fileService.saveFile(body);
    if (file == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    return new ResponseEntity<>(file, HttpStatus.CREATED);
  }

  /**
   * Get file
   *
   * @param id File id
   * @return
   */
  @GetMapping(value = "/{id}")
  public ResponseEntity<File> getFile(@PathVariable Long id) {
    final File file = fileService.getFile(id, Authority.OWNER_OR_COLLABORATOR_OR_PUBLIC);
    if (file == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(file, HttpStatus.OK);
  }

  /**
   * Update file
   *
   * @param id   File id
   * @param body
   */
  @PutMapping(value = "/{id}")
  public ResponseEntity<Object> putFile(@PathVariable Long id, @NonNull @RequestBody JsonFileBody body) {
    if (!body.putDataOk()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    final File file = fileService.updateFile(id, body);
    if (file == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Delete file
   *
   * @param id File id
   */
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Object> deleteFile(@PathVariable Long id) {
    final boolean isFile = fileService.deleteFile(id);
    if (isFile) resourceService.deleteResources(id);
    else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /*
   * Resource
   */

  /**
   * Save resource
   *
   * @param id   File id
   * @param file Resource
   */
  @PostMapping(value = "/{id}/resource")
  public ResponseEntity<Object> postResource(@PathVariable Long id, @RequestParam("file") MultipartFile file, @Nullable @RequestParam("name") String name) {
    final String fileName = resourceService.saveResource(id, file, name);
    if (fileName == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    Map<String, Object> data = new HashMap<>();
    data.put("uri", fileName);

    return new ResponseEntity<>(data, HttpStatus.CREATED);
  }

  /**
   * Get resource
   *
   * @param id           File id
   * @param resourceName Resource name
   * @return
   */
  @GetMapping(value = "/{id}/resource/{resourceName:.+}")
  public ResponseEntity<InputStreamResource> getResource(@PathVariable Long id, @PathVariable String resourceName) {
    Resource resource = resourceService.getResource(id, resourceName);
    try {
      MediaType contentType = MediaType.parseMediaType(resource.getFile().toURL().openConnection().getContentType());
      InputStream in = resource.getInputStream();

      return ResponseEntity.ok()
        .contentType(contentType)
        .body(new InputStreamResource(in));
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Delete resource
   *
   * @param id           File id
   * @param resourceName Resource name
   */
  @DeleteMapping(value = "/{id}/resource/{resourceName:.+}")
  public ResponseEntity<Object> deleteResource(@PathVariable Long id, @PathVariable String resourceName) {
    final boolean isResource = resourceService.deleteResource(id, resourceName);
    if (!isResource) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /*
   * Metadata
   */

  /**
   * Update metadata
   *
   * @param id File id
   */
  @PutMapping(value = "/{id}/metadata")
  public ResponseEntity<List<Collaboration>> putMetadata(@PathVariable Long id, @NonNull @RequestBody JsonMetadataBody body) {
    if (!body.putDataOk()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    final boolean isMetadata = metadataService.updateMetadata(id, body);
    if (!isMetadata) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /*
   * Collaboration
   */

  /**
   * List sharing
   *
   * @param id File id
   * @return
   */
  @GetMapping(value = "/{id}/share")
  public ResponseEntity<List<Collaboration>> getShare(@PathVariable Long id) {
    return new ResponseEntity<>(collaborationService.getCollaborations(id), HttpStatus.OK);
  }

  /**
   * Share to someone
   *
   * @param id   File id
   * @param body
   */
  @PostMapping(value = "/{id}/share")
  public ResponseEntity<Object> postShare(@PathVariable Long id, @NonNull @RequestBody JsonCollaborationBody body) {
    if (!body.postDataOk()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    final boolean isSaved = collaborationService.saveCollaboration(id, body);
    if (!isSaved) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * Update a share
   *
   * @param id     File id
   * @param userId User id
   * @param body
   */
  @PutMapping(value = "/{id}/share/{userId}")
  public ResponseEntity<Object> putShare(@PathVariable Long id, @PathVariable Long userId, @NonNull @RequestBody JsonCollaborationBody body) {
    if (!body.putDataOk()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    final boolean isCollaboration = collaborationService.updateCollaboration(id, userId, body);
    if (!isCollaboration) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Delete a share
   *
   * @param id     File id
   * @param userId User id
   */
  @DeleteMapping(value = "/{id}/share/{userId}")
  public ResponseEntity<Object> deleteShare(@PathVariable Long id, @PathVariable Long userId) {
    final boolean isCollaboration = collaborationService.deleteCollaboration(id, userId);
    if (!isCollaboration) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Delete sharing
   *
   * @param id File id
   */
  @DeleteMapping(value = "/{id}/share")
  public ResponseEntity<Object> deleteShared(@PathVariable Long id) {
    final boolean isCollaborations = collaborationService.deleteCollaborations(id);
    if (!isCollaborations) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /*
   * History
   */

  /**
   * List histories
   *
   * @param id File id
   * @return
   */
  @GetMapping(value = "/{id}/history")
  public ResponseEntity<List<FileHistory>> getHistories(@PathVariable Long id) {
    return new ResponseEntity<>(fileHistoryService.getHistories(id), HttpStatus.OK);
  }

  /**
   * Create a history
   *
   * @param id   File id
   * @param body
   */
  @PostMapping(value = "/{id}/history")
  public ResponseEntity<Object> postHistory(@PathVariable Long id, @NonNull @RequestBody JsonHistoryBody body) {
    if (!body.postDataOk()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    final boolean isFileHistory = fileHistoryService.createHistory(id, body);
    if (!isFileHistory) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * Get a history
   *
   * @param id        File id
   * @param historyId FileHistory id
   * @return
   */
  @GetMapping(value = "/{id}/history/{historyId}")
  public ResponseEntity<FileHistory> getHistory(@PathVariable Long id, @PathVariable Long historyId) {
    return new ResponseEntity<>(fileHistoryService.getHistory(id, historyId), HttpStatus.OK);
  }

  /**
   * Delete a history
   *
   * @param id        File id
   * @param historyId FileHistory id
   */
  @DeleteMapping(value = "/{id}/history/{historyId}")
  public ResponseEntity<Object> deleteHistory(@PathVariable Long id, @PathVariable Long historyId) {
    final boolean isFileHistory = fileHistoryService.deleteHistory(id, historyId);
    if (!isFileHistory) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Revert a history
   *
   * @param id        File id
   * @param historyId FileHistory id
   */
  @PostMapping(value = "/{id}/history/{historyId}/revert")
  public ResponseEntity<Object> revertHistory(@PathVariable Long id, @PathVariable Long historyId) {
    final boolean isFileHistory = fileHistoryService.revertHistory(id, historyId);
    if (!isFileHistory) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Delete histories
   *
   * @param id File id
   */
  @DeleteMapping(value = "/{id}/history")
  public ResponseEntity<Object> deleteHistories(@PathVariable Long id) {
    final boolean isFileHistories = fileHistoryService.deleteHistories(id);
    if (!isFileHistories) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
