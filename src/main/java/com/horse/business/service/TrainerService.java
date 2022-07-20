package com.horse.business.service;

import com.horse.data.dto.trainer.TrainerRequest;
import com.horse.data.dto.trainer.TrainerResponse;

public interface TrainerService {

    TrainerResponse createTrainer(TrainerRequest trainerRequest);
    TrainerResponse updateTrainer(TrainerRequest trainerRequest);
}
