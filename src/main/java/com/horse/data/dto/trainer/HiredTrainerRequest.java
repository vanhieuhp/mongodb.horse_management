package com.horse.data.dto.trainer;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class HiredTrainerRequest {

    @NotBlank(message = "HorseId should not be blank")
    String horseId;

    @NotBlank(message = "TrainerId should not be blank")
    String trainerId;
}
