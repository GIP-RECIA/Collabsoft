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

import fr.recia.collabsoft.test.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class HealthCheckTest {

  private MockMvc mockMvc;

  @PostConstruct
  public void setup() {
    HealthCheckController healthCheckController = new HealthCheckController();
    this.mockMvc = MockMvcBuilders.standaloneSetup(healthCheckController).build();
  }

  @Test
  void healthCheck() throws Exception {
    mockMvc.perform(head("/health-check")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .accept(TestUtil.APPLICATION_JSON_UTF8))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(status().isOk());
  }

}
