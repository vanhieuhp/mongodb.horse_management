package com.horse.business.impl;

import com.horse.business.service.AccountService;
import com.horse.data.collection.Account;
import com.horse.data.collection.Role;
import com.horse.data.dto.account.AccountRequest;
import com.horse.data.dto.account.AccountResponse;
import com.horse.data.repository.account.AccountRepository;
import com.horse.data.repository.role.RoleRepository;
import com.horse.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {

        if (accountRepository.findByUsername(accountRequest.getUsername()).isPresent()) {
            throw new DataNotFoundException("Username is existed in database: " + accountRequest.getUsername());
        } else {
            String password = passwordEncoder.encode(accountRequest.getPassword());

            // get role from accountRequest
            Role role = roleRepository.findRoleByCode(accountRequest.getRole())
                    .orElseThrow(() -> new DataNotFoundException("Role not found with code: " + accountRequest.getRole()));

            // create new account and save it
            Account account = new Account(accountRequest.getUsername(), password, role, new Date());
            Account createdAccount = accountRepository.save(account);

            return new AccountResponse(createdAccount.getId(), createdAccount.getUsername(), role.getName());
        }
    }

    @Override
    public void changePassword(String password) {

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

    public AccountResponse transferAccountIntoAccountResponse(Account account) {

        List<Map<String, String>> trainers = new ArrayList<>();
        List<Map<String, String>> horses = new ArrayList<>();

        if (!"TRAINER".equals(account.getRole().getCode()) && account.getTrainers() != null) {
            account.getTrainers().forEach(trainer -> {
                Map<String, String> trainerInfo = new HashMap<>();
                trainerInfo.put(trainer.getId(), trainer.getName());
                trainers.add(trainerInfo);
            });
        }

        return new AccountResponse(account.getId(), account.getUsername(), account.getRole().getName(),
                trainers, horses, account.getCreatedAt(), account.getModifiedAt());
    }
}
