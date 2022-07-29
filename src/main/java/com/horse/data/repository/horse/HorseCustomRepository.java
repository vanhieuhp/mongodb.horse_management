package com.horse.data.repository.horse;


import com.horse.data.collection.Account;
import com.horse.data.collection.Horse;
import com.horse.data.dto.horse.HorseRequest;

import java.util.List;

public interface HorseCustomRepository {

    List<Horse> findByTrainerAndYear(Account trainer, Integer year);
    List<Horse> findByYear(Integer year);
    List<Horse> findByTrainer(Account trainer);
    List<Horse> findByPrice(Integer price);
}
