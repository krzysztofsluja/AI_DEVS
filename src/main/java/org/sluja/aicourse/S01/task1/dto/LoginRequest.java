package org.sluja.aicourse.S01.task1.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginRequest implements Serializable {
    private String username;
    private String password;
} 