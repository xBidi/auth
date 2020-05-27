package com.example.demo.model.dto;

import com.example.demo.model.validator.PasswordConstraint;
import lombok.Data;

@Data public class UpdateUserPasswordDto {
    @PasswordConstraint private String oldPassword;
    @PasswordConstraint private String newPassword;
}
