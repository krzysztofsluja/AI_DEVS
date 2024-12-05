package org.sluja.aicourse.newClasses.tasks.S04E02.controller;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.newClasses.tasks.S04E02.service.CreateFineTuneDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finetune")
@RequiredArgsConstructor
public class FineTuneDataController {

    private final CreateFineTuneDataService createFineTuneDataService;

    @PostMapping("/create-training-data")
    public ResponseEntity<String> createTrainingData() {
        try {
            createFineTuneDataService.saveFineTunedDataToJsonl();
            return ResponseEntity.ok("Training data has been successfully created and saved to JSONL file");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to create training data: " + e.getMessage());
        }
    }
} 