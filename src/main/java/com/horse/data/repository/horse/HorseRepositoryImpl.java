package com.horse.data.repository.horse;

import com.horse.data.collection.Horse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HorseRepositoryImpl implements HorseCustomRepository {


    @Override
    public List<Horse> findByTrainerIdAndYear(Integer trainerId, Integer year) {
        return null;
    }
}
