package com.globits.da.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class REST_POST_DTO {
    private String code;
    private String name;
    private Integer age;

    public REST_POST_DTO() {}

    public REST_POST_DTO(String code, String name, Integer age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }
}
