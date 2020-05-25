package com.example.demo.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
/**
 * @author diegotobalina
 */
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString public class UserInfoOutputDto {
    @ApiModelProperty(example = "4b4beaba-42d3-4284-bf7c-4a00100bc828") private String userId;
    @ApiModelProperty(example = "user") private String username;
    @ApiModelProperty(example = "email@email.com") private String email;
    @ApiModelProperty(example = "false") private Boolean emailVerified;
    @ApiModelProperty(example = "[ROLE_USER,ROLE_ADMIN]") private List<String> roles;
    @ApiModelProperty(example = "[READ,CREATE]") private List<String> scopes;
    private List<TokenDto> sessions = new ArrayList<>();
}
