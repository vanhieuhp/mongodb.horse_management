package com.horse.data.dto.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class RoleResponse {

    private String id;
    private String name;
    private String code;
    private Date createdAt;
    private Date modifiedAt;
}
