package org.sluja.aicourse.S01.task3.service;

import org.sluja.aicourse.S01.task3.dto.Answer;
import org.sluja.aicourse.S01.task3.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;

@Service
public class AnswerService {

    private final GetTestDataService testDataService;
    private final String apiKey;
    private final RestClient restClient;
    private final String verifyAddress;

    public AnswerService(
            GetTestDataService testDataService,
            @Value("${task3.api.key}") String apiKey,
            @Value("${task3.verify.address}") String verifyAddress) {
        this.testDataService = testDataService;
        this.apiKey = apiKey;
        this.verifyAddress = verifyAddress;
        this.restClient = RestClient.create();
    }

    public ResponseEntity<String> getAnswer() throws IOException {
        ApiResponse response = prepareResponse();

        return restClient.post()
                .uri(verifyAddress)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
                .retrieve()
                .toEntity(String.class);
    }

    private ApiResponse prepareResponse() throws IOException {
        Answer answer = new Answer(
                apiKey,
                testDataService.getDescription(),
                testDataService.getCopyright(),
                testDataService.getTestData()
        );

        return new ApiResponse(
                "JSON",
                apiKey,
                answer
        );
    }
}