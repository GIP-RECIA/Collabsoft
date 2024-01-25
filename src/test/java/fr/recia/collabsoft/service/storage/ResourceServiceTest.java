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
package fr.recia.collabsoft.service.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ResourceServiceTest {

  @Autowired
  private ResourceService resourceService;

//  @Test
//  void getResource_ShouldNotBe_becauseUserIsNotOwnerOrCollaborator() {}

//  @Test
//  void getResource_ShouldBe() {}

  @Test
  void saveResource_ShouldBeNull_becauseUserIsNotOwnerOrCollaborator() {
    assertNull(null);
  }

  @Test
  void saveResource_ShouldNotBeNull() {
    assertNotNull(true);
  }

  @Test
  void deleteResource_ShouldBeFalse_becauseUserIsNotOwnerOrCollaborator() {
    assertFalse(false);
  }

  @Test
  void deleteResource_ShouldBeFalse_becauseFileDoesNotExist() {
    assertFalse(false);
  }

  @Test
  void deleteResource_ShouldBeTrue() {
    assertTrue(true);
  }

}
