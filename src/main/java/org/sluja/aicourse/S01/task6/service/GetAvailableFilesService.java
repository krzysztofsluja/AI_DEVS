package org.sluja.aicourse.S01.task6.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GetAvailableFilesService {

    private static final String RESOURCES_PATH = "classpath:files/task6";

    public List<String> getAvailableFiles() {
        try {
            File directory = ResourceUtils.getFile(RESOURCES_PATH);
            
            if (!directory.exists() || !directory.isDirectory()) {
                log.warn("Directory {} does not exist or is not a directory", RESOURCES_PATH);
                return Collections.emptyList();
            }

            return Optional.ofNullable(directory.list())
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList());
                    
        } catch (FileNotFoundException e) {
            log.error("Failed to access directory: {}", RESOURCES_PATH, e);
            return Collections.emptyList();
        }
    }
} 