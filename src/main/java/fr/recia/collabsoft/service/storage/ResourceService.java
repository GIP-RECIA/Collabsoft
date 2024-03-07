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
package fr.recia.collabsoft.service.storage;

import com.google.common.io.Files;
import fr.recia.collabsoft.configuration.CollabsoftProperties;
import fr.recia.collabsoft.configuration.bean.StorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class ResourceService {

  private final StorageProperties storageProperties;

  public ResourceService(CollabsoftProperties collabsoftProperties) {
    this.storageProperties = collabsoftProperties.getStorage();
  }

  private String getFolderPath(Long fileId) {
    return storageProperties.getLocation() + File.separator + fileId;
  }

  private String getFilePath(Long fileId, String resourceName) {
    return getFolderPath(fileId) + File.separator + resourceName;
  }

  public Resource getResource(Long fileId, String resourceName) {
    return new PathResource(getFilePath(fileId, resourceName));
  }

  public String saveResource(Long fileId, MultipartFile file, String name) {
    if (file.isEmpty()) throw new RuntimeException("File can not be empty");
    try {
      final String fileExt = Files.getFileExtension(file.getOriginalFilename()).toLowerCase();
      String fileName = name != null && !name.trim().isEmpty() ? name : new SimpleDateFormat("yyyyMMddHHmmss'." + fileExt + "'").format(new Date());
      File inFile = new File(getFilePath(fileId, fileName));
      if (!inFile.getParentFile().exists()) {
        boolean error = !inFile.getParentFile().mkdirs();
        if (error) {
          log.error("Can't create directory {} to upload file, track error!", inFile.getParentFile().getPath());
          return null;
        }
      }

      file.transferTo(inFile);

      return fileName;
    } catch (IOException e) {
      return null;
    }
  }

  public boolean deleteResource(Long fileId, String resourceName) {
    final String path = getFilePath(fileId, resourceName);
    File file = new File(path);

    if (file.exists()) {
      if (!file.delete()) {
        log.error("Tried to delete the file {} failed, track errors!", path);
        return false;
      } else return true;
    }
    log.error("Tried to delete the file {} but it doesn't exist, track errors!", path);
    return false;
  }

  public boolean deleteResources(Long fileId) {
    final String path = getFolderPath(fileId);
    File file = new File(path);

    if (file.exists()) {
      try {
        FileUtils.deleteDirectory(file);
        return true;
      } catch (IOException e) {
        log.error("Tried to delete the folder {} failed, track errors!", path);
        return false;
      }
    }
    log.error("Tried to delete the folder {} but it doesn't exist, track errors!", path);
    return false;
  }

}
