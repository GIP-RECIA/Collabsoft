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

import fr.recia.collabsoft.db.entities.QUser;
import fr.recia.collabsoft.db.entities.User;
import fr.recia.collabsoft.db.repositories.UserRepository;
import fr.recia.collabsoft.interceptors.beans.SoffitHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

  @Autowired
  private UserRepository<User> userRepository;

  private final SoffitHolder soffitHolder;

  public UserService(SoffitHolder soffitHolder) {
    this.soffitHolder = soffitHolder;
  }

  public User getCurrentUser() {
    return userRepository.findOne(
      QUser.user.casUid.eq(soffitHolder.getSub())
    ).orElse(null);
  }

  public User getUser(Long userId) {
    return userRepository.findOne(
      QUser.user.id.eq(userId)
    ).orElse(null);
  }

  public User createUser() {
    if (soffitHolder.getSub() != null && !soffitHolder.getSub().startsWith("guest")) {
      User user = new User();
      user.setCasUid(soffitHolder.getSub());
      userRepository.saveAndFlush(user);

      return getCurrentUser();
    }
    log.info("Unable to create user with sub \"{}\"", soffitHolder.getSub());

    return null;
  }

}
