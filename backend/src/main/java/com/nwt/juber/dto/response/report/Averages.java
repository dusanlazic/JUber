package com.nwt.juber.dto.response.report;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Averages {
    Double rides;
    Double distance;
    Double fare;
}
