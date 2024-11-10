package org.sluja.aicourse.S01.task4.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExternalFileDownloadService {

    @Value("${task4.file.url}")
    private String fileUrl;
    
    private final RestClient restClient = RestClient.builder()
            .build();
            
    public Pair<String, Map<String, String>> downloadContent() {
        try {
            var response = restClient.get()
                    .uri(fileUrl)
                    .retrieve()
                    .toEntity(String.class);
                    
            Map<String, String> headers = response.getHeaders()
                    .toSingleValueMap();
                    
            return Pair.of(response.getBody(), headers);
            
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Failed to connect to the server. Please check your internet connection.", e);
        } catch (RestClientException e) {
            if (e.getMessage().contains("404")) {
                throw new RuntimeException("The requested file does not exist: " + fileUrl, e);
            }
            throw new RuntimeException("Failed to download the file: " + e.getMessage(), e);
        }
    }
} 