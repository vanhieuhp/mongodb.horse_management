package com.horse.data.repository.horse;

import com.horse.data.collection.Horse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HorseRepository extends MongoRepository<Horse, String>, HorseCustomRepository {

}