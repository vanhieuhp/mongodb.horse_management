package com.horse.data.repository.horse;


import com.horse.data.collection.Horse;
import com.horse.data.dto.horse.HorseRequest;

import java.util.List;

public interface HorseCustomRepository {

    List<Horse> findByTrainerIdAndYear(String trainerId, Integer year);
    Horse findAllByName(Horse horse, String id);
    List<Horse> findAllByPrice(Integer price);
}
