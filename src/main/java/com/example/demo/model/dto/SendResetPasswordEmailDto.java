package com.example.demo.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data public class SendResetPasswordEmailDto {
    @Email @NotNull @NotBlank private String email;
}
