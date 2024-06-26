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

import fr.recia.collabsoft.db.entity.QUser;
import fr.recia.collabsoft.db.entity.User;
import fr.recia.collabsoft.db.repository.UserRepository;
import fr.recia.collabsoft.interceptor.bean.SoffitHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

  @Autowired
  private UserRepository<User> userRepository;

  @Autowired
  private SoffitHolder soffitHolder;

  private boolean isSubOk() {
    final boolean isOk = soffitHolder.getSub() != null && !soffitHolder.getSub().startsWith("guest");
    if (!isOk) log.debug("User is guest : sub \"{}\"", soffitHolder.getSub());

    return isOk;
  }

  public User getCurrentUser() {
    if (!isSubOk()) return null;
    final User user = userRepository.findOne(
      QUser.user.casUid.eq(soffitHolder.getSub())
    ).orElse(null);

    if (user == null) log.debug("No user found with sub \"{}\"", soffitHolder.getSub());

    return user;
  }

  public User getUser(Long userId) {
    final User user = userRepository.findOne(
      QUser.user.id.eq(userId)
    ).orElse(null);

    if (user == null) log.debug("No user found with id \"{}\"", userId);

    return user;
  }

  public User createUser() {
    if (!isSubOk()) return null;
    if (getCurrentUser() != null) {
      log.debug("User with sub \"{}\" already exist", soffitHolder.getSub());

      return null;
    }

    User user = new User();
    user.setCasUid(soffitHolder.getSub());
    userRepository.saveAndFlush(user);

    return getCurrentUser();
  }

}
