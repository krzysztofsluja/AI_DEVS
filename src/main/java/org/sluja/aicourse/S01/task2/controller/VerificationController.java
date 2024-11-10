package org.sluja.aicourse.S01.task2.controller;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.S01.task2.service.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/verify")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    @PostMapping
    public ResponseEntity<String> getVerification() {
        String question = verificationService.verify();
        return ResponseEntity.ok(question);
    }
}
