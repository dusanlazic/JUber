package com.nwt.juber.dto.response.report;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReportResponse {
    List<DailyData> days;
    Sums sums;
    Averages averages;
}
