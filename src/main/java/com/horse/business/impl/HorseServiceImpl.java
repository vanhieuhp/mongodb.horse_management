package com.horse.business.impl;

import com.horse.business.service.HorseService;
import com.horse.data.dto.horse.HorseRequest;
import com.horse.data.dto.horse.HorseResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorseServiceImpl implements HorseService {

    @Override
    public List<HorseResponse> findAll() {
        return null;
    }

    @Override
    public List<HorseResponse> findByTrainerAndDate(Integer TrainerId, Integer year) {
        return null;
    }

    @Override
    public HorseResponse getOne(Integer id) {
        return null;
    }

    @Override
    public HorseResponse createHorse(HorseRequest horseRequest) {
        return null;
    }

    @Override
    public HorseResponse updateHorse(Integer id, HorseRequest horseRequest) {
        return null;
    }

    @Override
    public void deleteHorse(Integer id) {

    }
}
