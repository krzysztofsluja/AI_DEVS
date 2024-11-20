package org.sluja.aicourse.newClasses.S03E02.controller;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.newClasses.S03E02.service.Task10Service;
import org.sluja.aicourse.newClasses.utils.embedding.EmbeddingService;
import org.sluja.aicourse.newClasses.utils.files.GetLocalFilesService;
import org.springframework.ai.embedding.Embedding;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/10")
public class Task10Controller {

    private final Task10Service task10Service;

    @GetMapping("/embeddings")
    public String getEmbeddings() throws Exception {
        return task10Service.getDate();
    }
}
