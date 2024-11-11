package org.sluja.aicourse.S01.task5.controller;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.S01.task5.service.DownloadAudioFilesFromDirectoryService;
import org.sluja.aicourse.S01.task5.service.Task5Service;
import org.sluja.aicourse.S01.task5.service.TranscriptionQuestionService;
import org.sluja.aicourse.S01.task5.service.TranscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/5")
@RequiredArgsConstructor
public class TranscriptionController {

    private final DownloadAudioFilesFromDirectoryService downloadAudioFilesFromDirectoryService;
    private final TranscriptionQuestionService transcriptionQuestionService;
    private final Task5Service task5Service;

    @GetMapping("/downloadedfiles")
    public ResponseEntity<String> getDownloadedFiles() {
        try {
            final List<String> transcription = downloadAudioFilesFromDirectoryService.getAudioFilePaths();
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error processing audio file: " + e.getMessage());
        }
    }

    @PostMapping("/transcribe")
    public ResponseEntity<String> transcribeAudioFile() {
        return ResponseEntity.ok(task5Service.getProcessedResponse());
    }
} 