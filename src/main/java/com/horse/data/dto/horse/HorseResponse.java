package com.horse.data.dto.horse;

import com.horse.data.dto.ResponseDtoModel;
import lombok.*;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Builder
public class HorseResponse {

    private String id;
    private String name;
    private Date foaled;
    private Integer price;
    private Map<String, String> trainerOfHorse;
    private Date createdAt;
    private Date modifiedAt;
}
