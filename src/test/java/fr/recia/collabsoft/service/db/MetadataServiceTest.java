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

import fr.recia.collabsoft.web.interceptor.bean.SoffitHolder;
import fr.recia.collabsoft.model.pojo.JsonMetadataBody;
import fr.recia.collabsoft.test.DatabaseUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import static fr.recia.collabsoft.test.DatabaseUtils.fileUnknownId;
import static fr.recia.collabsoft.test.DatabaseUtils.user1Sub;
import static fr.recia.collabsoft.test.DatabaseUtils.userGuestSub;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ComponentScan(basePackages = "fr.recia.collabsoft")
class MetadataServiceTest {

  @Autowired
  private DatabaseUtils databaseUtils;

  @Autowired
  private MetadataService metadataService;

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
  void updateMetadata_ShouldBeFalse_becauseUserOrFileDoesNotExist() {
    final JsonMetadataBody body = new JsonMetadataBody();
    body.setStarred(true);

    // Incorrect sub
    soffitHolder.setSub(userGuestSub);
    boolean result = metadataService.updateMetadata(data.getFile1Id(), body);
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
    boolean result = metadataService.updateMetadata(data.getFile1Id(), body);
    assertTrue(result);

    // Collaborate
    result = metadataService.updateMetadata(data.getFile3Id(), body);
    assertTrue(result);

    // Public
    result = metadataService.updateMetadata(data.getFile4Id(), body);
    assertTrue(result);
  }

}
