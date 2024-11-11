package org.sluja.aicourse.S01.task5.service;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.S01.task4.dtos.response.ApiResponse;
import org.sluja.aicourse.S01.task4.service.ContentProcessingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@RequiredArgsConstructor
@Service
public class Task5Service {

    private final TranscriptionQuestionService transcriptionQuestionService;
    
    @Value("${task4.api.key}")
    private String apiKey;
    
    @Value("${task4.verify.address}")
    private String verifyAddress;
    
    private static final String TASK_NAME = "mp3";
    
    private final RestClient restClient = RestClient.builder().build();

    public String getProcessedResponse() {
        ApiResponse response = new ApiResponse(TASK_NAME, apiKey, transcriptionQuestionService.getAnswer());
        
        return restClient.post()
                .uri(verifyAddress)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
                .retrieve()
                .body(String.class);
    }
} 