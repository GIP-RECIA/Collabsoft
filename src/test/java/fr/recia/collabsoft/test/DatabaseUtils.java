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
import fr.recia.collabsoft.db.entity.QAssociatedApp;
import fr.recia.collabsoft.db.entity.QFile;
import fr.recia.collabsoft.db.entity.QFileHistory;
import fr.recia.collabsoft.db.entity.QUser;
import fr.recia.collabsoft.db.entity.User;
import fr.recia.collabsoft.db.enums.Role;
import fr.recia.collabsoft.db.repository.AssociatedAppRepository;
import fr.recia.collabsoft.db.repository.CollaborationRepository;
import fr.recia.collabsoft.db.repository.FileHistoryRepository;
import fr.recia.collabsoft.db.repository.FileRepository;
import fr.recia.collabsoft.db.repository.MetadataRepository;
import fr.recia.collabsoft.db.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
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

  public static final Long userUnknownId = 999L;

  // Files
  public static final Long fileUnknownId = 999L;

  // Histories
  public static final Long historyUnknownId = 999L;

  // Associated App
  public static final Long associatedAppUnknownId = 999L;

  public enum DataKey {
    USER,
    FILE,
    HISTORY,
    ASSOCIATED_APP
  }

  @Data
  public static class DataToId {

    // Users
    private Long user1Id;
    private Long user2Id;
    private Long user3Id;

    // Files
    private Long file1Id;
    private Long file2Id;
    private Long file3Id;
    private Long file4Id;
    private Long file5Id;
    private Long file6Id;

    // Histories
    private Long history1Id;
    private Long history2Id;
    private Long history3Id;
    private Long history4Id;

    // Associated App
    private Long associatedApp1Id;

    public DataToId(Map<DataKey, List<Long>> data) {
      this.user1Id = data.get(DataKey.USER).get(0);
      this.user2Id = data.get(DataKey.USER).get(1);
      this.user3Id = data.get(DataKey.USER).get(2);
      this.file1Id = data.get(DataKey.FILE).get(0);
      this.file2Id = data.get(DataKey.FILE).get(1);
      this.file3Id = data.get(DataKey.FILE).get(2);
      this.file4Id = data.get(DataKey.FILE).get(3);
      this.file5Id = data.get(DataKey.FILE).get(4);
      this.file6Id = data.get(DataKey.FILE).get(5);
      this.history1Id = data.get(DataKey.HISTORY).get(0);
      this.history2Id = data.get(DataKey.HISTORY).get(1);
      this.history3Id = data.get(DataKey.HISTORY).get(2);
      this.history4Id = data.get(DataKey.HISTORY).get(3);
      this.associatedApp1Id = data.get(DataKey.ASSOCIATED_APP).get(0);
    }

  }

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

  public DataToId insertData() {
    Map<DataKey, List<Long>> data = new HashMap<>();

    AssociatedApp associatedApp1 = new AssociatedApp();
    associatedApp1.setEnabled(true);
    associatedApp1.setSlug("tldraw");
    associatedApp1.setExtension("tldr");
    associatedApp1.setType("json");
    associatedAppRepository.saveAndFlush(associatedApp1);
    AssociatedApp aa1 = associatedAppRepository.findOne(QAssociatedApp.associatedApp.slug.eq("tldraw")).orElseThrow();
    data.put(DataKey.ASSOCIATED_APP, List.of(aa1.getId()));

    User user1 = new User();
    user1.setCasUid(user1Sub);
    User user2 = new User();
    user2.setCasUid(user2Sub);
    User user3 = new User();
    user3.setCasUid(user3Sub);
    userRepository.saveAllAndFlush(List.of(user1, user2, user3));
    User u1 = userRepository.findOne(QUser.user.casUid.eq(user1Sub)).orElseThrow();
    User u2 = userRepository.findOne(QUser.user.casUid.eq(user2Sub)).orElseThrow();
    User u3 = userRepository.findOne(QUser.user.casUid.eq(user3Sub)).orElseThrow();
    data.put(DataKey.USER, List.of(u1.getId(), u2.getId(), u3.getId()));

    File file1 = new File();
    file1.setUuid(UUID.randomUUID().toString());
    file1.setTitle("file1");
    file1.setDescription(null);
    file1.setData("");
    file1.setCreator(u1);
    file1.setLastEditor(u1);
    file1.setAssociatedApp(aa1);
    file1.setPub(false);
    File file2 = new File();
    file2.setUuid(UUID.randomUUID().toString());
    file2.setTitle("file2");
    file2.setDescription(null);
    file2.setData("");
    file2.setCreator(u1);
    file2.setLastEditor(u1);
    file2.setAssociatedApp(aa1);
    file2.setPub(false);
    File file3 = new File();
    file3.setUuid(UUID.randomUUID().toString());
    file3.setTitle("file3");
    file3.setDescription(null);
    file3.setData("");
    file3.setCreator(u2);
    file3.setLastEditor(u2);
    file3.setAssociatedApp(aa1);
    file3.setPub(false);
    File file4 = new File();
    file4.setUuid(UUID.randomUUID().toString());
    file4.setTitle("file4");
    file4.setDescription(null);
    file4.setData("");
    file4.setCreator(u2);
    file4.setLastEditor(u2);
    file4.setAssociatedApp(aa1);
    file4.setPub(true);
    File file5 = new File();
    file5.setUuid(UUID.randomUUID().toString());
    file5.setTitle("file5");
    file5.setDescription(null);
    file5.setData("");
    file5.setCreator(u3);
    file5.setLastEditor(u3);
    file5.setAssociatedApp(aa1);
    file5.setPub(false);
    File file6 = new File();
    file6.setUuid(UUID.randomUUID().toString());
    file6.setTitle("file6");
    file6.setDescription(null);
    file6.setData("");
    file6.setCreator(u3);
    file6.setLastEditor(u3);
    file6.setAssociatedApp(aa1);
    file6.setPub(false);
    fileRepository.saveAllAndFlush(List.of(file1, file2, file3, file4, file5, file6));
    File f1 = fileRepository.findOne(QFile.file.title.eq("file1")).orElseThrow();
    File f2 = fileRepository.findOne(QFile.file.title.eq("file2")).orElseThrow();
    File f3 = fileRepository.findOne(QFile.file.title.eq("file3")).orElseThrow();
    File f4 = fileRepository.findOne(QFile.file.title.eq("file4")).orElseThrow();
    File f5 = fileRepository.findOne(QFile.file.title.eq("file5")).orElseThrow();
    File f6 = fileRepository.findOne(QFile.file.title.eq("file6")).orElseThrow();
    data.put(DataKey.FILE, List.of(f1.getId(), f2.getId(), f3.getId(), f4.getId(), f5.getId(), f6.getId()));

    FileHistory history1 = new FileHistory();
    history1.setFile(f5);
    history1.setData("1");
    FileHistory history2 = new FileHistory();
    history2.setFile(f5);
    history2.setData("2");
    FileHistory history3 = new FileHistory();
    history3.setFile(f2);
    history3.setData("3");
    FileHistory history4 = new FileHistory();
    history4.setFile(f4);
    history4.setData("4");
    fileHistoryRepository.saveAndFlush(history1);
    fileHistoryRepository.saveAndFlush(history2);
    fileHistoryRepository.saveAndFlush(history3);
    fileHistoryRepository.saveAndFlush(history4);
    FileHistory h1 = fileHistoryRepository.findOne(QFileHistory.fileHistory.data.eq("1")).orElseThrow();
    FileHistory h2 = fileHistoryRepository.findOne(QFileHistory.fileHistory.data.eq("2")).orElseThrow();
    FileHistory h3 = fileHistoryRepository.findOne(QFileHistory.fileHistory.data.eq("3")).orElseThrow();
    FileHistory h4 = fileHistoryRepository.findOne(QFileHistory.fileHistory.data.eq("4")).orElseThrow();
    data.put(DataKey.HISTORY, List.of(h1.getKey().getId(), h2.getKey().getId(), h3.getKey().getId(), h4.getKey().getId()));

    Metadata metadata1 = new Metadata();
    metadata1.setUser(u1);
    metadata1.setFile(f1);
    metadata1.setStarred(true);
    Metadata metadata2 = new Metadata();
    metadata2.setUser(u1);
    metadata2.setFile(f3);
    metadata2.setStarred(true);
    metadataRepository.saveAllAndFlush(List.of(metadata1, metadata2));

    Collaboration collaboration1 = new Collaboration();
    collaboration1.setUser(u2);
    collaboration1.setFile(f2);
    collaboration1.setRole(Role.EDITOR);
    Collaboration collaboration2 = new Collaboration();
    collaboration2.setUser(u1);
    collaboration2.setFile(f3);
    collaboration2.setRole(Role.EDITOR);
    collaborationRepository.saveAllAndFlush(List.of(collaboration1, collaboration2));

    return new DataToId(data);
  }

  public void deleteData() {
    metadataRepository.deleteAll();
    metadataRepository.flush();

    collaborationRepository.deleteAll();
    collaborationRepository.flush();

    fileHistoryRepository.deleteAll();
    fileHistoryRepository.flush();

    fileRepository.deleteAll();
    fileRepository.flush();

    associatedAppRepository.deleteAll();
    associatedAppRepository.flush();

    userRepository.deleteAll();
    userRepository.flush();
  }

}
