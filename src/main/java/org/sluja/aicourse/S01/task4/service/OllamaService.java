package org.sluja.aicourse.S01.task4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OllamaService {
    
    private final OllamaChatModel taskOllamaChatModel;
    private final ResourceLoader resourceLoader;

    public String generateResponse(final String prompt) {
        return generateResponse(List.of(new UserMessage(prompt)));
    }

    public String generateResponse(final String prompt, final String systemPromptPath) {
        System.out.println("USER MESS: " + prompt);
        final String systemPromptContent = loadSystemPrompt(systemPromptPath);
        final UserMessage userMessage = new UserMessage(prompt);
        final SystemMessage systemMessage = new SystemMessage(systemPromptContent);
        return generateResponse(List.of(systemMessage, userMessage));
    }

    private String generateResponse(final List<Message> messages) {
        final String response = taskOllamaChatModel.call(new Prompt(messages))
                .getResult()
                .getOutput()
                .getContent();
        System.out.println("OLLAMA RESPONSE: " + response);
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
} 