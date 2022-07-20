package com.horse.business.impl;

import com.horse.business.service.AccountService;
import com.horse.data.collection.Account;
import com.horse.data.collection.Horse;
import com.horse.data.collection.Role;
import com.horse.data.collection.Trainer;
import com.horse.data.dto.account.AccountRequest;
import com.horse.data.dto.account.AccountResponse;
import com.horse.data.dto.account.PageInfoRequest;
import com.horse.data.dto.trainer.HiredTrainerRequest;
import com.horse.data.dto.trainer.HiredTrainerResponse;
import com.horse.data.dto.trainer.TrainerInfoResponse;
import com.horse.data.dto.trainer.TrainerResponse;
import com.horse.data.repository.account.AccountRepository;
import com.horse.data.repository.horse.HorseRepository;
import com.horse.data.repository.role.RoleRepository;
import com.horse.exception.DataConflictException;
import com.horse.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private HorseRepository horseRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {

        if (accountRepository.findByUsername(accountRequest.getUsername()).isPresent()) {
            throw new DataNotFoundException("Username is existed in database: " + accountRequest.getUsername());
        } else {
            String bcryptPassword = passwordEncoder.encode(accountRequest.getPassword());

            // get role from accountRequest
            Role role = roleRepository.findRoleByCode(accountRequest.getRole())
                    .orElseThrow(() -> new DataNotFoundException("Role not found with code: " + accountRequest.getRole()));

            // create new account model
            Account account = Account.builder().username(accountRequest.getUsername()).password(bcryptPassword)
                            .role(role).build();
            account.setCreatedAt(new Date());

            // save account into mongodb
            Account createdAccount = accountRepository.save(account);

            // return account response to front end;
            return AccountResponse.builder().id(createdAccount.getId()).username(createdAccount.getUsername())
                            .role(role.getName()).createdAt(createdAccount.getCreatedAt()).build();
        }
    }

    @Override
    public void changePassword(String password) {

        // get current account and change password;
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        account.setPassword(passwordEncoder.encode(password));
        account.setModifiedAt(new Date());
        accountRepository.save(account);
    }

    @Override
    public AccountResponse getOne(String id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Account not found with id: " + id));

        return transferAccountIntoAccountResponse(account);
    }

    @Override
    public AccountResponse getInfo() {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return transferAccountIntoAccountResponse(account);
    }

    @Override
    public String getAuthoritiesOfAccount(String username) {

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("Account not found with username"));

        return account.getRole().getCode();
    }

    @Override
    public List<TrainerResponse> getAllTrainers() {
        List<Account> trainerAccounts = accountRepository.findByRoleCode("TRAINER");
        if (!trainerAccounts.isEmpty()) {
            return trainerAccounts.stream().map(account -> {
                return TrainerResponse.builder().id(account.getId()).name(account.getTrainerInfo().getName())
                        .age(account.getTrainerInfo().getAge()).gender(account.getTrainerInfo().getGender())
                        .address(account.getTrainerInfo().getAddress()).horses(account.getHorses())
                        .createdAt(account.getCreatedAt()).modifiedAt(account.getModifiedAt()).build();
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public HiredTrainerResponse hiredTrainer(HiredTrainerRequest hiredTrainerRequest) {

        String trainerId = hiredTrainerRequest.getTrainerId();
        String horseId = hiredTrainerRequest.getHorseId();

        Account currentAccount = Account.getCurrentAccount();

        // check horse is existed in current Account
        if (!currentAccount.getHorses().containsKey(horseId)) {
            throw new DataConflictException("Horse not found with id: " + horseId);
        }

        // check trainer is existed in database
        Account trainerAccount = accountRepository.findById(trainerId).orElseThrow(() -> new DataNotFoundException("Trainer not found with id: " + trainerId));
        if (!"TRAINER".equals(trainerAccount.getRole().getCode())) {
            throw new DataConflictException("Trainer not found with id: " + trainerId);
        }

        // update data into horse Database
        Horse horseNeedTraining = horseRepository.findById(horseId).orElseThrow(() -> new DataNotFoundException("Horse not found with id: " + horseId));
        Map<String, String> trainerOfHorse = new HashMap<>();
        trainerOfHorse.put(trainerAccount.getId(), trainerAccount.getTrainerInfo().getName());
        horseNeedTraining.setTrainerOfHorse(trainerOfHorse);
        horseNeedTraining.setModifiedAt(new Date());

        // update data into current Account
        currentAccount.getHiredTrainers().put(trainerAccount.getId(), trainerAccount.getTrainerInfo().getName());
        trainerAccount.getHorses().put(horseNeedTraining.getId(), horseNeedTraining.getName());

        Horse modifiedHorse = horseRepository.save(horseNeedTraining);
        accountRepository.save(currentAccount);
        accountRepository.save(trainerAccount);

        return HiredTrainerResponse.builder()
                        .accountId(currentAccount.getId()).username(currentAccount.getUsername())
                        .trainerId(trainerAccount.getId()).trainerName(trainerAccount.getTrainerInfo().getName())
                        .horseId(horseNeedTraining.getId()).horseName(horseNeedTraining.getName())
                        .createdAt(new Date()).build();
    }

    @Override
    public Map<String, Object> findAll(PageInfoRequest pageInfoRequest) {
        Map<String, Object> response = new HashMap<>();
        Sort sort = Sort.by(pageInfoRequest.getSortBy());
        Pageable pageable = PageRequest.of(pageInfoRequest.getPageNumber(), pageInfoRequest.getPageSize(), sort);
        Page<Account> accounts = accountRepository.findAll(pageable);
        response.put("data", accounts.getContent());
        response.put("Total No Of Page", accounts.getTotalPages());
        response.put("Total no of Elements", accounts.getTotalElements());
        response.put("Current Page No", accounts.getNumber());
        return response;
    }

    public AccountResponse transferAccountIntoAccountResponse(Account account) {

        AccountResponse accountResponse = AccountResponse.builder().id(account.getId()).username(account.getUsername())
                        .role(account.getRole().getName()).createdAt(account.getCreatedAt()).build();

        if (account.getModifiedAt() != null) {
            accountResponse.setModifiedAt(account.getModifiedAt());
        }

        // set hired trainers for userAccount;
        if (!"TRAINER".equals(account.getRole().getCode()) && !account.getHiredTrainers().isEmpty()) {
            accountResponse.setHiredTrainers(account.getHiredTrainers());
        }

        // set trainerInfo for trainerAccount
        if ("TRAINER".equals(account.getRole().getCode()) && account.getTrainerInfo() != null) {
            TrainerInfoResponse trainerInfo = TrainerInfoResponse.builder().name(account.getTrainerInfo().getName())
                            .address(account.getTrainerInfo().getAddress()).age(account.getTrainerInfo().getAge())
                            .gender(account.getTrainerInfo().getGender()).build();

            accountResponse.setTrainerInfo(trainerInfo);
        }

        if (!account.getHorses().isEmpty()) {
            accountResponse.setHorses(account.getHorses());
        }

        return accountResponse;
    }
}
