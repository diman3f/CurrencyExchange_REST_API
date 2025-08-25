package org.pet.dto;

import lombok.*;

@Builder
@Getter
@Setter
public class CurrencyRequestDto {
    String name;
    String code;
    String sign;
}