package org.sluja.aicourse.S01.task4.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record ApiResponse(String task,
                          @JsonProperty("apikey") String apiKey,
                          String answer) implements Serializable {
}
