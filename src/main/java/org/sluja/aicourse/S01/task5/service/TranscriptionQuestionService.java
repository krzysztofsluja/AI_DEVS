package org.sluja.aicourse.S01.task5.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranscriptionQuestionService {

    private final ResourceLoader resourceLoader;
    private static final String TRANSCRIPTIONS_PATH = "classpath:transcriptions/*.txt";

    private final OpenAiChatModel chatModel;

    public String getAnswer() {
        String transcription = combineTranscriptions();
        String systemPrompt = loadSystemPrompt("prompts/transcription_prompt.md");
        systemPrompt = systemPrompt.replace("{transcription}", transcription);
        return generateResponseWithExistingSystemPrompt("", systemPrompt);
    }

    private String combineTranscriptions() {
        try {
            Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                    .getResources(TRANSCRIPTIONS_PATH);

            return Arrays.stream(resources)
                    .map(this::readAndFormatTranscription)
                    .filter(content -> !content.isEmpty())
                    .collect(Collectors.joining("\n"));

        } catch (IOException e) {
            log.error("Failed to read transcription files", e);
            throw new RuntimeException("Failed to combine transcriptions: " + e.getMessage());
        }
    }

    public String generateResponse(final String prompt, final String systemPromptPath) {
        System.out.println("USER MESS: " + prompt);
        final String systemPromptContent = loadSystemPrompt(systemPromptPath);
        final UserMessage userMessage = new UserMessage(prompt);
        final SystemMessage systemMessage = new SystemMessage(systemPromptContent);
        return generateResponse(List.of(systemMessage, userMessage));
    }

    public String generateResponseWithExistingSystemPrompt(final String prompt, final String systemPrompt) {
        System.out.println("USER MESS: " + prompt);
        final UserMessage userMessage = new UserMessage(prompt);
        final SystemMessage systemMessage = new SystemMessage(systemPrompt);
        return generateResponse(List.of(systemMessage, userMessage));
    }

    private String generateResponse(final List<Message> messages) {
        final String response = chatModel.call(new Prompt(messages))
                .getResult()
                .getOutput()
                .getContent();
        System.out.println("OPENAI RESPONSE: " + response);
        return response;
    }

    private String loadSystemPrompt(String promptPath) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + promptPath);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load system prompt from " + promptPath, e);
        }
    }

    private String readAndFormatTranscription(Resource resource) {
        try {
            String content = Files.readString(Paths.get(resource.getURI()));
            return String.format("<transcription>%s</transcription>\n", content);
        } catch (IOException e) {
            log.error("Failed to read transcription file: {}", resource.getFilename(), e);
            return "";
        }
    }
} 