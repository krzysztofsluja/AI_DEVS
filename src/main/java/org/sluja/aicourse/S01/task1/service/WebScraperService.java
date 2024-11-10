package org.sluja.aicourse.S01.task1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebScraperService {

    @Value("${org.agents}")
    private String WEBSITE_URL;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public String fetchQuestion() {
        try {
            Document doc = Jsoup.connect(WEBSITE_URL).get();
            return doc.select("p#human-question").text().replace("Question:", StringUtils.EMPTY);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch question from website", e);
        }
    }

    public String submitAnswer(String username, String password, String answer) {
        try {
            Map<String, String> formData = new HashMap<>();
            formData.put("username", username);
            formData.put("password", password);
            formData.put("answer", answer);

            Connection.Response response = Jsoup.connect(WEBSITE_URL)
                    .method(Connection.Method.POST)
                    .data(formData)
                    .header("Content-Type", "application/json")
                    .ignoreContentType(true)
                    .execute();

            System.out.println("RES: "  +response.body());
            // Parse the JSON response to extract the code
            Map<String, String> responseMap = objectMapper.readValue(response.body(), Map.class);
            if (responseMap.containsKey("code")) {
                return responseMap.get("code");
            } else if (responseMap.containsKey("error")) {
                throw new RuntimeException("Login failed: " + responseMap.get("error"));
            }
            
            throw new RuntimeException("Unexpected response format");
        } catch (Exception e) {
            throw new RuntimeException("Failed to submit answer: " + e.getMessage(), e);
        }
    }
} 