package com.horse.data.collection;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "account")
public class Account extends BaseCollection {

    private String username;
    private String password;
    private Trainer trainerInfo;
    private Set<String> hiredTrainers = new HashSet<>();
    private Role role;
    private Set<String> ownerHorses = new HashSet<>();

    public static Account getCurrentAccount() {
        return (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
