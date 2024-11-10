package org.sluja.aicourse.S01.task1.controller;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.sluja.aicourse.S01.task1.dto.LoginRequest;
import org.sluja.aicourse.S01.task1.service.ChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final ChatService chatService;
    @Value("${org.agents}")
    private String AGENTS_WEBSITE;

    @PostMapping("/login2")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            String answer = chatService.getAnswerForWebsite();
            Document doc = Jsoup.connect(AGENTS_WEBSITE)
                    .data("username", loginRequest.getUsername())
                    .data("password", loginRequest.getPassword())
                    .data("answer", answer)
                    .post();

            return ResponseEntity.ok(doc.html());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Login failed: " + e.getMessage());
        }
    }
} 