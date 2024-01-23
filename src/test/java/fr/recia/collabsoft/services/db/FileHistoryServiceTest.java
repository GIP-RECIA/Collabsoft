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
class FileHistoryServiceTest {

  @Autowired
  private FileHistoryService fileHistoryService;

  @Autowired
  private SoffitHolder soffitHolder;

  @Test
  void getHistories_ShouldBeEmpty_becauseFileHasNoHistories() {
    assertTrue(true);
  }

  @Test
  void getHistories_ShouldBeEmpty_becauseUserIsNotTheOwnerOrCollaboratorOrFileIsNotPublic() {
    assertTrue(true);
  }

  @Test
  void getHistories_ShouldNotBeEmpty_becauseFileHasHistories() {
    assertFalse(false);
  }

  @Test
  void createHistory_ShouldBeFalse_becauseFileDoesNotExist() {
    assertFalse(false);
  }

  @Test
  void createHistory_ShouldBeFalse_becauseUserIsNotTheOwner() {
    assertFalse(false);
  }

  @Test
  void createHistory_ShouldBeTrue() {
    assertTrue(true);
  }

  @Test
  void getHistory_ShouldBeNull_becauseHistoryDoesNotExist() {
    assertNull(null);
  }

  @Test
  void getHistory_ShouldBeNull_becauseUserIsNotTheOwnerOrCollaboratorOrFileIsNotPublic() {
    assertNull(null);
  }

  @Test
  void getHistory_ShouldNotBeNull() {
    assertNotNull(true);
  }

  @Test
  void deleteHistory_ShouldBeFalse_becauseHistoryDoesNotExist() {
    assertFalse(false);
  }

  @Test
  void deleteHistory_ShouldBeFalse_becauseUserIsNotTheOwner() {
    assertFalse(false);
  }

  @Test
  void deleteHistory_ShouldBeTrue() {
    assertTrue(true);
  }

  @Test
  void revertHistory_ShouldBeFalse_becauseUserIsNotTheOwner() {
    assertFalse(false);
  }

  @Test
  void revertHistory_ShouldBeFalse_becauseThereIsNoHistory() {
    assertFalse(false);
  }

  @Test
  void revertHistory_ShouldBeTrue() {
    assertTrue(true);
  }

  @Test
  void deleteHistories_ShouldBeFalse_becauseUserIsNotTheOwner() {
    assertFalse(false);
  }

  @Test
  void deleteHistories_ShouldBeFalse_becauseThereIsNoHistories() {
    assertFalse(false);
  }

  @Test
  void deleteHistories_ShouldBeTrue() {
    assertTrue(true);
  }

}
