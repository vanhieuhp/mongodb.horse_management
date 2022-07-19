package com.horse.data.repository.trainer;


import com.horse.data.collection.Trainer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrainerRepository extends MongoRepository<Trainer, String>, TrainerCustomRepository {
}