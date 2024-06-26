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

import fr.recia.collabsoft.db.entity.AssociatedApp;
import fr.recia.collabsoft.db.repository.AssociatedAppRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AssociatedAppService {

  @Autowired
  private AssociatedAppRepository<AssociatedApp> associatedAppRepository;

  public List<AssociatedApp> getApps() {
    final List<AssociatedApp> apps = IteratorUtils.toList(
      associatedAppRepository.findAll().iterator()
    );

    if (apps.isEmpty()) log.error("No apps defined");

    return apps;
  }

}
