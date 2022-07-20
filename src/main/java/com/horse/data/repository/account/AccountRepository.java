package com.horse.data.repository.account;


import com.horse.data.collection.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String>, AccountCustomRepository {

    Optional<Account> findByUsername(@NotBlank(message = "Username should not be blank") String username);

    @Query("{trainerInfo : {$ne: null}, 'role.code': ?0}")
    List<Account> findByRoleCode(String code);
}