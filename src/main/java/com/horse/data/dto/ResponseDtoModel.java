package com.horse.data.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public abstract class ResponseDtoModel {

    private String id;
    private Date createdAt;
    private Date modifiedAt;

}
