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

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FileControllerTest {

  private MockMvc mockMvc;

  @PostConstruct
  public void setup() {
    FileController fileController = new FileController();
    this.mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
  }

  /*
   * File
   */

  @Test
  void getFiles() throws Exception {
    assertTrue(true);
  }

  @Test
  void getStarredFiles() throws Exception {
    assertTrue(true);
  }

  @Test
  void getSharedFiles() throws Exception {
    assertTrue(true);
  }

  @Test
  void postFile() throws Exception {
    assertTrue(true);
  }

  @Test
  void getFile() throws Exception {
    assertTrue(true);
  }

  @Test
  void putFile() throws Exception {
    assertTrue(true);
  }

  @Test
  void deleteFile() throws Exception {
    assertTrue(true);
  }

  /*
   * Resource
   */

  @Test
  void postResource() throws Exception {
    assertTrue(true);
  }

  @Test
  void getResource() throws Exception {
    assertTrue(true);
  }

  @Test
  void deleteResource() throws Exception {
    assertTrue(true);
  }

  /*
   * Metadata
   */

  @Test
  void putMetadata() throws Exception {
    assertTrue(true);
  }

  /*
   * Collaboration
   */

  @Test
  void getShare() throws Exception {
    assertTrue(true);
  }

  @Test
  void postShare() throws Exception {
    assertTrue(true);
  }

  @Test
  void putShare() throws Exception {
    assertTrue(true);
  }

  @Test
  void deleteShare() throws Exception {
    assertTrue(true);
  }

  @Test
  void deleteShared() throws Exception {
    assertTrue(true);
  }

  /*
   * History
   */

  @Test
  void getHistories() throws Exception {
    assertTrue(true);
  }

  @Test
  void postHistory() throws Exception {
    assertTrue(true);
  }

  @Test
  void getHistory() throws Exception {
    assertTrue(true);
  }

  @Test
  void deleteHistory() throws Exception {
    assertTrue(true);
  }

  @Test
  void revertHistory() throws Exception {
    assertTrue(true);
  }

  @Test
  void deleteHistories() throws Exception {
    assertTrue(true);
  }

}
