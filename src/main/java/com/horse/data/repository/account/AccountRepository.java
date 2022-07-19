package com.horse.data.repository.account;


import com.horse.data.collection.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String>, AccountCustomRepository {

    Optional<Account> findByUsername(@NotBlank(message = "Username should not be blank") String username);
}