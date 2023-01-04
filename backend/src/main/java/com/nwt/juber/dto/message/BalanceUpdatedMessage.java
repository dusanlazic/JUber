package com.nwt.juber.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BalanceUpdatedMessage {

    private BigDecimal currentBalance;

    private BigDecimal increase;

}
