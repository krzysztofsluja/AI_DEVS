package org.sluja.aicourse.S01.task6_1.controller;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.S01.task6_1.service.ImageAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/61")
@RequiredArgsConstructor
@Controller
public class Task61Controller {

    private final ImageAnalysisService imageAnalysisService;
    @GetMapping("/answer")
    public ResponseEntity<String> getAnswer() {
        return ResponseEntity.ok(imageAnalysisService.analyzeImage());
    }
}
