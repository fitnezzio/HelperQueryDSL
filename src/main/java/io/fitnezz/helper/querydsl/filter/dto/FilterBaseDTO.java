package io.fitnezz.helper.querydsl.filter.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterBaseDTO {

    private int page;
    private int size;
    private String direction;
    private String sort;

}
