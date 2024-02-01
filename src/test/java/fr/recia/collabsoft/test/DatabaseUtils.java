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

import fr.recia.collabsoft.db.entity.AssociatedApp;
import fr.recia.collabsoft.db.entity.Collaboration;
import fr.recia.collabsoft.db.entity.File;
import fr.recia.collabsoft.db.entity.FileHistory;
import fr.recia.collabsoft.db.entity.Metadata;
import fr.recia.collabsoft.db.entity.User;
import fr.recia.collabsoft.db.enums.Role;
import fr.recia.collabsoft.db.repository.AssociatedAppRepository;
import fr.recia.collabsoft.db.repository.CollaborationRepository;
import fr.recia.collabsoft.db.repository.FileHistoryRepository;
import fr.recia.collabsoft.db.repository.FileRepository;
import fr.recia.collabsoft.db.repository.MetadataRepository;
import fr.recia.collabsoft.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DatabaseUtils {

  /*
   * User 1 :
   *  - id : user1Id
   *  - sub : user1Sub
   *  - files [ file1Id, file2Id ]
   *  - starred [ file1Id, file3Id ]
   *  - shared [ file2Id ]
   *  - collaborations [ file3Id ]
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
   *  - collaborations [ file2Id ]
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
   *  - collaborations [  ]
   *  - public [ ]
   *  - histories [ historyUnknownId ]
   *  - filesWithHistories [ ]
   */

  // Users

  public static final String user1Sub = "User1";
  public static final String user2Sub = "User2";
  public static final String user3Sub = "User3";
  public static final String user4Sub = "User4";
  public static final String userGuestSub = "guest";

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

  // Histories

  public static final Long history1Id = 1L;
  public static final Long history2Id = 2L;
  public static final Long history3Id = 3L;
  public static final Long history4Id = 4L;
  public static final Long historyUnknownId = 999L;

  // Associated App

  public static final Long associatedApp1Id = 1L;
  public static final Long associatedAppUnknownId = 999L;

  @Autowired
  private AssociatedAppRepository<AssociatedApp> associatedAppRepository;
  @Autowired
  private CollaborationRepository<Collaboration> collaborationRepository;
  @Autowired
  private FileHistoryRepository<FileHistory> fileHistoryRepository;
  @Autowired
  private FileRepository<File> fileRepository;
  @Autowired
  private MetadataRepository<Metadata> metadataRepository;
  @Autowired
  private UserRepository<User> userRepository;

  public void initDatabase() {
    final AssociatedApp associatedApp1 = new AssociatedApp(
      associatedApp1Id, "tldraw", "tldraw", null, null, "tldr", "json"
    );
    associatedAppRepository.saveAndFlush(associatedApp1);

    final User user1 = new User(user1Id, user1Sub);
    final User user2 = new User(user2Id, user3Sub);
    final User user3 = new User(user3Id, user3Sub);
    userRepository.saveAllAndFlush(List.of(user1, user2, user3));

    final File file1 = new File(
      file1Id, "file1", null, "".getBytes(), user1, null, user1, null,
      associatedApp1, false
    );
    final File file2 = new File(
      file2Id, "file2", null, "".getBytes(), user1, null, user1, null,
      associatedApp1, false
    );
    final File file3 = new File(
      file3Id, "file3", null, "".getBytes(), user2, null, user2, null,
      associatedApp1, false
    );
    final File file4 = new File(
      file4Id, "file4", null, "".getBytes(), user2, null, user2, null,
      associatedApp1, true
    );
    final File file5 = new File(
      file5Id, "file5", null, "".getBytes(), user3, null, user3, null,
      associatedApp1, false
    );
    final File file6 = new File(
      file6Id, "file6", null, "".getBytes(), user3, null, user3, null,
      associatedApp1, false
    );
    fileRepository.saveAllAndFlush(List.of(file1, file2, file3, file4, file5, file6));

    final FileHistory history1 = new FileHistory(history1Id, file5, "".getBytes(), null);
    final FileHistory history2 = new FileHistory(history2Id, file5, "".getBytes(), null);
    final FileHistory history3 = new FileHistory(history3Id, file2, "".getBytes(), null);
    final FileHistory history4 = new FileHistory(history4Id, file4, "".getBytes(), null);
    fileHistoryRepository.saveAllAndFlush(List.of(history1, history2, history3, history4));

    final Metadata metadata1 = new Metadata(user1, file1, true);
    final Metadata metadata2 = new Metadata(user1, file3, true);
    metadataRepository.saveAllAndFlush(List.of(metadata1, metadata2));

    final Collaboration collaboration1 = new Collaboration(user2, file2, Role.EDITOR);
    final Collaboration collaboration2 = new Collaboration(user1, file2, Role.EDITOR);
    collaborationRepository.saveAllAndFlush(List.of(collaboration1, collaboration2));
  }

}
