package com.horse.data.collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "role")
public class Role extends BaseCollection {

    private String name;
    private String code;

    public Role(String code, String name, Date createdAt) {
        this.name = name;
        this.code = code;
        this.setCreatedAt(createdAt);
    }
}