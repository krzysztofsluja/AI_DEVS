package org.sluja.aicourse.S01.task6_1.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class DownloadImageService {

    private static final String IMAGE_PATH = "files/task6_1/image.png";

    public Resource downloadImage() {
        try {
            Resource resource = new ClassPathResource(IMAGE_PATH);
            if (!resource.exists()) {
                throw new RuntimeException("Image file not found at: " + IMAGE_PATH);
            }
            return resource;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load image: " + e.getMessage(), e);
        }
    }
}
