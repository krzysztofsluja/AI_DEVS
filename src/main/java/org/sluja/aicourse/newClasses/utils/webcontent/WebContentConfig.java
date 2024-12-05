package org.sluja.aicourse.newClasses.utils.webcontent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class WebContentConfig {

    @Bean
    public RestClient myRestClient() {
        return RestClient.builder()
                .build();
    }
} 