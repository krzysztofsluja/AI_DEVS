package org.sluja.aicourse.newClasses.tasks.S04E02.dtos;

import org.sluja.aicourse.newClasses.tasks.S04E02.service.CreateFineTuneDataService.DataType;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public record FineTunedDataObject(List<Message> messages) implements Serializable {

    public static final String SYSTEM_MESSAGE = "Rate that sequence: correct or incorrect";

    public record Message(String role, String content) {}

    public static List<FineTunedDataObject> createFromData(List<String> data, DataType type) {
        return data.stream()
                .map(line -> new FineTunedDataObject(List.of(
                        new Message("system", SYSTEM_MESSAGE),
                        new Message("user", line),
                        new Message("assistant", type == DataType.CORRECT ? "correct" : "incorrect")
                )))
                .collect(Collectors.toList());
    }
}
