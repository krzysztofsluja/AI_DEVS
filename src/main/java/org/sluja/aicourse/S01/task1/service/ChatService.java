package org.sluja.aicourse.S01.task1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiChatModel chatModel;
    private final WebScraperService webScraperService;

    public String getAnswerForWebsite() {
        String question = webScraperService.fetchQuestion();
        final String promptText = "Odpowiedz na zadane pytanie: {message}. Twoja odpowiedź powinna być numeryczna, bez żadnych tłumaczeń, jedynie cyfry.";
        return chat(question, promptText);
    }

    public String chat(String message, String promptText) {
        final PromptTemplate promptTemplate;
        String response;
        try {
            promptTemplate = new PromptTemplate(promptText,
                    Map.of("message", message));

            ChatResponse chatResponse = chatModel.call(promptTemplate.create());
            response = chatResponse.getResult().getOutput().getContent();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
