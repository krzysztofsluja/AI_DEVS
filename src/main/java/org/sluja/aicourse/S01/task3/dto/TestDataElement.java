package org.sluja.aicourse.S01.task3.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.io.Serializable;

public record TestDataElement(String question,
                              Integer answer,
                              @JsonAlias("test") TestElement test) implements Serializable {
}
