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
package fr.recia.collabsoft.test;

public class TestConstants {

  /*
   * User 1 :
   *  - id : user1Id
   *  - sub : user1Sub
   *  - files [ file1Id, file2Id ]
   *  - starred [ file1Id, file3Id ]
   *  - shared [ file2Id ]
   *  - collaborations [ collaboration2Id ]
   *  - public [ ]
   *  - histories [ history3Id ]
   *  - filesWithHistories [ file2Id ]
   *
   * User 2 :
   *  - id : user2Id
   *  - sub : user2Sub
   *  - files [ file3Id, file4Id ]
   *  - starred [  ]
   *  - shared [ file3Id ]
   *  - collaborations [ collaboration1Id ]
   *  - public [ file4Id ]
   *  - histories [ history4Id ]
   *  - filesWithHistories [ file4Id ]
   *
   * User 3 :
   *  - id : user3Id
   *  - sub : user3Sub
   *  - files [ file5Id, file6Id ]
   *  - starred [  ]
   *  - shared [  ]
   *  - collaborations [  ]
   *  - public [ ]
   *  - histories [ history1Id, history2Id ]
   *  - filesWithHistories [ file5Id ]
   *
   * User Unknown :
   *  - id : userUnknownId
   *  - sub : userUnknownSub
   *  - files [ fileUnknownId ]
   *  - starred [  ]
   *  - shared [  ]
   *  - collaborations [ cuId ]
   *  - public [ ]
   *  - histories [ historyUnknownId ]
   *  - filesWithHistories [ ]
   */

  // Soffit subs

  public static final String user1Sub = "User1";
  public static final String user2Sub = "User2";
  public static final String user3Sub = "User3";
  public static final String user4Sub = "User4";
  public static final String userGuestSub = "guest";

  // Users

  public static final Long user1Id = 1L;
  public static final Long user2Id = 2L;
  public static final Long user3Id = 3L;
  public static final Long userUnknownId = 999L;

  // Files

  public static final Long file1Id = 1L;
  public static final Long file2Id = 2L;
  public static final Long file3Id = 3L;
  public static final Long file4Id = 4L;
  public static final Long file5Id = 5L;
  public static final Long file6Id = 6L;
  public static final Long fileUnknownId = 999L;

  // Collaborations

  public static final Long collaboration1Id = 1L;
  public static final Long collaboration2Id = 2L;
  public static final Long collaborationuId = 999L;

  // Histories

  public static final Long history1Id = 1L;
  public static final Long history2Id = 2L;
  public static final Long history3Id = 3L;
  public static final Long history4Id = 4L;
  public static final Long historyUnknownId = 999L;

  // Associated App

  public static final Long associatedApp1Id = 1L;
  public static final Long associatedAppUnknownId = 999L;

}
