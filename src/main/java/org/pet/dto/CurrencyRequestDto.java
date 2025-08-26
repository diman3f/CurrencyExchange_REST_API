package org.pet.dto;

import lombok.*;

@Builder
@Getter
@Setter
public class CurrencyRequestDto {
    private String name;
    private String code;
    private String sign;
}