package com.horse.data.collection;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trainer{

    private String name;
    private Integer age;
    private String gender;
    private String address;
    private Set<String> trainerHorses = new HashSet<>();
}
