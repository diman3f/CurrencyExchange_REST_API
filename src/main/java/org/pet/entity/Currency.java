package org.pet.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class Currency {
    private Integer id;
    private String name;
    private String code;
    private String sign;

}
