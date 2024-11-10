package org.sluja.aicourse.S01.task4.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OllamaConfig {

    @Value("${ollama.base.url}")
    private String baseUrl;

    @Value("${ollama.model.name}")
    private String modelName;

    @Bean
    @Primary
    public OllamaChatModel taskOllamaChatModel() {
        OllamaApi ollamaApi = new OllamaApi(baseUrl);
        return new OllamaChatModel(ollamaApi, OllamaOptions.builder()
                .withModel(OllamaModel.LLAMA3_1)
                .withTemperature(0.8)
                .build());

    }

} 