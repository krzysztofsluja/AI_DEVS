package org.sluja.aicourse.S01.task6_1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageAnalysisService {
    
    private final OpenAiChatModel VLMChatModel;
    private final DownloadImageService downloadImageService;
    
    private static final String SYSTEM_PROMPT = """
            You have the perfect view and the ability to analyze the image.
            Your task is answering the question of the user based on the information from conversation shown on the image.
            """;

    public String analyzeImage() {
        try {
            Resource imageResource = downloadImageService.downloadImage();
            if (imageResource == null) {
                return "No image found to analyze";
            }

            Media imageMedia = new Media(MimeTypeUtils.IMAGE_PNG, imageResource);
            
            List<Message> messages = List.of(
                new SystemMessage(SYSTEM_PROMPT),
                new UserMessage("Where will the girls go tonight?", List.of(imageMedia))
            );

            return VLMChatModel.call(new Prompt(messages))
                    .getResult()
                    .getOutput()
                    .getContent();
                    
        } catch (Exception e) {
            log.error("Failed to analyze image", e);
            throw new RuntimeException("Failed to analyze image: " + e.getMessage(), e);
        }
    }
} 