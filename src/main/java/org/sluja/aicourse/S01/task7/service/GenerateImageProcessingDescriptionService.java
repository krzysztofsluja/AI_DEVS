package org.sluja.aicourse.S01.task7.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.sluja.aicourse.S01.task4.service.ExternalFileDownloadService;
import org.sluja.aicourse.S01.task7.dtos.RobotDescriptionJSONData;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenerateImageProcessingDescriptionService {

    private final ExternalFileDownloadService externalFileDownloadService;
    private final OpenAiChatModel openAiChatModel;
    private final ResourceLoader resourceLoader;
    private final OpenAiImageModel imageModel;

    @Value("${task7.robot.description.url}")
    private String ROBOT_DESCRIPTION_URL;
    private static final String IMAGE_GENERATION_PROMPT_PATH = "prompts/image_generation_prompt.md";


    private String loadSystemPrompt(final String promptPath) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + promptPath);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load system prompt from " + promptPath, e);
        }
    }

    public String generateImageAndGetUrl() {
        final String descriptionPrompt = generateDescriptionPrompt();
        return imageModel.call(new ImagePrompt(
                descriptionPrompt,
                OpenAiImageOptions.builder()
                        .withHeight(1024)
                        .withWidth(1024)
                        .withQuality("hd")
                        .withN(1)
                        .build()
        )).getResult().getOutput().getUrl();
    }

    private String generateDescriptionPrompt() {
        final String systemPrompt = loadSystemPrompt(IMAGE_GENERATION_PROMPT_PATH);
        final String description = getDescription().description();
        return openAiChatModel.call(new Prompt(List.of(new SystemMessage(systemPrompt), new UserMessage(description))))
                .getResult().getOutput().getContent();
    }
    private RobotDescriptionJSONData getDescription() {
        return externalFileDownloadService.downloadJsonContent(ROBOT_DESCRIPTION_URL, RobotDescriptionJSONData.class);
    }
} 