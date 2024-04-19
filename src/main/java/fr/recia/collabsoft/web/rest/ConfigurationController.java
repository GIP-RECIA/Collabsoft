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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.recia.collabsoft.configuration.CollabsoftProperties;
import fr.recia.collabsoft.service.db.AssociatedAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/config")
public class ConfigurationController {

  @Autowired
  private CollabsoftProperties collabsoftProperties;
  @Autowired
  private AssociatedAppService associatedAppService;

  @GetMapping
  public ResponseEntity<Object> getConfiguration() {
    Map<String, Object> data = new HashMap<>();
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> front = objectMapper.convertValue(collabsoftProperties.getFront(), new TypeReference<>() {
    });
    front.put("apps", associatedAppService.getApps());
    data.put("front", front);

    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
