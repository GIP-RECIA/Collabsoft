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

import fr.recia.collabsoft.db.entities.FileHistory;
import fr.recia.collabsoft.interceptors.beans.SoffitHolder;
import fr.recia.collabsoft.pojo.JsonHistoryBody;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static fr.recia.collabsoft.test.DatabaseUtils.file1Id;
import static fr.recia.collabsoft.test.DatabaseUtils.file2Id;
import static fr.recia.collabsoft.test.DatabaseUtils.file3Id;
import static fr.recia.collabsoft.test.DatabaseUtils.file4Id;
import static fr.recia.collabsoft.test.DatabaseUtils.file5Id;
import static fr.recia.collabsoft.test.DatabaseUtils.file6Id;
import static fr.recia.collabsoft.test.DatabaseUtils.fileUnknownId;
import static fr.recia.collabsoft.test.DatabaseUtils.history1Id;
import static fr.recia.collabsoft.test.DatabaseUtils.history2Id;
import static fr.recia.collabsoft.test.DatabaseUtils.history3Id;
import static fr.recia.collabsoft.test.DatabaseUtils.historyUnknownId;
import static fr.recia.collabsoft.test.DatabaseUtils.user1Sub;
import static fr.recia.collabsoft.test.DatabaseUtils.user3Sub;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FileHistoryServiceTest {

  @Autowired
  private FileHistoryService fileHistoryService;

  @Autowired
  private SoffitHolder soffitHolder;

  @Test
  void getHistories_ShouldBeEmpty_becauseFileHasNoHistories() {
    soffitHolder.setSub(user3Sub);
    final List<FileHistory> histories = fileHistoryService.getHistories(file6Id);
    assertTrue(histories.isEmpty());
  }

  @Test
  void getHistories_ShouldBeEmpty_becauseUserIsNotTheOwnerOrCollaboratorOrFileIsNotPublic() {
    soffitHolder.setSub(user1Sub);
    final List<FileHistory> histories = fileHistoryService.getHistories(file5Id);
    assertTrue(histories.isEmpty());
  }

  @Test
  void getHistories_ShouldNotBeEmpty() {
    // Owned
    soffitHolder.setSub(user3Sub);
    List<FileHistory> histories = fileHistoryService.getHistories(file5Id);
    assertFalse(histories.isEmpty());

    // Public
    soffitHolder.setSub(user3Sub);
    histories = fileHistoryService.getHistories(file4Id);
    assertFalse(histories.isEmpty());
  }

  @Test
  void createHistory_ShouldBeFalse_becauseFileDoesNotExist() {
    final JsonHistoryBody body = new JsonHistoryBody();
    body.setBlob("history".getBytes());

    soffitHolder.setSub(user1Sub);
    final boolean result = fileHistoryService.createHistory(fileUnknownId, body);
    assertFalse(result);
  }

  @Test
  void createHistory_ShouldBeFalse_becauseUserIsNotTheOwner() {
    final JsonHistoryBody body = new JsonHistoryBody();
    body.setBlob("history".getBytes());

    soffitHolder.setSub(user1Sub);
    final boolean result = fileHistoryService.createHistory(file3Id, body);
    assertFalse(result);
  }

  @Test
  void createHistory_ShouldBeTrue() {
    final JsonHistoryBody body = new JsonHistoryBody();
    body.setBlob("history".getBytes());

    soffitHolder.setSub(user1Sub);
    final boolean result = fileHistoryService.createHistory(file1Id, body);
    assertTrue(result);
  }

  @Test
  void getHistory_ShouldBeNull_becauseHistoryDoesNotExist() {
    soffitHolder.setSub(user3Sub);

    // fileId does not exist or match with historyId
    FileHistory fileHistory = fileHistoryService.getHistory(file6Id, history2Id);
    assertNull(fileHistory);

    // historyId does not exist or match with fileId
    fileHistory = fileHistoryService.getHistory(file5Id, historyUnknownId);
    assertNull(fileHistory);
  }

  @Test
  void getHistory_ShouldBeNull_becauseUserIsNotTheOwnerOrCollaboratorOrFileIsNotPublic() {
    soffitHolder.setSub(user3Sub);
    final FileHistory fileHistory = fileHistoryService.getHistory(file2Id, history3Id);
    assertNull(fileHistory);
  }

  @Test
  void getHistory_ShouldNotBeNull() {
    soffitHolder.setSub(user3Sub);
    final FileHistory fileHistory = fileHistoryService.getHistory(file5Id, history1Id);
    assertNotNull(fileHistory);
  }

  @Test
  void deleteHistory_ShouldBeFalse_becauseHistoryDoesNotExist() {
    soffitHolder.setSub(user3Sub);

    // fileId does not exist or match with historyId
    boolean result = fileHistoryService.deleteHistory(file6Id, history2Id);
    assertFalse(result);

    // historyId does not exist or match with fileId
    result = fileHistoryService.deleteHistory(file5Id, history3Id);
    assertFalse(result);
  }

  @Test
  void deleteHistory_ShouldBeFalse_becauseUserIsNotTheOwner() {
    soffitHolder.setSub(user3Sub);
    final boolean result = fileHistoryService.deleteHistory(file2Id, history3Id);
    assertFalse(result);
  }

  @Test
  void deleteHistory_ShouldBeTrue() {
    soffitHolder.setSub(user3Sub);
    final boolean result = fileHistoryService.deleteHistory(file5Id, history1Id);
    assertTrue(result);
  }

  @Test
  void revertHistory_ShouldBeFalse_becauseUserIsNotTheOwner() {
    soffitHolder.setSub(user3Sub);
    final boolean result = fileHistoryService.revertHistory(file2Id, history3Id);
    assertFalse(result);
  }

  @Test
  void revertHistory_ShouldBeFalse_becauseThereIsNoHistory() {
    soffitHolder.setSub(user3Sub);

    // fileId does not exist or match with historyId
    boolean result = fileHistoryService.revertHistory(file6Id, history2Id);
    assertFalse(result);

    // historyId does not exist or match with fileId
    result = fileHistoryService.revertHistory(file5Id, history3Id);
    assertFalse(result);
  }

  @Test
  void revertHistory_ShouldBeTrue() {
    soffitHolder.setSub(user3Sub);
    final boolean result = fileHistoryService.revertHistory(file5Id, history1Id);
    assertTrue(result);
  }

  @Test
  void deleteHistories_ShouldBeFalse_becauseUserIsNotTheOwner() {
    soffitHolder.setSub(user3Sub);
    final boolean result = fileHistoryService.deleteHistories(file2Id);
    assertFalse(result);
  }

  @Test
  void deleteHistories_ShouldBeFalse_becauseThereIsNoHistories() {
    soffitHolder.setSub(user3Sub);
    final boolean result = fileHistoryService.deleteHistories(file6Id);
    assertFalse(result);
  }

  @Test
  void deleteHistories_ShouldBeTrue() {
    soffitHolder.setSub(user3Sub);
    final boolean result = fileHistoryService.deleteHistories(file5Id);
    assertTrue(result);
  }

}
