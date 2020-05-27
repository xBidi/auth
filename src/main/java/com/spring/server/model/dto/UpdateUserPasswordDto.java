package com.spring.server.model.dto;

import com.spring.server.model.validator.PasswordConstraint;
import lombok.Data;

@Data public class UpdateUserPasswordDto {
    @PasswordConstraint private String oldPassword;
    @PasswordConstraint private String newPassword;
}
