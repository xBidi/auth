package com.example.demo.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data public class SendResetPasswordEmailDto {
    @Email private String email;
}
