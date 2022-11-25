package com.nwt.juber.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DepositDetailsResponse {
    private String ethAddress;
    private BigDecimal weiToRsdRate;
}
