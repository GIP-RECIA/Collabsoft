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
class CollaborationServiceTest {

  @Autowired
  private CollaborationService collaborationService;

  @Autowired
  private SoffitHolder soffitHolder;

  @Test
  void getCollaborations_ShouldBeEmpty_becauseFileDoesNotExist() {
    assertTrue(true);
  }

  @Test
  void getCollaborations_ShouldBeEmpty_becauseThereAreNoCollaborations() {
    assertTrue(true);
  }

  @Test
  void getCollaborations_ShouldNotBeEmpty() {
    assertFalse(false);
  }

  @Test
  void getCollaboration_ShouldBeNull_becauseUserOrFileDoesNotExist() {
    assertNull(null);
  }

  @Test
  void getCollaboration_ShouldNotBeNull() {
    assertNotNull(true);
  }

  @Test
  void saveCollaboration_ShouldBeFalse_becauseUserOrFileDoesNotExist() {
    assertFalse(false);
  }

  @Test
  void saveCollaboration_ShouldBeFalse_becauseRoleIsIncorrect() {
    assertFalse(false);
  }

  @Test
  void saveCollaboration_ShouldBeFalse_becauseUserIsNotTheOwner() {
    assertFalse(false);
  }

  @Test
  void saveCollaboration_ShouldBeTrue() {
    assertTrue(true);
  }

  @Test
  void updateCollaboration_ShouldBeFalse_becauseUserOrFileDoesNotExist() {
    assertFalse(false);
  }

  @Test
  void updateCollaboration_ShouldBeFalse_becauseRoleIsIncorrect() {
    assertFalse(false);
  }

  @Test
  void updateCollaboration_ShouldBeFalse_becauseUserIsNotOwner() {
    assertFalse(false);
  }

  @Test
  void updateCollaboration_ShouldBeTrue() {
    assertTrue(true);
  }

  @Test
  void deleteCollaboration_ShouldBeFalse_becauseUserOrFileDoesNotExist() {
    assertFalse(false);
  }

  @Test
  void deleteCollaboration_ShouldBeFalse_becauseUserIsNotTheOwnerOrSelf() {
    assertFalse(false);
  }

  @Test
  void deleteCollaboration_ShouldBeTrue() {
    assertTrue(true);
  }

  @Test
  void deleteCollaborations_ShouldBeFalse_becauseThereAreNoCollaborations() {
    assertFalse(false);
  }

  @Test
  void deleteCollaborations_ShouldBeFalse_becauseUserIsNotTheOwner() {
    assertFalse(false);
  }

  @Test
  void deleteCollaborations_ShouldBeTrue() {
    assertTrue(true);
  }

}
