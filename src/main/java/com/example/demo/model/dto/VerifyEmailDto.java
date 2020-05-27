package com.example.demo.model.dto;

import com.example.demo.model.validator.VerifyEmailTokenConstraint;
import lombok.Data;

@Data public class VerifyEmailDto {
    @VerifyEmailTokenConstraint private String token;
}
