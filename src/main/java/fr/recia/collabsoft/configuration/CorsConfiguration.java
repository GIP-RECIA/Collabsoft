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

package fr.recia.collabsoft.configuration;

import fr.recia.collabsoft.configuration.bean.CorsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@Slf4j
public class CorsConfiguration {

  private final CorsProperties corsProperties;

  public CorsConfiguration(CollabsoftProperties collabsoftProperties) {
    this.corsProperties = collabsoftProperties.getCors();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    log.info("CORS: {}", corsProperties.isEnable());
    if (corsProperties.isEnable()) {
      org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();

      configuration.setAllowCredentials(corsProperties.isAllowCredentials());
      configuration.setAllowedOrigins(corsProperties.getAllowedOrigins());
      configuration.setExposedHeaders(corsProperties.getExposedHeaders());
      configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
      configuration.setAllowedMethods(corsProperties.getAllowedMethods());

      source.registerCorsConfiguration("/**", configuration);
    }

    return source;
  }

}
