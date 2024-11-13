package org.sluja.aicourse.S01.task4.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentProcessingService {

    private final ExternalFileDownloadService externalFileDownloadService;
    private final OllamaService ollamaService;

    @Value("${task4.file.url}")
    private String fileUrl;
    
    private static final String CENSORSHIP_PROMPT_PATH = "prompts/censorship_prompt.md";

    public String processContent() {
        Pair<String, Map<String, String>> downloadResult = externalFileDownloadService.downloadContent(fileUrl);
        String content = downloadResult.getLeft();
        Map<String, String> headers = downloadResult.getRight();
        
        log.info("Downloaded content with headers: {}", headers);
        
        return ollamaService.generateResponse(content, CENSORSHIP_PROMPT_PATH);
    }
} 