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

import lombok.extern.slf4j.Slf4j;
import org.apereo.portal.soffit.security.SoffitApiAuthenticationManager;
import org.apereo.portal.soffit.security.SoffitApiPreAuthenticatedProcessingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  private final CollabsoftProperties collabsoftProperties;

  public SecurityConfiguration(CollabsoftProperties collabsoftProperties) {
    this.collabsoftProperties = collabsoftProperties;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return new SoffitApiAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    final AbstractPreAuthenticatedProcessingFilter filter = new SoffitApiPreAuthenticatedProcessingFilter(
      collabsoftProperties.getSoffit().getJwtSignatureKey()
    );
    filter.setAuthenticationManager(authenticationManager());
    http.addFilter(filter);

    http
      .csrf()
      .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

    http.authorizeHttpRequests(authz -> authz
      .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
      .antMatchers("/health-check", "/api/config", "/ui/**").permitAll()
      .antMatchers("/api/**").authenticated()
      .anyRequest().denyAll()
    );

    http.sessionManagement().sessionFixation().newSession();

    return http.build();
  }

}
