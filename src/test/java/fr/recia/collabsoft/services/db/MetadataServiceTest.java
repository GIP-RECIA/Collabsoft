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
import fr.recia.collabsoft.pojo.JsonMetadataBody;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static fr.recia.collabsoft.test.DatabaseUtils.file1Id;
import static fr.recia.collabsoft.test.DatabaseUtils.file3Id;
import static fr.recia.collabsoft.test.DatabaseUtils.file4Id;
import static fr.recia.collabsoft.test.DatabaseUtils.fileUnknownId;
import static fr.recia.collabsoft.test.DatabaseUtils.user1Sub;
import static fr.recia.collabsoft.test.DatabaseUtils.userGuestSub;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MetadataServiceTest {

  @Autowired
  private MetadataService metadataService;

  @Autowired
  private SoffitHolder soffitHolder;

  @Test
  void updateMetadata_ShouldBeFalse_becauseUserOrFileDoesNotExist() {
    final JsonMetadataBody body = new JsonMetadataBody();
    body.setStarred(true);

    // Incorrect sub
    soffitHolder.setSub(userGuestSub);
    boolean result = metadataService.updateMetadata(file1Id, body);
    assertFalse(result);

    // Incorrect file id
    soffitHolder.setSub(user1Sub);
    result = metadataService.updateMetadata(fileUnknownId, body);
    assertFalse(result);
  }

  @Test
  void updateMetadata_ShouldBeTrue() {
    final JsonMetadataBody body = new JsonMetadataBody();
    body.setStarred(true);
    soffitHolder.setSub(user1Sub);

    // Owned
    boolean result = metadataService.updateMetadata(file1Id, body);
    assertTrue(result);

    // Collaborate
    result = metadataService.updateMetadata(file3Id, body);
    assertTrue(result);

    // Public
    result = metadataService.updateMetadata(file4Id, body);
    assertTrue(result);
  }

}
