package com.horse.data.dto.account;

import com.horse.data.dto.ResponseDtoModel;
import com.horse.data.dto.trainer.TrainerInfoResponse;
import javafx.scene.NodeBuilder;
import lombok.*;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Builder
public class AccountResponse extends ResponseDtoModel {

    private String id;
    private String username;
    private String role;
    private Map<String, String> hiredTrainers;
    private Map<String, String> horses;
    private TrainerInfoResponse trainerInfo;
    private Date createdAt;
    private Date modifiedAt;
}
