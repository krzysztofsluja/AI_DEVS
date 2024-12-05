package org.sluja.aicourse.newClasses.controllers;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.newClasses.utils.embedding.EmbeddingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/embeddings")
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    @GetMapping("/create")
    public float[] getEmbedding(@RequestBody final String text) {
        return embeddingService.getEmbedding(text);
    }
}
