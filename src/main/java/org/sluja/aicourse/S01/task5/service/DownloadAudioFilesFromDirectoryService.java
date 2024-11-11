package org.sluja.aicourse.S01.task5.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class DownloadAudioFilesFromDirectoryService {

    private final ResourceLoader resourceLoader;

    public List<String> getAudioFilePaths() {
        try {
            Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                    .getResources("classpath:files/*.*");

            return Arrays.stream(resources)
                    .map(this::getPathFromResource)
                    .filter(Objects::nonNull)
                    .filter(this::isAudioFile)
                    .peek(path -> log.info("Found audio file: {}", path))
                    .toList();

        } catch (IOException e) {
            log.error("Failed to read resources", e);
            throw new RuntimeException("Failed to read resources: " + e.getMessage());
        }
    }

    private String getPathFromResource(Resource resource) {
        try {
            return resource.getURI().getPath();
        } catch (IOException e) {
            log.error("Failed to get path from resource: {}", resource, e);
            return null;
        }
    }

    private boolean isAudioFile(String path) {
        String fileName = path.toLowerCase();
        return fileName.endsWith(".m4a") || 
               fileName.endsWith(".mp3") || 
               fileName.endsWith(".wav");
    }
} 