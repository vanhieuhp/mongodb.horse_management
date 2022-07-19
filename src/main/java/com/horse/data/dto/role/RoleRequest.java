package com.horse.data.dto.role;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RoleRequest {

    @NotBlank(message = "Name role should not be blank")
    private String name;

    @NotBlank(message = "Code role should not be blank")
    private String code;
}
