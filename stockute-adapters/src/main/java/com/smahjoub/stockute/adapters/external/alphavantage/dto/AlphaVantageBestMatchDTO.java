package com.smahjoub.stockute.adapters.external.alphavantage.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AlphaVantageBestMatchDTO {

    @JsonProperty("1. symbol")
    private String symbol;

    @JsonProperty("2. name")
    private String name;

    @JsonProperty("3. type")
    private String type;

    @JsonProperty("4. region")
    private String region;

    @JsonProperty("5. marketOpen")
    private String marketOpen;

    @JsonProperty("6. marketClose")
    private String marketClose;

    @JsonProperty("7. timezone")
    private String timezone;

    @JsonProperty("8. currency")
    private String currency;

    @JsonProperty("9. matchScore")
    private String matchScore;
}