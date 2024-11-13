package org.sluja.aicourse.S01.task7.service;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.S01.task4.dtos.response.ApiResponse;
import org.sluja.aicourse.S01.task4.service.ContentProcessingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ParametrizedApiResponseService {
    
    @Value("${task.api.key}")
    private String apiKey;
    
    @Value("${task.central.verify.address}")
    private String verifyAddress;
    
    private final RestClient restClient = RestClient.builder().build();

    public String getProcessedResponse(final String taskName, final String answer) {
        ApiResponse response = new ApiResponse(taskName, apiKey, answer);
        
        return restClient.post()
                .uri(verifyAddress)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
                .retrieve()
                .body(String.class);
    }
} 