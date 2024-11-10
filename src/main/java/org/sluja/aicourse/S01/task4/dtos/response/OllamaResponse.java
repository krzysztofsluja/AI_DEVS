package org.sluja.aicourse.S01.task4.dtos.response;

public record OllamaResponse(
    String model,
    String response,
    boolean done
) {} 