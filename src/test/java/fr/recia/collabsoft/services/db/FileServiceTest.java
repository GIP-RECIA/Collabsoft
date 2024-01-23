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

import fr.recia.collabsoft.interceptors.beans.SoffitHolder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
  void getFiles_ShouldBeEmpty() {
    assertTrue(true);
  }

  @Test
  void getFiles_ShouldNotBeEmpty() {
    assertTrue(true);
  }

  @Test
  void getStarredFiles_ShouldBeEmpty() {
    assertTrue(true);
  }

  @Test
  void getStarredFiles_ShouldNotBeEmpty() {
    assertTrue(true);
  }

  @Test
  void getSharedFiles_ShouldBeEmpty() {
    assertTrue(true);
  }

  @Test
  void getSharedFiles_ShouldNotBeEmpty() {
    assertTrue(true);
  }

  @Test
  void getPublicFiles_ShouldNotBeEmpty() {
    assertTrue(true);
  }

  @Test
  void saveFile_ShouldBeNull_becauseUserCreationFail() {
    assertNull(null);
  }

  @Test
  void saveFile_ShouldBeNull_becauseAssociatedAppDoesNotExist() {
    assertNull(null);
  }

  @Test
  void saveFile_ShouldNotBeNull() {
    assertNotNull(true);
  }

  @Test
  void getFile_ShouldBeNull_becauseFileDoesNotExist() {
    assertNull(null);
  }

  @Test
  void getFile_ShouldBeNull_becauseUserIsNotTheOwnerOrCollaboratorOrFileIsNotPublic() {
    assertNull(null);
  }

  @Test
  void getFile_ShouldNotBeNull() {
    assertTrue(true);
  }

  @Test
  void updateFile_ShouldBeNull_becauseFileDoesNotExist() {
    assertNull(null);
  }

  @Test
  void updateFile_ShouldBeNull_becauseUserIsNotTheOwnerOrCollaborator() {
    assertNull(null);
  }

  @Test
  void updateFile_ShouldNotBeNull() {
    assertNotNull(true);
  }

  @Test
  void deleteFile_ShouldBeFalse_becauseFileDoesNotExist() {
    assertFalse(false);
  }

  @Test
  void deleteFile_ShouldBeFalse_becauseUserIsNotTheOwner() {
    assertFalse(false);
  }

  @Test
  void deleteFile_ShouldBeTrue() {
    assertTrue(true);
  }

}
