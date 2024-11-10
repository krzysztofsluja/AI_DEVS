package org.sluja.aicourse.S01.task4.service;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.S01.task4.dtos.response.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ApiResponseService {

    private final ContentProcessingService contentProcessingService;
    
    @Value("${task4.api.key}")
    private String apiKey;
    
    @Value("${task4.verify.address}")
    private String verifyAddress;
    
    private static final String TASK_NAME = "CENZURA";
    
    private final RestClient restClient = RestClient.builder().build();

    public String getProcessedResponse() {
        String processedContent = contentProcessingService.processContent();
        ApiResponse response = new ApiResponse(TASK_NAME, apiKey, processedContent);
        
        return restClient.post()
                .uri(verifyAddress)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
                .retrieve()
                .body(String.class);
    }
} 