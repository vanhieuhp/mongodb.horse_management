package com.horse.data.dto.horse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class HorseResponse {

    private String id;
    private String name;
    private Date foaled;
    private Integer price;
    private Date createdAt;
    private Date modifiedAt;
}
