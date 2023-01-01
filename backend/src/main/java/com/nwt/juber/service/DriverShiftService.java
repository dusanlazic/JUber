package com.nwt.juber.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nwt.juber.exception.DriverShiftNotFoundException;
import com.nwt.juber.model.Driver;
import com.nwt.juber.model.DriverShift;
import com.nwt.juber.model.DriverStatus;

@Service
public class DriverShiftService {
	
	private final Long maxMiliseconds = 28800000L;  // 8h
	private final Long dayMiliseconds = 86400000L;  // 24h

	public Driver startShift(Driver driver) {
		Timestamp now = new Timestamp(System.currentTimeMillis());

		DriverShift driverShift = new DriverShift(now);
		driverShift.setId(UUID.randomUUID());
		driver.getDriverShifts().add(driverShift);
		return driver;
	}
	
	public Driver stopShift(Driver driver) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		DriverShift lastShift = getLastShift(driver);
		if(lastShift == null) {
			throw new DriverShiftNotFoundException();
		}
		if(lastShift.getEndOfShift() == null) {
			lastShift.setEndShift(now);
		}

		return driver;
	}

	public boolean isWorkingOver8Hours(Driver driver) {
		DriverShift lastShift = getLastShift(driver);
		if(lastShift == null) {
			return false;
		}
		Long sumHours = sumHours(driver.getDriverShifts());
		
		if(lastShift.getEndOfShift() == null && driver.getStatus() == DriverStatus.ACTIVE) {
			Long nowMs = new Timestamp(System.currentTimeMillis()).getTime();
			Long lastShiftStartMs = lastShift.getStartOfShift().getTime();
			sumHours += nowMs - lastShiftStartMs;
		}

		return sumHours  > maxMiliseconds;
	}

	public boolean isNewWorkDay(Driver driver) {
		Timestamp now = new Timestamp(System.currentTimeMillis());

		DriverShift firstShift = getFirstShift(driver);
		if(firstShift == null) {
			return false;
		}
		Long firstShiftStartMs = firstShift.getStartOfShift().getTime();
		return (firstShiftStartMs + dayMiliseconds) <= now.getTime();
	}

	private DriverShift getFirstShift(Driver driver) {
		List<DriverShift> driverShift = driver.getDriverShifts();
		if(driverShift.size() > 0) {
			return driverShift.get(0);
		}
		return null;
	}

	private DriverShift getLastShift(Driver driver) {
		List<DriverShift> driverShift = driver.getDriverShifts();
		if(driverShift.size() > 0) {
			return driverShift.get(driverShift.size() - 1);
		}
		return null;
	}

	private Long sumHours(List<DriverShift> driverShifts) {
		if(driverShifts.size()>0) {
			return driverShifts.stream().collect(Collectors.summingLong(shift -> shift.getDuration()));
		}
		return 0L;
	}
}
