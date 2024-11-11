package org.sluja.aicourse.S01.task5.config;

import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WhisperConfig {

    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;

    @Bean
    public OpenAiAudioTranscriptionModel whisperTranscriptionClient() {
        OpenAiAudioApi audioApi = new OpenAiAudioApi(openaiApiKey);
        return new OpenAiAudioTranscriptionModel(audioApi);
    }

    public static org.springframework.ai.openai.OpenAiAudioTranscriptionOptions transcriptionOptions() {
      return OpenAiAudioTranscriptionOptions.builder()
              .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
              .withTemperature(0f)
              .build();
    }
} 