package com.horse.data.collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "account")
public class Account extends BaseCollection {

    private String username;
    private String password;
    private Trainer trainerInfo;
    private Set<String> trainers;
    private Role role;
    private Set<String> horses;

    public Account(String username, String password, Role role, Date createdAt) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.setCreatedAt(createdAt);
    }
}
