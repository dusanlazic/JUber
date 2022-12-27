package com.nwt.juber.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nwt.juber.model.Driver;
import com.nwt.juber.model.DriverShift;

@Service
public class DriverShiftService {
	
	private final Long maxMiliseconds = 28800000L;  // 8h
	private final Long dayMiliseconds = 86400000L;  // 24h

	public Driver startShift(Driver driver) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		DriverShift driverShift = new DriverShift(now);
		driver.getDriverShifts().add(driverShift);
		return driver;
	}
	
	public Driver stopShift(Driver driver) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		DriverShift lastShift = getLastShift(driver);
		lastShift.setEndShift(now);
		return driver;
	}
	
	public DriverShift getLastShift(Driver driver) {
		List<DriverShift> driverShift = driver.getDriverShifts();
		return driverShift.get(driverShift.size() - 1);
	}

	public boolean isWorkingOver8Hours(Driver driver) {
		Long nowMs = new Timestamp(System.currentTimeMillis()).getTime();
		
		Long lastShiftStartMs = getLastShift(driver).getStartShift().getTime();
		Long sumHours = sumHours(driver.getDriverShifts());
		
		sumHours += nowMs - lastShiftStartMs;
		return sumHours  > maxMiliseconds;
	}
	
	private Long sumHours(List<DriverShift> driverShifts) {
		return driverShifts.stream().collect(Collectors.summingLong(shift -> shift.getDuration()));
	}

	public boolean isNewWorkDay(Driver driver) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		DriverShift firstShift = driver.getDriverShifts().get(0);
		Long firstShiftStartMs = firstShift.getStartShift().getTime();
		return (firstShiftStartMs + dayMiliseconds) >= now.getTime();
	}
}
