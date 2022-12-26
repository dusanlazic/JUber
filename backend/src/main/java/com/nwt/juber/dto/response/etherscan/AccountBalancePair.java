package com.nwt.juber.dto.response.etherscan;

import lombok.Data;

@Data
public class AccountBalancePair {
    private String account;
    private String balance;
}
