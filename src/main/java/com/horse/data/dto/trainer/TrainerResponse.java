package com.horse.data.dto.trainer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
public class TrainerResponse{

    private String id;
    private String name;
    private Integer age;
    private String gender;
    private String address;
    private Set<String> trainerHorses;
    private Date createdAt;
    private Date modifiedAt;
}
