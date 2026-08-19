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

import fr.recia.notifications.soffit_java_client.SoffitJwtAuthenticationFilter;
import fr.recia.notifications.soffit_java_client.SoffitJwtValidator;
import lombok.extern.slf4j.Slf4j;
import org.apereo.portal.soffit.security.SoffitApiAuthenticationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  private final CollabsoftProperties collabsoftProperties;

  public SecurityConfiguration(CollabsoftProperties collabsoftProperties) {
    this.collabsoftProperties = collabsoftProperties;
  }


  @Bean
  SoffitJwtValidator soffitJwtValidator() {
    return new SoffitJwtValidator(collabsoftProperties.getSoffit().getJwtSignatureKey());
  }

  @Bean
  SoffitJwtAuthenticationFilter soffitJwtAuthenticationFilter(SoffitJwtValidator validator) {
    return new SoffitJwtAuthenticationFilter(validator);
  }


  @Bean
  public AuthenticationManager authenticationManager() {
    return new SoffitApiAuthenticationManager();
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http, SoffitJwtAuthenticationFilter filter) {

    CsrfTokenRequestAttributeHandler csrfTokenRequestHandler =
      new CsrfTokenRequestAttributeHandler();

    http.csrf(csrf -> csrf
      .csrfTokenRepository(
        CookieCsrfTokenRepository.withHttpOnlyFalse())
      .csrfTokenRequestHandler(csrfTokenRequestHandler)
    );

    http.authorizeHttpRequests(authz -> authz
      .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
      .requestMatchers("/health-check", "/api/config", "/ui/**", "/dist/**").permitAll()
      .requestMatchers("/api/**").authenticated()
      .anyRequest().denyAll()
    );

   http .sessionManagement(session -> session
      .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
      .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::newSession // retester avec new session ?
      )
    ).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);


    return http.build();
  }

}
