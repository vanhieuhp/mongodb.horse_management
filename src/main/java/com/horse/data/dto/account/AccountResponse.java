package com.horse.data.dto.account;

import com.horse.data.dto.ResponseDtoModel;
import com.horse.data.dto.trainer.TrainerInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
public class AccountResponse extends ResponseDtoModel {

    private String id;
    private String username;
    private String role;
    private Set<String> hiredTrainers;
    private Set<String> ownerHorses;
    private TrainerInfoResponse trainerInfo;
    private Date createdAt;
    private Date modifiedAt;
}
