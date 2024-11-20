package org.sluja.aicourse.S01.task6.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageProcessingService {
    
    private final OpenAiChatModel chatModel;
    private final GetAvailableFilesService filesService;
    private static final String PROMPT_PATH = "prompts/city_image_processing.md";
    private static final String IMAGES_PATH = "files/task6/";
    private static final String RESOURCES_PATH = "classpath:files/task6";

    public String processCityImages() {
        try {
            // Read system prompt
            String systemPrompt = Files.readString(new ClassPathResource(PROMPT_PATH).getFile().toPath());

            List<Media> imageMediaList = new ArrayList<>();
            for (String filename : filesService.getAvailableFiles(RESOURCES_PATH)) {
                final Resource imageContent = new ClassPathResource(IMAGES_PATH + filename);
                imageMediaList.add(new Media(MimeTypeUtils.IMAGE_PNG, imageContent));
            }

            List<Message> messages = List.of(
                new SystemMessage(systemPrompt),
                new UserMessage("Analyze these city plan images according to the provided instructions.", imageMediaList)
            );

            return chatModel.call(new Prompt(messages))
                    .getResult()
                    .getOutput()
                    .getContent();
                    
        } catch (Exception e) {
            log.error("Failed to process city images", e);
            throw new RuntimeException("Failed to process city images: " + e.getMessage(), e);
        }
    }
} 