package com.nwt.juber.dto.response.etherscan;

import lombok.Data;

import java.util.List;

@Data
public class BalancesResponse {
    private List<AccountBalancePair> result;
}
