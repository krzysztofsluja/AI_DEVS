package org.sluja.aicourse.S01.task2.dto;

import java.io.Serializable;

public record ConversationElement(String text,
                                  String msgID) implements Serializable {

    public static ConversationElement startMessage() {
        return new ConversationElement("READY", "0");
    }
}
