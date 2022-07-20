package com.horse.data.collection;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "horse")
public class Horse extends BaseCollection{

    private Date foaled;
    private String name;
    private Integer price;
    private Map<String, String> trainerOfHorse = new HashMap<>();
}
