package com.horse.business.impl;

import com.horse.business.service.HorseService;
import com.horse.data.collection.Account;
import com.horse.data.collection.Horse;
import com.horse.data.dto.horse.HorseRequest;
import com.horse.data.dto.horse.HorseResponse;
import com.horse.data.repository.account.AccountRepository;
import com.horse.data.repository.horse.HorseRepository;
import com.horse.exception.DataConflictException;
import com.horse.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HorseServiceImpl implements HorseService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private HorseRepository horseRepository;

    @Override
    public List<HorseResponse> findAll() {

        List<Horse> horseList = horseRepository.findAll();
        return horseList.stream().map(horse -> HorseResponse.builder().id(horse.getId()).name(horse.getName()).foaled(horse.getFoaled())
                .price(horse.getPrice()).createdAt(horse.getCreatedAt())
                .modifiedAt(horse.getModifiedAt()).build()).collect(Collectors.toList());
    }

    @Override
    public List<HorseResponse> findByTrainerAndYear(String trainerId, Integer year) {
        Account trainer = accountRepository.findById(trainerId).orElse(null);

        Set<Horse> temp = new HashSet<>();
        List<HorseResponse> horseResponseList = new ArrayList<>();
        List<Horse> trainerHorse = new ArrayList<>();
        List<Horse> yearHorse = new ArrayList<>();

        if (trainer != null) {
            trainerHorse = horseRepository.findByTrainer(trainer);
            temp.addAll(trainerHorse);
        }

        if (year != 0) {
            yearHorse = horseRepository.findByYear(year);
            temp.addAll(yearHorse);
        }

        List<Horse> filterHorse = new ArrayList<>(temp);

        if(!trainerHorse.isEmpty()) {
            for (int i = filterHorse.size()-1; i > 0; i--) {
                if (!trainerHorse.contains(filterHorse.get(i))) {
                    filterHorse.remove(i);
                }
            }
        }

        if(!yearHorse.isEmpty()) {
            for (int i = filterHorse.size()-1; i > 0; i--) {
                if (!yearHorse.contains(filterHorse.get(i))) {
                    filterHorse.remove(i);
                }
            }
        }

        if (trainer == null && year == 0) {
            filterHorse = horseRepository.findAll();
        }

        for (Horse horse : filterHorse) {
            horseResponseList.add(getHorseResponse(horse));
        }

        return horseResponseList;
    }

    @Override
    public HorseResponse findOne(String id) {

        Horse horse = horseRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Horse not found with id: " + id));

        return getHorseResponse(horse);
    }

    @Override
    public HorseResponse createHorse(HorseRequest horseRequest) {

        // saving Horse
        Horse horse = new Horse();
        horse.setName(horseRequest.getName());
        horse.setFoaled(horseRequest.getFoaled());
        horse.setCreatedAt(new Date());
        if (horseRequest.getPrice() != null) {
            horse.setPrice(horseRequest.getPrice());
        }
        Horse createdHorse = horseRepository.save(horse);

        // add horseInfo in currentAccount
        Account currentAccount =(Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        currentAccount.getOwnerHorses().add(createdHorse.getId());
        currentAccount.setModifiedAt(new Date());
        accountRepository.save(currentAccount);

        return getHorseResponse(createdHorse);
    }

    @Override
    public HorseResponse updateHorse(String id, HorseRequest horseRequest) {
        Account account = Account.getCurrentAccount();
        if (!account.getOwnerHorses().contains(id)) {
            throw new DataConflictException("Horse not found with id: " + id);
        }

        // check horse is existed in mongodb
        Horse horse = horseRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Horse not found with id: " + id));
        if (horseRequest.getName() != null) {
            horse.setName(horseRequest.getName());
        }

        if (horseRequest.getPrice() != null) {
            horse.setPrice(horseRequest.getPrice());
        }

        if (horseRequest.getFoaled() != null) {
            horse.setFoaled(horseRequest.getFoaled());
        }

        horse.setModifiedAt(new Date());
        Horse modifiedHorse = horseRepository.save(horse);

        return getHorseResponse(modifiedHorse);
    }

    @Override
    public void deleteHorse(String id) {

        Account currentAccount = Account.getCurrentAccount();

        if (!currentAccount.getOwnerHorses().contains(id)) {
            throw new DataConflictException("Horse not found with id: " + id);
        }

        horseRepository.deleteById(id);
        currentAccount.getOwnerHorses().remove(id);
        accountRepository.save(currentAccount);
    }

    @Override
    public List<Horse> findAllByPrice(Integer price) {
        return horseRepository.findByPrice(price);
    }

    private HorseResponse getHorseResponse(Horse horse) {
        HorseResponse horseResponse = HorseResponse.builder().id(horse.getId()).name(horse.getName()).foaled(horse.getFoaled())
                        .createdAt(horse.getCreatedAt()).build();
        if (horse.getModifiedAt() != null) {
            horseResponse.setModifiedAt(horse.getModifiedAt());
        }

        if (horse.getPrice() != null) {
            horseResponse.setPrice(horse.getPrice());
        }
        return horseResponse;
    }
}
