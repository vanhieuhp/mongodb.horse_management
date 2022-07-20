package com.horse.data.dto.trainer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Builder
public class HiredTrainerResponse {

    private String accountId;
    private String username;
    private String horseId;
    private String horseName;
    private String trainerId;
    private String trainerName;
    private Date createdAt;
    private Date modifiedAt;
}
