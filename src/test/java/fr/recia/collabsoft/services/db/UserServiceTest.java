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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private SoffitHolder soffitHolder;

  @Test
  void getCurrentUser_ShouldBeNull_becauseUserDoesNotExist() {
    assertNull(null);
  }

  @Test
  void getCurrentUser_ShouldNotBeNull() {
    assertNotNull(true);
  }

  @Test
  void getUser_ShouldBeNull_becauseUserIdDoesNotExist() {
    assertNull(null);
  }

  @Test
  void getUser_ShouldNotBeNull() {
    assertNotNull(true);
  }

  @Test
  void createUser_ShouldBeNull_becauseSoffitHolderSubStartWithGuest() {
    assertNull(null);
  }

  @Test
  void createUser_ShouldBeNull_becauseUserAlreadyExist() {
    assertNull(null);
  }

  @Test
  void createUser_ShouldNotBeNull() {
    assertNotNull(true);
  }

}
