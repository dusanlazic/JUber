package com.nwt.juber.service;

import com.nwt.juber.dto.response.report.Averages;
import com.nwt.juber.dto.response.report.DailyData;
import com.nwt.juber.dto.response.report.ReportResponse;
import com.nwt.juber.dto.response.report.Sums;
import com.nwt.juber.model.*;
import com.nwt.juber.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ReportService {

    @Autowired
    private RideRepository rideRepository;

    public ReportResponse generateReport(Date startDate, Date endDate, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        LocalDate localStartDate = LocalDate.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDate localEndDate = LocalDate.ofInstant(endDate.toInstant(), ZoneId.systemDefault());

        List<LocalDate> dateRange = new ArrayList<>();
        while (!localStartDate.isAfter(localEndDate)) {
            dateRange.add(localStartDate);
            localStartDate = localStartDate.plusDays(1);
        }

        List<DailyData> days;
        if (user.getRole().equals(Role.ROLE_DRIVER)) {
            Driver driver = (Driver) authentication.getPrincipal();
            days = processDataForDriver(dateRange, driver);
        } else if (user.getRole().equals(Role.ROLE_PASSENGER)) {
            Passenger passenger = (Passenger) authentication.getPrincipal();
            days = processDataForPassenger(dateRange, passenger);
        } else { // Global
            days = processGlobalData(dateRange);
        }

        Sums sums = new Sums(
                days.stream().mapToInt(DailyData::getRides).sum(),
                days.stream().mapToDouble(DailyData::getDistance).sum(),
                days.stream().mapToDouble(DailyData::getFare).sum()
        );

        Averages averages = new Averages(
                days.stream().mapToInt(DailyData::getRides).average().orElse(0),
                days.stream().mapToDouble(DailyData::getDistance).average().orElse(0),
                days.stream().mapToDouble(DailyData::getFare).average().orElse(0)
        );

        return new ReportResponse(days, sums, averages);
    }

    private List<DailyData> processDataForDriver(List<LocalDate> dates, Driver driver) {
        return dates.stream().map(date -> {
            List<Ride> rides = rideRepository.getRidesForDriverAtDate(driver, date);
            return new DailyData(
                    date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    rides.size(),
                    rides.stream().mapToDouble(Ride::getDistance).sum(),
                    rides.stream().mapToDouble(this::getEarning).sum()
            );
        }).toList();
    }

    private List<DailyData> processDataForPassenger(List<LocalDate> dates, Passenger passenger) {
        return dates.stream().map(date -> {
            List<Ride> rides = rideRepository.getRidesForPassengerAtDate(passenger, date);
            return new DailyData(
                    date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    rides.size(),
                    rides.stream().mapToDouble(Ride::getDistance).sum(),
                    rides.stream().mapToDouble(this::getSpending).sum()
            );
        }).toList();
    }

    private List<DailyData> processGlobalData(List<LocalDate> dates) {
        return dates.stream().map(date -> {
            List<Ride> rides = rideRepository.getRidesAtDate(date);
            return new DailyData(
                    date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    rides.size(),
                    rides.stream().mapToDouble(Ride::getDistance).sum(),
                    rides.stream().mapToDouble(this::getEarning).sum()
            );
        }).toList();
    }

    private Double getSpending(Ride ride) {
        return ride.getFare() / ride.getPassengers().size();
    }

    private Double getEarning(Ride ride) {
        return ride.getFare();
    }
}
