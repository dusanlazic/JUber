package com.nwt.juber.controller;

import com.nwt.juber.dto.response.report.ReportResponse;
import com.nwt.juber.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping(value = "/report")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER', 'ADMIN')")
    public ReportResponse generateReport(@RequestParam("startDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
                                         @RequestParam("endDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,
                                         Authentication authentication) {
        return reportService.generateReport(startDate, endDate, authentication);
    }

}
