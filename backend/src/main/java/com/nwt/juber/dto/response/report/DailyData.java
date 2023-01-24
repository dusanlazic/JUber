package com.nwt.juber.dto.response.report;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyData {
    String date;
    Integer rides;
    Double distance;
    Double fare;
}
