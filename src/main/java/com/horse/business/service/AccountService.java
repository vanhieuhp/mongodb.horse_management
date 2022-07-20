package com.horse.business.service;

import com.horse.data.dto.account.AccountRequest;
import com.horse.data.dto.account.AccountResponse;
import com.horse.data.dto.account.PageInfoRequest;
import com.horse.data.dto.trainer.HiredTrainerRequest;
import com.horse.data.dto.trainer.HiredTrainerResponse;
import com.horse.data.dto.trainer.TrainerResponse;

import java.util.List;
import java.util.Map;

public interface AccountService {

    AccountResponse createAccount(AccountRequest accountRequest);
    void changePassword(String password);
    AccountResponse getOne(String id);
    AccountResponse getInfo();
    String getAuthoritiesOfAccount(String username);
    List<TrainerResponse> getAllTrainers();
    HiredTrainerResponse hiredTrainer(HiredTrainerRequest hiredTrainerRequest);
    Map<String, Object> findAll(PageInfoRequest pageInfoRequest);
}
