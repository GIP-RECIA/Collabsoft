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

import fr.recia.collabsoft.db.entity.Collaboration;
import fr.recia.collabsoft.db.enums.Role;
import fr.recia.collabsoft.web.interceptor.bean.SoffitHolder;
import fr.recia.collabsoft.model.pojo.JsonCollaborationBody;
import fr.recia.collabsoft.test.DatabaseUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

import static fr.recia.collabsoft.test.DatabaseUtils.fileUnknownId;
import static fr.recia.collabsoft.test.DatabaseUtils.user1Sub;
import static fr.recia.collabsoft.test.DatabaseUtils.user2Sub;
import static fr.recia.collabsoft.test.DatabaseUtils.user3Sub;
import static fr.recia.collabsoft.test.DatabaseUtils.userUnknownId;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ComponentScan(basePackages = "fr.recia.collabsoft")
class CollaborationServiceTest {

  @Autowired
  private DatabaseUtils databaseUtils;

  @Autowired
  private CollaborationService collaborationService;

  @Autowired
  private SoffitHolder soffitHolder;

  private DatabaseUtils.DataToId data;

  @BeforeEach
  public void setUp() {
    data = databaseUtils.insertData();
  }

  @AfterEach
  public void tearDown() {
    databaseUtils.deleteData();
  }

  @Test
  void getCollaborations_ShouldBeEmpty_becauseFileDoesNotExist() {
    final List<Collaboration> collaborations = collaborationService.getCollaborations(fileUnknownId);
    assertTrue(collaborations.isEmpty());
  }

  @Test
  void getCollaborations_ShouldBeEmpty_becauseThereAreNoCollaborations() {
    final List<Collaboration> collaborations = collaborationService.getCollaborations(data.getFile4Id());
    assertTrue(collaborations.isEmpty());
  }

  @Test
  void getCollaborations_ShouldNotBeEmpty() {
    final List<Collaboration> collaborations = collaborationService.getCollaborations(data.getFile2Id());
    assertFalse(collaborations.isEmpty());
  }

  @Test
  void saveCollaboration_ShouldBeFalse_becauseUserOrFileDoesNotExist() {
    final JsonCollaborationBody body = new JsonCollaborationBody();
    body.setUserId(userUnknownId);
    body.setRole(Role.EDITOR);

    soffitHolder.setSub(user2Sub);
    final boolean result = collaborationService.saveCollaboration(data.getFile4Id(), body);
    assertFalse(result);
  }

// TODO: Define collaborations permissions

//  @Test
//  void saveCollaboration_ShouldBeFalse_becauseRoleIsIncorrect() {
//    final JsonCollaborationBody body = new JsonCollaborationBody();
//    body.setUserId(data.getUser3Id());
//    body.setRole(Role.OWNER);
//
//    soffitHolder.setSub(user2Sub);
//    final boolean result = collaborationService.saveCollaboration(data.getFile4Id(), body);
//    assertFalse(result);
//  }

  @Test
  void saveCollaboration_ShouldBeFalse_becauseUserIsNotTheOwner() {
    final JsonCollaborationBody body = new JsonCollaborationBody();
    body.setUserId(data.getUser3Id());
    body.setRole(Role.EDITOR);

    soffitHolder.setSub(user3Sub);
    final boolean result = collaborationService.saveCollaboration(data.getFile4Id(), body);
    assertFalse(result);
  }

  @Test
  void saveCollaboration_ShouldBeTrue() {
    final JsonCollaborationBody body = new JsonCollaborationBody();
    body.setUserId(data.getUser3Id());
    body.setRole(Role.EDITOR);

    soffitHolder.setSub(user2Sub);
    final boolean result = collaborationService.saveCollaboration(data.getFile4Id(), body);
    assertTrue(result);
  }

  @Test
  void updateCollaboration_ShouldBeFalse_becauseUserOrFileDoesNotExist() {
    final JsonCollaborationBody body = new JsonCollaborationBody();
    body.setRole(Role.READONLY);

    soffitHolder.setSub(user1Sub);
    final boolean result = collaborationService.updateCollaboration(fileUnknownId, data.getUser2Id(), body);
    assertFalse(result);
  }

// TODO: Define collaborations permissions

//  @Test
//  void updateCollaboration_ShouldBeFalse_becauseRoleIsIncorrect() {
//    final JsonCollaborationBody body = new JsonCollaborationBody();
//    body.setRole(Role.OWNER);
//
//    soffitHolder.setSub(user1Sub);
//    final boolean result = collaborationService.updateCollaboration(data.getFile2Id(), data.getUser2Id(), body);
//    assertFalse(result);
//  }

  @Test
  void updateCollaboration_ShouldBeFalse_becauseUserIsNotOwner() {
    final JsonCollaborationBody body = new JsonCollaborationBody();
    body.setRole(Role.READONLY);

    soffitHolder.setSub(user2Sub);
    final boolean result = collaborationService.updateCollaboration(data.getFile2Id(), data.getUser2Id(), body);
    assertFalse(result);
  }

  @Test
  void updateCollaboration_ShouldBeTrue() {
    final JsonCollaborationBody body = new JsonCollaborationBody();
    body.setRole(Role.READONLY);

    soffitHolder.setSub(user1Sub);
    final boolean result = collaborationService.updateCollaboration(data.getFile2Id(), data.getUser2Id(), body);
    assertTrue(result);
  }

  @Test
  void deleteCollaboration_ShouldBeFalse_becauseUserOrFileDoesNotExist() {
    soffitHolder.setSub(user1Sub);
    final boolean result = collaborationService.deleteCollaboration(fileUnknownId, data.getUser2Id());
    assertFalse(result);
  }

  @Test
  void deleteCollaboration_ShouldBeFalse_becauseUserIsNotTheOwnerOrSelf() {
    soffitHolder.setSub(user3Sub);
    final boolean result = collaborationService.deleteCollaboration(data.getFile2Id(), data.getUser2Id());
    assertFalse(result);
  }

  @Test
  void deleteCollaboration_ShouldBeTrue() {
    // Owner
    soffitHolder.setSub(user1Sub);
    boolean result = collaborationService.deleteCollaboration(data.getFile2Id(), data.getUser2Id());
    assertTrue(result);

    // Self
    soffitHolder.setSub(user1Sub);
    result = collaborationService.deleteCollaboration(data.getFile3Id(), data.getUser1Id());
    assertTrue(result);
  }

  @Test
  void deleteCollaborations_ShouldBeFalse_becauseThereAreNoCollaborations() {
    soffitHolder.setSub(user1Sub);
    final boolean result = collaborationService.deleteCollaborations(data.getFile1Id());
    assertFalse(result);
  }

  @Test
  void deleteCollaborations_ShouldBeFalse_becauseUserIsNotTheOwner() {
    soffitHolder.setSub(user1Sub);
    final boolean result = collaborationService.deleteCollaborations(data.getFile3Id());
    assertFalse(result);
  }

  @Test
  void deleteCollaborations_ShouldBeTrue() {
    soffitHolder.setSub(user1Sub);
    final boolean result = collaborationService.deleteCollaborations(data.getFile2Id());
    assertTrue(result);
  }

}
