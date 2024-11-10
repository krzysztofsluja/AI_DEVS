package org.sluja.aicourse.S01.task3.dto;

import java.io.Serializable;

public record ApiResponse(String task,
                          String apikey,
                          Answer answer) implements Serializable {
}
