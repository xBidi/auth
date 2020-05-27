package com.example.demo.model.dto;

import com.example.demo.model.validator.PasswordConstraint;
import com.example.demo.model.validator.ResetPasswordTokenConstraint;
import lombok.Data;

@Data public class ResetPasswordWithEmailDto {

    @ResetPasswordTokenConstraint private String token;
    @PasswordConstraint private String newPassword;
}
