package org.sluja.aicourse.newClasses.utils.webcontent;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class GetContentFromWebsiteService {
    
    private final RestClient restClient;

    public String getContent(String url) {
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }
    
    public byte[] getContentAsBytes(String url) {
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(byte[].class);
    }
}
