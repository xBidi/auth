package com.example.demo.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter public class UserInfoOutputDto {
    @ApiModelProperty(example = "4b4beaba-42d3-4284-bf7c-4a00100bc828") private String userId;
    @ApiModelProperty(example = "user") private String username;
    @ApiModelProperty(example = "[ROLE_USER,ROLE_ADMIN]") private List<String> roles;
    @ApiModelProperty(example = "[READ,CREATE]") private List<String> scopes;
    private List<TokenDto> sessions = new ArrayList<>();
}
