package com.horse.business.impl;

import com.horse.business.service.TrainerService;
import com.horse.data.collection.Account;
import com.horse.data.collection.Trainer;
import com.horse.data.dto.trainer.TrainerRequest;
import com.horse.data.dto.trainer.TrainerResponse;
import com.horse.data.repository.account.AccountRepository;
import com.horse.data.repository.trainer.TrainerRepository;
import com.horse.exception.DataConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TrainerServiceImpl implements TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public TrainerResponse createTrainer(TrainerRequest trainerRequest) {

        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!account.getTrainers().isEmpty()) {
            throw new DataConflictException("An trainer account just only have one trainer");
        }

        Trainer trainer = new Trainer(trainerRequest.getName(), trainerRequest.getAge(), trainerRequest.getGender(), trainerRequest.getAddress());
        trainer.setCreatedAt(new Date());

        Trainer createdTrainer = trainerRepository.save(trainer);
        account.setTrainerInfo(createdTrainer);
        account.setModifiedAt(new Date());
        accountRepository.save(account);

        return new TrainerResponse(createdTrainer.getId(), createdTrainer.getName(), createdTrainer.getAge(),
                createdTrainer.getGender(), createdTrainer.getAddress(), createdTrainer.getCreatedAt());

    }

    @Override
    public TrainerResponse updateTrainer(Integer trainerId, TrainerRequest trainerRequest) {
        return null;
    }

    @Override
    public void deleteTrainer(Integer id) {

    }

    @Override
    public TrainerResponse getOne(Integer id) {
        return null;
    }
}
