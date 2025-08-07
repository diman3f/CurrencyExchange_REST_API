package org.pet.dto;

import lombok.*;

@Builder
@Getter
@Setter
public class CurrencyDTO extends BaseDto {
    Integer id;
    String name;
    String code;
    String sign;

}


