package org.sluja.aicourse.S01.task3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public record Answer(String apikey,
                     String description,
                     String copyright,
                     @JsonProperty("test-data") List<TestDataElement> testData) implements Serializable {
}
