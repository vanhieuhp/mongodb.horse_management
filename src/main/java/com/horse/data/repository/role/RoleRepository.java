package com.horse.data.repository.role;


import com.horse.data.collection.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findRoleByCode(String code);

    Optional<Role> findRoleByName(String name);
}