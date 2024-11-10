package org.sluja.aicourse.S01.task3.controller;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.S01.task3.service.AnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/task3")
@RequiredArgsConstructor
public class DownloadController {

    private final AnswerService answerService;

    @GetMapping("/download")
    public ResponseEntity<String> downloadAndSaveFile() {
        try {
            return answerService.getAnswer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
