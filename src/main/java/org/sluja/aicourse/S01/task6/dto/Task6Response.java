package org.sluja.aicourse.S01.task6.dto;

import java.io.Serializable;

public record Task6Response(
    String task,
    String apiKey,
    String answer
) implements Serializable {} 