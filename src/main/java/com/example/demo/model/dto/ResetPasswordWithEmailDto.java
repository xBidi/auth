package com.example.demo.model.dto;

import lombok.Data;

@Data
public class ResetPasswordWithEmailDto {

    private String token;
    private String newPassword;
}
