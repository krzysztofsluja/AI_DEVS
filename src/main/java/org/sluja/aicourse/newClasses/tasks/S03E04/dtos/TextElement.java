package org.sluja.aicourse.newClasses.tasks.S03E04.dtos;

import java.io.Serializable;
import java.util.List;

public record TextElement(String type,
                          List<String> elements) implements Serializable {
}
