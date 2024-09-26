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

package fr.recia.collabsoft.configuration.bean;

import lombok.Data;

import java.util.Map;

@Data
public class FrontProperties {

  private String appName;
  private String userInfoApiUrl;
  private String nextcloudUri;
  private CollaborationProperties collaboration;
  private String templateApiPath;
  private ExtendedUportalProperties extendedUportal;

  @Data
  public static class CollaborationProperties {

    private String websocketApiUrl;

    @Override
    public String toString() {
      return "{" +
        "\n\t\t\"websocketApiUrl\": \"" + websocketApiUrl + "\"" +
        "\n\t}";
    }

  }

  @Data
  public static class ExtendedUportalProperties {

    private ComponentProperties header;
    private ComponentProperties footer;

    @Override
    public String toString() {
      return "{" +
        "\n\t\t\"header\": " + header + "," +
        "\n\t\t\"footer\": " + footer +
        "\n\t}";
    }

    @Data
    public static class ComponentProperties {

      private String componentPath;
      private Map<String, String> props;

      @Override
      public String toString() {
        return "{" +
          "\n\t\t\t\"componentPath\": \"" + componentPath + "\"," +
          "\n\t\t\t\"props\": " + props +
          "\n\t\t}";
      }

    }

  }

  @Override
  public String toString() {
    return "\"FrontProperties\": {" +
      "\n\t\"appName\": \"" + appName + "\"," +
      "\n\t\"userInfoApiUrl\": \"" + userInfoApiUrl + "\"," +
      "\n\t\"nextcloudUri\": \"" + nextcloudUri + "\"," +
      "\n\t\"collaboration\": " + collaboration + "," +
      "\n\t\"templateApiPath\": \"" + templateApiPath + "\"," +
      "\n\t\"extendedUportal\": " + extendedUportal +
      "\n}";
  }

}
