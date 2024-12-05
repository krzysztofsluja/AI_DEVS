package org.sluja.aicourse.newClasses.utils.webcontent;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.newClasses.utils.webcontent.dto.ReportRequest;
import org.sluja.aicourse.newClasses.utils.webcontent.dto.WebRequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class GetContentFromWebsiteService {
    
    private final RestClient myRestClient;
    @Value("${task.central.verify.address}")
    private String reportUrl;

    public String getContent(String url) {
        return myRestClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }
    
    public byte[] getContentAsBytes(String url) {
        return myRestClient.get()
                .uri(url)
                .retrieve()
                .body(byte[].class);
    }

    public String getContent(final String url, final String body, final String key) {
        WebRequestBody requestBody = WebRequestBody.builder()
                .apikey(key)
                .query(body)
                .build();

        return myRestClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);
    }

    public <T> T getContent(final String url, final String body, final String key, final Class<T> responseType) {
        WebRequestBody requestBody = WebRequestBody.builder()
                .apikey(key)
                .query(body)
                .build();

        return myRestClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(responseType);
    }

    public String reportAnswer (final String taskName, final String apiKey, final String answer) {
        ReportRequest requestBody = ReportRequest.builder()
                .task(taskName)
                .apikey(apiKey)
                .answer(answer.trim())
                .build();

        return myRestClient.post()
                .uri(reportUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);
    }
}
