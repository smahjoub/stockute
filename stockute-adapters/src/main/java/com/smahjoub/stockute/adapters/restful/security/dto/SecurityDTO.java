package com.smahjoub.stockute.adapters.restful.security.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SecurityDTO {

    private Long id;
    private String symbol;
    private String name;
    private String type;
    private String region;
    private String currency;
}