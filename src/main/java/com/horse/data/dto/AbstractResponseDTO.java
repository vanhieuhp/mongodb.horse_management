package com.horse.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class AbstractResponseDTO {

    private String id;
    private Date createdAt;
    private Date modifiedAt;
}
