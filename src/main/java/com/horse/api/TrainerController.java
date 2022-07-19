package com.horse.api;

import com.horse.business.service.TrainerService;
import com.horse.data.dto.trainer.TrainerRequest;
import com.horse.data.dto.trainer.TrainerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/trainers")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @PostMapping
    public ResponseEntity<?> createTrainer(@RequestBody @Valid TrainerRequest trainerRequest) {

        TrainerResponse trainerResponse = trainerService.createTrainer(trainerRequest);
        return new ResponseEntity<>(trainerResponse, HttpStatus.OK);
    }
}
