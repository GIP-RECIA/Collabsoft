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
package fr.recia.collabsoft.services;

import fr.recia.collabsoft.db.entities.File;
import fr.recia.collabsoft.db.entities.FileHistory;
import fr.recia.collabsoft.db.entities.QFileHistory;
import fr.recia.collabsoft.db.repositories.FileHistoryRepository;
import fr.recia.collabsoft.db.repositories.FileRepository;
import fr.recia.collabsoft.pojo.JsonHistoryBody;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class FileHistoryService {

  @Inject
  private FileHistoryRepository<FileHistory> fileHistoryRepository;
  @Inject
  private FileRepository<File> fileRepository;

  @Inject
  private FileService fileService;

  public List<FileHistory> getHistories(Long fileId) {
    return IteratorUtils.toList(
      fileHistoryRepository.findAll(QFileHistory.fileHistory.file.id.eq(fileId)).iterator()
    );
  }

  public boolean createHistory(Long fileId, JsonHistoryBody body) {
    final File file = fileService.getFile(fileId);
    if (file == null) return false;
    FileHistory fileHistory = new FileHistory();
    fileHistory.setFile(file);
    fileHistory.setBlob(body.getBlob());
    fileHistoryRepository.saveAndFlush(fileHistory);

    return true;
  }

  public FileHistory getHistory(Long fileId, Long historyId) {
    return fileHistoryRepository.findOne(
      QFileHistory.fileHistory.file.id.eq(fileId).and(QFileHistory.fileHistory.id.eq(historyId))
    ).orElse(null);
  }

  public boolean deleteHistory(Long fileId, Long historyId) {
    final FileHistory fileHistory = getHistory(fileId, historyId);
    if (fileHistory == null) return false;
    fileHistoryRepository.delete(fileHistory);

    return true;
  }

  public boolean revertHistory(Long fileId, Long historyId) {
    final FileHistory fileHistory = getHistory(fileId, historyId);
    if (fileHistory == null) return false;

    // create new file history
    FileHistory newFileHistory = new FileHistory();
    newFileHistory.setFile(fileHistory.getFile());
    newFileHistory.setBlob(fileHistory.getFile().getBlob().getBytes());
    fileHistoryRepository.saveAndFlush(newFileHistory);

    // update file blob with history
    File file = fileHistory.getFile();
    file.setBlob(fileHistory.getBlob().getBytes());
    fileRepository.saveAndFlush(file);

    return true;
  }

  public boolean deleteHistories(Long fileId) {
    final List<FileHistory> fileHistories = getHistories(fileId);
    if (fileHistories.isEmpty()) return false;
    fileHistoryRepository.deleteAll(fileHistories);

    return true;
  }

}
