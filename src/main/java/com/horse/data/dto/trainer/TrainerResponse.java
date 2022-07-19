package com.horse.data.dto.trainer;

import com.horse.data.dto.AbstractResponseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class TrainerResponse extends AbstractResponseDTO {

    private String name;
    private Integer age;
    private String gender;
    private String address;

    private List<String> horses;

    public TrainerResponse(String id, String name, Integer age, String gender, String address, Date createdAt) {
        this.setId(id);
        this.setName(name);
        this.setAge(age);
        this.setGender(gender);
        this.setAddress(address);
        this.setCreatedAt(createdAt);
    }
}
