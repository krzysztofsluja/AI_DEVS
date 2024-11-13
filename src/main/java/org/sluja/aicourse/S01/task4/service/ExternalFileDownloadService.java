package org.sluja.aicourse.S01.task4.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExternalFileDownloadService {


    
    private final RestClient restClient = RestClient.builder()
            .build();
            
    public Pair<String, Map<String, String>> downloadContent(final String url) {
        try {
            var response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(String.class);
                    
            Map<String, String> headers = response.getHeaders()
                    .toSingleValueMap();
                    
            return Pair.of(response.getBody(), headers);
            
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Failed to connect to the server. Please check your internet connection.", e);
        } catch (RestClientException e) {
            if (e.getMessage().contains("404")) {
                throw new RuntimeException("The requested file does not exist: " + url, e);
            }
            throw new RuntimeException("Failed to download the file: " + e.getMessage(), e);
        }
    }

    public <T> T downloadJsonContent(final String url, Class<T> responseType) {
        try {
            return restClient.get()
                    .uri(url)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(responseType);
                    
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Failed to connect to the server. Please check your internet connection.", e);
        } catch (RestClientException e) {
            if (e.getMessage().contains("404")) {
                throw new RuntimeException("The requested file does not exist: " + url, e);
            }
            throw new RuntimeException("Failed to download the file: " + e.getMessage(), e);
        }
    }
} 