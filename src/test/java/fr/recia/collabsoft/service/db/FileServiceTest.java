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

import fr.recia.collabsoft.db.entity.File;
import fr.recia.collabsoft.interceptor.bean.SoffitHolder;
import fr.recia.collabsoft.model.enums.Authority;
import fr.recia.collabsoft.model.pojo.JsonFileBody;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static fr.recia.collabsoft.test.DatabaseUtils.associatedApp1Id;
import static fr.recia.collabsoft.test.DatabaseUtils.associatedAppUnknownId;
import static fr.recia.collabsoft.test.DatabaseUtils.file1Id;
import static fr.recia.collabsoft.test.DatabaseUtils.file2Id;
import static fr.recia.collabsoft.test.DatabaseUtils.file3Id;
import static fr.recia.collabsoft.test.DatabaseUtils.file4Id;
import static fr.recia.collabsoft.test.DatabaseUtils.file6Id;
import static fr.recia.collabsoft.test.DatabaseUtils.fileUnknownId;
import static fr.recia.collabsoft.test.DatabaseUtils.user1Sub;
import static fr.recia.collabsoft.test.DatabaseUtils.user2Sub;
import static fr.recia.collabsoft.test.DatabaseUtils.user3Sub;
import static fr.recia.collabsoft.test.DatabaseUtils.userGuestSub;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FileServiceTest {

  @Autowired
  private FileService fileService;

  @Autowired
  private SoffitHolder soffitHolder;

  @Test
  void getFiles_ShouldNotBeEmpty() {
    soffitHolder.setSub(user1Sub);
    final List<File> files = fileService.getFiles();
    assertTrue(files.isEmpty());
  }

  @Test
  void getStarredFiles_ShouldBeEmpty() {
    soffitHolder.setSub(user2Sub);
    final List<File> files = fileService.getStarredFiles();
    assertTrue(files.isEmpty());
  }

  @Test
  void getStarredFiles_ShouldNotBeEmpty() {
    soffitHolder.setSub(user1Sub);
    final List<File> files = fileService.getStarredFiles();
    assertTrue(files.isEmpty());
  }

  @Test
  void getSharedFiles_ShouldBeEmpty() {
    soffitHolder.setSub(user3Sub);
    final List<File> files = fileService.getSharedFiles();
    assertTrue(files.isEmpty());
  }

  @Test
  void getSharedFiles_ShouldNotBeEmpty() {
    soffitHolder.setSub(user1Sub);
    final List<File> files = fileService.getSharedFiles();
    assertTrue(files.isEmpty());
  }

  @Test
  void getPublicFiles_ShouldNotBeEmpty() {
    final List<File> files = fileService.getPublicFiles();
    assertTrue(files.isEmpty());
  }

  @Test
  void saveFile_ShouldBeNull_becauseUserCreationFail() {
    final JsonFileBody body = new JsonFileBody();
    body.setTitle("TEST SAVE");
    body.setDescription(null);
    body.setData("some data");
    body.setAssociatedAppId(associatedApp1Id);
    body.setPub(false);

    soffitHolder.setSub(userGuestSub);
    final File file = fileService.saveFile(body);
    assertNull(file);
  }

  @Test
  void saveFile_ShouldBeNull_becauseAssociatedAppDoesNotExist() {
    final JsonFileBody body = new JsonFileBody();
    body.setTitle("TEST SAVE");
    body.setDescription(null);
    body.setData("some data");
    body.setAssociatedAppId(associatedAppUnknownId);
    body.setPub(false);

    soffitHolder.setSub(user1Sub);
    final File file = fileService.saveFile(body);
    assertNull(file);
  }

  @Test
  void saveFile_ShouldNotBeNull() {
    final JsonFileBody body = new JsonFileBody();
    body.setTitle("TEST SAVE");
    body.setDescription(null);
    body.setData("some data");
    body.setAssociatedAppId(associatedApp1Id);
    body.setPub(false);

    // Already exist user
    soffitHolder.setSub(user1Sub);
    File file = fileService.saveFile(body);
    assertNotNull(file);

    // New user
    soffitHolder.setSub(user1Sub);
    file = fileService.saveFile(body);
    assertNotNull(file);
  }

  @Test
  void getFile_ShouldBeNull_becauseFileDoesNotExist() {
    soffitHolder.setSub(user1Sub);
    final File file = fileService.getFile(fileUnknownId, Authority.OWNER_OR_COLLABORATOR_OR_PUBLIC);
    assertNull(file);
  }

  @Test
  void getFile_ShouldBeNull_becauseUserIsNotTheOwnerOrCollaboratorOrFileIsNotPublic() {
    soffitHolder.setSub(user1Sub);
    final File file = fileService.getFile(file6Id, Authority.OWNER_OR_COLLABORATOR_OR_PUBLIC);
    assertNull(file);
  }

  @Test
  void getFile_ShouldNotBeNull() {
    soffitHolder.setSub(user1Sub);

    // Owned file
    File file = fileService.getFile(file1Id, Authority.OWNER_OR_COLLABORATOR_OR_PUBLIC);
    assertNotNull(file);

    // Collaborate file
    file = fileService.getFile(file3Id, Authority.OWNER_OR_COLLABORATOR_OR_PUBLIC);
    assertNotNull(file);

    // Public file
    file = fileService.getFile(file4Id, Authority.OWNER_OR_COLLABORATOR_OR_PUBLIC);
    assertNotNull(file);
  }

  @Test
  void updateFile_ShouldBeNull_becauseFileDoesNotExist() {
    final JsonFileBody body = new JsonFileBody();
    body.setTitle("UPDATED");

    soffitHolder.setSub(user1Sub);
    final File file = fileService.updateFile(fileUnknownId, body);
    assertNull(file);
  }

  @Test
  void updateFile_ShouldBeNull_becauseUserIsNotTheOwnerOrCollaborator() {
    final JsonFileBody body = new JsonFileBody();
    body.setTitle("UPDATED");

    soffitHolder.setSub(user3Sub);
    final File file = fileService.updateFile(file1Id, body);
    assertNull(file);
  }

  @Test
  void updateFile_ShouldNotBeNull() {
    final JsonFileBody body = new JsonFileBody();
    body.setTitle("UPDATED");

    // Owner
    soffitHolder.setSub(user1Sub);
    File file = fileService.updateFile(file1Id, body);
    assertNotNull(file);

    // Collaborator
    soffitHolder.setSub(user2Sub);
    file = fileService.updateFile(file2Id, body);
    assertNotNull(file);
  }

  @Test
  void deleteFile_ShouldBeFalse_becauseFileDoesNotExist() {
    soffitHolder.setSub(user1Sub);
    final boolean result = fileService.deleteFile(fileUnknownId);
    assertFalse(result);
  }

  @Test
  void deleteFile_ShouldBeFalse_becauseUserIsNotTheOwner() {
    soffitHolder.setSub(user2Sub);
    final boolean result = fileService.deleteFile(file2Id);
    assertFalse(result);
  }

  @Test
  void deleteFile_ShouldBeTrue() {
    soffitHolder.setSub(user1Sub);
    final boolean result = fileService.deleteFile(file1Id);
    assertTrue(result);
  }

}
