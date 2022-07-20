package com.horse.data.dto.trainer;

import lombok.*;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Builder
public class TrainerResponse{

    private String id;
    private String name;
    private Integer age;
    private String gender;
    private String address;
    private Map<String, String> horses;
    private Date createdAt;
    private Date modifiedAt;
}
