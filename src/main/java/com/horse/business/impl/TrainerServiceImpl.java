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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public TrainerResponse createTrainer(TrainerRequest trainerRequest) {

        Account currentAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentAccount.getTrainerInfo() != null) {
            throw new DataConflictException("An trainer account just only have one trainer");
        }

        // set info for trainerInfo
        Trainer trainerInfo = new Trainer();
        trainerInfo.setName(trainerRequest.getName());
        trainerInfo.setAge(trainerRequest.getAge());
        trainerInfo.setGender(trainerRequest.getGender());
        trainerInfo.setAddress(trainerRequest.getAddress());

        // saving trainerInfo into mongodb
        currentAccount.setTrainerInfo(trainerInfo);
        currentAccount.setModifiedAt(new Date());
        Account modifiedAccount = accountRepository.save(currentAccount);

        // responding data for front end
        return getTrainerResponse(modifiedAccount);

    }

    @Override
    public TrainerResponse updateTrainer(TrainerRequest trainerRequest) {

        Account currentAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Trainer trainer = currentAccount.getTrainerInfo();
        if (trainerRequest.getName() != null) {
            trainer.setName(trainerRequest.getName());
        }

        if (trainerRequest.getAge() != null) {
            trainer.setAge(trainerRequest.getAge());
        }

        if (trainerRequest.getGender() != null) {
            trainer.setGender(trainerRequest.getGender());
        }

        if (trainerRequest.getAddress() != null) {
            trainer.setAddress(trainerRequest.getAddress());
        }

        currentAccount.setTrainerInfo(trainer);
        currentAccount.setModifiedAt(new Date());

        Account modifiedAccount = accountRepository.save(currentAccount);
        return getTrainerResponse(modifiedAccount);
    }

    public TrainerResponse getTrainerResponse(Account account) {

        TrainerResponse trainerResponse = TrainerResponse.builder().id(account.getId()).name(account.getTrainerInfo().getName())
                        .age(account.getTrainerInfo().getAge()).address(account.getTrainerInfo().getAddress())
                        .gender(account.getTrainerInfo().getGender()).createdAt(account.getCreatedAt()).build();
        if (account.getModifiedAt() != null) {
            trainerResponse.setModifiedAt(account.getModifiedAt());
        }
        return trainerResponse;
    }
}
