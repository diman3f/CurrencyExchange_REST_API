package org.pet.dto;

import lombok.*;

@Builder
@Getter
@Setter
public class CurrencyDTO {
    Integer id;
    String name;
    String code;
    String sign;

}


