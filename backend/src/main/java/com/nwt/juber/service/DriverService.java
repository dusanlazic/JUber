package com.nwt.juber.service;

import com.nimbusds.jose.util.Pair;
import com.nwt.juber.dto.SimulationInfo;
import com.nwt.juber.dto.message.PersonLocationMessage;
import com.nwt.juber.dto.request.AdditionalRideRequests;
import com.nwt.juber.dto.response.BriefDriverStatusResponse;
import com.nwt.juber.dto.response.DriverInfoResponse;
import com.nwt.juber.dto.response.PastRidesResponse;
import com.nwt.juber.dto.response.RideReviewResponse;
import com.nwt.juber.exception.UserNotFoundException;
import com.nwt.juber.model.Driver;
import com.nwt.juber.model.DriverStatus;
import com.nwt.juber.model.Ride;
import com.nwt.juber.repository.DriverRepository;
import com.nwt.juber.repository.RideRepository;
import com.nwt.juber.repository.RideReviewRepository;
import com.nwt.juber.util.MappingUtils;
import kotlin.NotImplementedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.nwt.juber.util.MappingUtils.getStartAndEndPlaceNames;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

	@Autowired
	private RideRepository rideRepository;

	@Autowired
	private RideReviewRepository rideReviewRepository;

    @Autowired
	private DriverShiftService driverShiftService;

    public List<SimulationInfo> getSimulationInfo() {
        List<Driver> drivers = driverRepository.findAll();
        return drivers
                .stream()
                .map(this::driverToSimulationInfo)
                .toList();
    }

    private SimulationInfo driverToSimulationInfo(Driver driver) {
        SimulationInfo dto = new SimulationInfo();
        dto.setUsername(driver.getUsername());
        dto.setLongitude(driver.getVehicle().getLongitude());
        dto.setLatitude(driver.getVehicle().getLatitude());
        Optional<Ride> rideToSim = driverRepository.findRideForSimulation(driver.getUsername());
        dto.setPlaces(rideToSim.map(Ride::getPlaces).orElse(null));
        dto.setStatus(rideToSim.map(Ride::getRideStatus).orElse(null));
        dto.setRideId(rideToSim.map(Ride::getId).orElse(null));
        return dto;
    }

    public void updateLocation(String username, Double longitude, Double latitude) {
        Driver driver = driverRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("No driver with username: " + username));
        driver.getVehicle().setLongitude(longitude);
        driver.getVehicle().setLatitude(latitude);
        driverRepository.save(driver);
    }
    
    public Driver findSuitableDriver(Ride ride, AdditionalRideRequests additionalRequsets) {
		throw new NotImplementedError("[!] findSuitableDriver(ride, additionalRequsets) not implemented ");
	}

	public Driver findByEmail(String email) {
		Optional<Driver> possibleDriver = driverRepository.findByEmail(email);
		if (possibleDriver.isEmpty()) {
			System.out.println("[!] User does not exist.");
			throw new UserNotFoundException("User does not exist.");
		}
		return possibleDriver.get();
	}

	@Transactional
	public Driver activateDriver(String driverEmail) {
		Driver driver = findByEmail(driverEmail);
		
		stopShiftsOver8h(driver);
		boolean isNewWorkingDay = resetShiftForNewDay(driver);
		
		if (driver.getStatus().equals(DriverStatus.INACTIVE)
				|| (driver.getStatus().equals(DriverStatus.OVERTIME) && isNewWorkingDay)) {

			driver.setStatus(DriverStatus.ACTIVE);
			driver = driverShiftService.startShift(driver);
			driver = driverRepository.save(driver);
		}

		return driver;
	}

	@Transactional
	public Driver inactivateDriver(String driverEmail) {
		Driver driver = findByEmail(driverEmail);

		if (driver.getStatus().equals(DriverStatus.ACTIVE)) {
			driver.setStatus(DriverStatus.INACTIVE);
			driver = driverShiftService.stopShift(driver);
			driverRepository.save(driver);
		}

		return driver;
	}

	@Transactional
	@Scheduled(cron = "0 * * * * *")
	public void checkAllDriverShifts() {
		List<Driver> drivers = driverRepository.findByStatus(DriverStatus.ACTIVE);
		for (Driver driver : drivers) {

			if(stopShiftsOver8h(driver)) {
				startOvertimeCountdown(driver);
			}
			resetShiftForNewDay(driver);

			driverRepository.save(driver);
		}
	}

	private boolean stopShiftsOver8h(Driver driver) {
		if (driverShiftService.isWorkingOver8Hours(driver)) {
			driver.setStatus(DriverStatus.OVERTIME);
			driver = driverShiftService.stopShift(driver);
			return true;
		}
		return false;
	}

	private boolean resetShiftForNewDay(Driver driver) {
		if (driverShiftService.isNewWorkDay(driver)) {
			driver.getDriverShifts().clear();
			return true;
		}
		return false;
	}

	private void startOvertimeCountdown(Driver driver) {
		// TODO: socket - set timer countdown
	}

    public List<PersonLocationMessage> getAllLocations() {
		return driverRepository.findAllLocations();
    }

	public PersonLocationMessage locationForDriverEmail(String email) {
		return driverRepository.locationForDriverEmail(email);
	}

	public PersonLocationMessage locationForDriverId(UUID id) {
		return driverRepository.locationForDriverId(id);
	}

    public List<BriefDriverStatusResponse> getAllBriefStatuses() {
		return driverRepository.findAll().stream().map(driver -> {
			Ride ride = rideRepository.getActiveRideForDriver(driver.getId());
			Pair<String, String> placeNames = getStartAndEndPlaceNames(ride);

			return new BriefDriverStatusResponse(
					driver.getId(),
					driver.getName(),
					driver.getStatus(),
					placeNames.getLeft(),
					placeNames.getRight()
			);
		}).toList();
    }

	public DriverInfoResponse getDriverInfo(UUID driverId) {
		Driver driver = driverRepository.findById(driverId).orElseThrow(UserNotFoundException::new);

		return new DriverInfoResponse(
				MappingUtils.convertPersonToDTO(driver),
				driver.getStatus()
		);
	}

	public List<PastRidesResponse> getDriversPastRides(UUID driverId) {
		Driver driver = driverRepository.findById(driverId).orElseThrow(UserNotFoundException::new);
		return driver.getRides().stream()
				.map(MappingUtils::convertPastRidesResponse)
				.sorted(Comparator.comparing(PastRidesResponse::getEndTimestamp).reversed())
				.toList();
	}

	public List<RideReviewResponse> getDriversReviews(UUID driverId) {
		return rideReviewRepository.getRideReviewsByDriverId(driverId)
				.stream().map(review -> new RideReviewResponse(
						review.getReviewer().getName(),
						review.getReviewer().getImageUrl(),
						review.getDriverRating(),
						review.getVehicleRating(),
						review.getComment()
				)).toList();
	}
}

