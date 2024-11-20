package org.sluja.aicourse.newClasses.utils.files;

import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component
public class GetLocalFilesService {

    public Map<String, String> getFilesWithTitle(final String path) {
        final Map<String, String> filesWithContent = new HashMap<>();
        try {
            File directory = new File(path);
            if (!directory.exists() || !directory.isDirectory()) {
                throw new IllegalArgumentException("Invalid directory path: " + path);
            }

            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        String fileName = file.getName();
                        Path filePath = Paths.get(file.getAbsolutePath());
                        String content = Files.readString(filePath);
                        filesWithContent.put(fileName, content);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading files from directory: " + e.getMessage(), e);
        }

        return filesWithContent;
    }
}
