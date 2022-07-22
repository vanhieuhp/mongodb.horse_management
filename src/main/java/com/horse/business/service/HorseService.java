package com.horse.business.service;

import com.horse.data.collection.Horse;
import com.horse.data.dto.horse.HorseRequest;
import com.horse.data.dto.horse.HorseResponse;

import java.util.List;

public interface HorseService {

    List<HorseResponse> findAll();
    List<HorseResponse> findByTrainerAndYear(String trainerId, Integer year);
    HorseResponse findOne(String id);
    HorseResponse createHorse(HorseRequest horseRequest);
    HorseResponse updateHorse(String id, HorseRequest horseRequest);
    void deleteHorse(String id);
    List<Horse> findAllByPrice(Integer price);
}
