package org.sluja.aicourse.S01.task6.controller;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.S01.task6.service.ImageProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/6")
@RequiredArgsConstructor
public class Task6Controller {

    private final ImageProcessingService imageProcessingService;

    @GetMapping("/process")
    public ResponseEntity<String> processTask() {
        return ResponseEntity.ok(imageProcessingService.processCityImages());
    }
} 