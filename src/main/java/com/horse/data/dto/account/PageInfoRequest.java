package com.horse.data.dto.account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class PageInfoRequest {

    @NotNull
    private int pageNumber;

    @NotNull
    private int pageSize;

    @NotBlank
    private String sortBy;
}
