package org.sluja.aicourse.newClasses.utils.chat;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatModelService {

    private final OpenAiChatModel chatModel;

    public enum FileType {
        URL,
        CLASSPATH;
    }

    public String getResponse(final String userPrompt, final String systemPromptPath, final String source, final String sourceAttribute) throws IOException {
        final Prompt prompt = buildPrompt(userPrompt, getSystemPromptWithSource(systemPromptPath, source, sourceAttribute));
        return getResponse(prompt);
    }

    public String getResponse(final String userPrompt, final String systemPromptPath) throws IOException {
        final Prompt prompt = buildPrompt(userPrompt, getSystemPrompt(systemPromptPath));
        return getResponse(prompt);
    }

    public String getResponse(final String userPrompt, final String systemPromptPath, final String filePath, final FileType fileType) throws IOException {
        final Prompt prompt = switch (fileType) {
            case URL -> buildPromptWithUrlFile(userPrompt, getSystemPrompt(systemPromptPath), filePath, MimeTypeUtils.IMAGE_PNG);
            case CLASSPATH -> buildPromptWithClassPathFile(userPrompt, getSystemPrompt(systemPromptPath), filePath, MimeTypeUtils.IMAGE_PNG);
        };
        return getResponse(prompt);
    }

    private String getResponse(final Prompt prompt) {
        return chatModel.call(prompt)
                .getResult()
                .getOutput()
                .getContent();
    }

    private String getSystemPrompt(final String systemPromptPath) throws IOException {
        if(StringUtils.isNotBlank(systemPromptPath)) {
            return Files.readString(new ClassPathResource(systemPromptPath).getFile().toPath());
        }
        return StringUtils.EMPTY;
    }

    private String getSystemPromptWithSource(final String systemPromptPath, final String source, final String sourceAttribute) throws IOException {
        String systemPrompt = getSystemPrompt(systemPromptPath);
        return systemPrompt.replace(sourceAttribute, source);
    }

    private Prompt buildPrompt(final String userMessage, final String systemMessage) {
        final List<Message> messages = List.of(
                new SystemMessage(systemMessage),
                new UserMessage(userMessage)
        );
        return new Prompt(messages);
    }

    private Prompt buildPromptWithUrlFile(final String userMessage, final String systemMessage, final String filePath, final MimeType mediaType) throws IOException {
        final Resource resource = new UrlResource(filePath);
        return buildPromptWithUserResource(userMessage, systemMessage, resource, mediaType);
    }

    private Prompt buildPromptWithClassPathFile(final String userMessage, final String systemMessage, final String filePath, final MimeType mediaType) throws IOException {
        final Resource resource = new ClassPathResource(filePath);
        return buildPromptWithUserResource(userMessage, systemMessage, resource, mediaType);
    }

    private Prompt buildPromptWithUserResource(final String userMessage, final String systemMessage, final Resource resource, final MimeType mediaType) throws IOException {
        final List<Message> messages = List.of(
                new SystemMessage(systemMessage),
                new UserMessage(userMessage, new Media(mediaType, resource))
        );
        return new Prompt(messages);
    }
}
