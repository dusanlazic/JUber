package com.nwt.juber.dto.response.cryptocompare;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PriceResponse {
    @JsonProperty("RSD")
    private Float value;
}
