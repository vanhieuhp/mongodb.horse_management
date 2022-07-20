package com.horse.data.dto.trainer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TrainerInfoResponse {

    private String name;
    private Integer age;
    private String gender;
    private String address;

}
