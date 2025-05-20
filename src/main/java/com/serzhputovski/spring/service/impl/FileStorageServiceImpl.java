package com.serzhputovski.spring.service.impl;

import com.serzhputovski.spring.config.StorageConfig;
import com.serzhputovski.spring.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path rootLocation;
    private static final Logger log = LogManager.getLogger(FileStorageServiceImpl.class);

    public FileStorageServiceImpl(StorageConfig storageConfig) {
        this.rootLocation = Paths.get(storageConfig.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(rootLocation);
            log.info("Created upload directory at {}", rootLocation);
        } catch (Exception e) {
            log.error("Could not create upload directory {}", rootLocation, e);
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String filename = UUID.randomUUID() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        log.debug("Storing file {} as {}", file.getOriginalFilename(), filename);
        try {
            Files.copy(file.getInputStream(), rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            log.info("File stored successfully: {}", filename);
            return filename;
        } catch (Exception e) {
            log.error("Failed to store file {}", filename, e);
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        log.debug("Loading resource {}", filename);
        try {
            Path file = rootLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                log.info("Resource {} loaded successfully", filename);
                return resource;
            }
            log.warn("Resource {} not found or not readable", filename);
            throw new RuntimeException("File not found " + filename);
        } catch (MalformedURLException e) {
            log.error("Malformed URL for file {}", filename, e);
            throw new RuntimeException("File not found " + filename, e);
        }
    }
}
