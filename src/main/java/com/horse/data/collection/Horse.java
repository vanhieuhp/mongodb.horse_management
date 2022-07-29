package com.horse.data.collection;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "horse")
public class Horse{

    @Id
    private String id;
    private Date createdAt;
    private Date modifiedAt;
    private Date foaled;
    private String name;
    private Integer price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Horse horse = (Horse) o;
        return Objects.equals(id, horse.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


