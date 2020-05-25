package com.example.demo.model.dto;

import lombok.Data;

@Data public class UpdateUserPasswordDto {
    private String oldPassword;
    private String newPassword;
}
