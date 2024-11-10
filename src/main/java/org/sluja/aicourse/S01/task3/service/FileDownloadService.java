package org.sluja.aicourse.S01.task3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileDownloadService {

    private final String documentAddress;
    private final Path resourcesPath = Paths.get("src/main/resources/downloaded_document.txt");

    public FileDownloadService(@Value("${task3.doc.address}") String documentAddress) {
        this.documentAddress = documentAddress;
    }

    public void downloadIfNotExists() {
        if (!isFileDownloaded()) {
            downloadAndSaveFile();
        }
    }

    private boolean isFileDownloaded() {
        return Files.exists(resourcesPath);
    }

    private void downloadAndSaveFile() {
        try {
            final URL url = new URL(documentAddress);
            
            try (InputStream in = url.openStream()) {
                Files.copy(in, resourcesPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file from: " + documentAddress, e);
        }
    }
} 