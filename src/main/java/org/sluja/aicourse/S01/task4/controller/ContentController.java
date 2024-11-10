package org.sluja.aicourse.S01.task4.controller;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.S01.task4.service.ApiResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/content")
@RequiredArgsConstructor
public class ContentController {

    private final ApiResponseService apiResponseService;

    @GetMapping("/process")
    public ResponseEntity<String> processContent() {
        try {
            String response = apiResponseService.getProcessedResponse();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error processing content: " + e.getMessage());
        }
    }
} 