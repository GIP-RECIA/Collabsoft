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

import fr.recia.collabsoft.db.entity.File;
import fr.recia.collabsoft.db.entity.FileHistory;
import fr.recia.collabsoft.db.entity.QFileHistory;
import fr.recia.collabsoft.db.repository.FileHistoryRepository;
import fr.recia.collabsoft.db.repository.FileRepository;
import fr.recia.collabsoft.model.enums.Authority;
import fr.recia.collabsoft.model.pojo.JsonHistoryBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FileHistoryService {

  @Autowired
  private FileHistoryRepository<FileHistory> fileHistoryRepository;
  @Autowired
  private FileRepository<File> fileRepository;

  @Autowired
  private FileService fileService;

  public List<FileHistory> getHistories(Long fileId) {
    final List<FileHistory> histories = IteratorUtils.toList(
      fileHistoryRepository.findAll(QFileHistory.fileHistory.file.id.eq(fileId)).iterator()
    );

    if (histories.isEmpty()) log.debug("No histories found for file with id \"{}\"", fileId);

    return histories;
  }

  public boolean createHistory(Long fileId, JsonHistoryBody body) {
    final File file = fileService.getFile(fileId, Authority.OWNER);
    if (file == null) return false;
    FileHistory fileHistory = new FileHistory();
    fileHistory.setFile(file);
    fileHistory.setData(body.getData());
    fileHistoryRepository.saveAndFlush(fileHistory);

    return true;
  }

  public FileHistory getHistory(Long fileId, Long historyId) {
    final FileHistory history = fileHistoryRepository.findOne(
      QFileHistory.fileHistory.file.id.eq(fileId).and(QFileHistory.fileHistory.key.id.eq(historyId))
    ).orElse(null);

    if (history == null)
      log.debug("No history found for file with id \"{}\" and history with id \"{}\"", fileId, historyId);

    return history;
  }

  public boolean deleteHistory(Long fileId, Long historyId) {
    final File file = fileService.getFile(fileId, Authority.OWNER);
    if (file == null) return false;
    final FileHistory fileHistory = getHistory(fileId, historyId);
    if (fileHistory == null) return false;
    fileHistoryRepository.delete(fileHistory);

    return true;
  }

  public boolean revertHistory(Long fileId, Long historyId) {
    File file = fileService.getFile(fileId, Authority.OWNER);
    if (file == null) return false;
    final FileHistory fileHistory = getHistory(fileId, historyId);
    if (fileHistory == null) return false;

    // create new file history
    FileHistory newFileHistory = new FileHistory();
    newFileHistory.setFile(fileHistory.getFile());
    newFileHistory.setData(fileHistory.getFile().getData());
    fileHistoryRepository.saveAndFlush(newFileHistory);

    // update file blob with history
    file.setData(fileHistory.getData());
    fileRepository.saveAndFlush(file);

    return true;
  }

  public boolean deleteHistories(Long fileId) {
    final File file = fileService.getFile(fileId, Authority.OWNER);
    if (file == null) return false;
    final List<FileHistory> fileHistories = getHistories(fileId);
    if (fileHistories.isEmpty()) return false;
    fileHistoryRepository.deleteAll(fileHistories);

    return true;
  }

}
