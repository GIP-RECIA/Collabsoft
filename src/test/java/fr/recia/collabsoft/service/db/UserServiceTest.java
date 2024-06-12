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

import fr.recia.collabsoft.db.entity.User;
import fr.recia.collabsoft.interceptor.bean.SoffitHolder;
import fr.recia.collabsoft.test.DatabaseUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import static fr.recia.collabsoft.test.DatabaseUtils.user1Sub;
import static fr.recia.collabsoft.test.DatabaseUtils.user4Sub;
import static fr.recia.collabsoft.test.DatabaseUtils.userGuestSub;
import static fr.recia.collabsoft.test.DatabaseUtils.userUnknownId;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ComponentScan(basePackages = "fr.recia.collabsoft")
class UserServiceTest {

  @Autowired
  private DatabaseUtils databaseUtils;

  @Autowired
  private UserService userService;

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
  void getCurrentUser_ShouldBeNull_becauseUserDoesNotExist() {
    soffitHolder.setSub(userGuestSub);
    final User user = userService.getCurrentUser();
    assertNull(user);
  }

  @Test
  void getCurrentUser_ShouldNotBeNull() {
    soffitHolder.setSub(user1Sub);
    final User user = userService.getCurrentUser();
    assertNotNull(user);
  }

  @Test
  void getUser_ShouldBeNull_becauseUserIdDoesNotExist() {
    final User user = userService.getUser(userUnknownId);
    assertNull(user);
  }

  @Test
  void getUser_ShouldNotBeNull() {
    final User user = userService.getUser(data.getUser1Id());
    assertNotNull(user);
  }

  @Test
  void createUser_ShouldBeNull_becauseSoffitHolderSubStartWithGuest() {
    soffitHolder.setSub(userGuestSub);
    final User user = userService.createUser();
    assertNull(user);
  }

  @Test
  void createUser_ShouldBeNull_becauseUserAlreadyExist() {
    soffitHolder.setSub(user1Sub);
    final User user = userService.createUser();
    assertNull(user);
  }

  @Test
  void createUser_ShouldNotBeNull() {
    soffitHolder.setSub(user4Sub);
    final User user = userService.createUser();
    assertNotNull(user);
  }

}
