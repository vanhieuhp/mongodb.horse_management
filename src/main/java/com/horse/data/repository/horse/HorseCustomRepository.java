package com.horse.data.repository.horse;


import com.horse.data.collection.Horse;

import java.util.List;

public interface HorseCustomRepository {

    List<Horse> findByTrainerIdAndYear(Integer trainerId, Integer year);
}
