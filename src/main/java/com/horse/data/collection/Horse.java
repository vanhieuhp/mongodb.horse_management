package com.horse.data.collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "horse")
public class Horse {

    @Id
    private String id;

    private Date foaled;
    private String name;
    private Integer price;
    private Trainer trainer;
}
