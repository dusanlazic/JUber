package com.nwt.juber.service;

import com.nwt.juber.dto.DriverRideDTO;
import com.nwt.juber.dto.RideDTO;
import com.nwt.juber.dto.message.RideMessage;
import com.nwt.juber.dto.message.RideMessageType;
import com.nwt.juber.dto.request.RideRequest;
import com.nwt.juber.dto.response.PastRidesResponse;
import com.nwt.juber.dto.response.report.Averages;
import com.nwt.juber.dto.response.report.DailyData;
import com.nwt.juber.dto.response.report.ReportResponse;
import com.nwt.juber.dto.response.report.Sums;
import com.nwt.juber.exception.EndRideException;
import com.nwt.juber.exception.InsufficientFundsException;
import com.nwt.juber.exception.StartRideException;
import com.nwt.juber.exception.UserNotFoundException;
import com.nwt.juber.model.*;
import com.nwt.juber.model.notification.*;
import com.nwt.juber.repository.*;
import com.nwt.juber.util.MappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.nwt.juber.util.MappingUtils.convertRideToDTO;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskScheduling taskScheduling;

    @Autowired
    TimeEstimator timeEstimator;

    @Autowired
    private RideCancellationRepository rideCancellationRepository;

    public void startRide(UUID rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new StartRideException("No ride with id: " + rideId));
        if (ride.getRideStatus() != RideStatus.ACCEPTED) {
            throw new StartRideException("Ride not accepted!");
        }
        ride.setRideStatus(RideStatus.IN_PROGRESS);
        ride.setStartTime(LocalDateTime.now());
        rideRepository.save(ride);
    }

    public void endRide(UUID rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new EndRideException("No ride with id: " + rideId));
        if (ride.getRideStatus() != RideStatus.IN_PROGRESS)  {
            throw new StartRideException("Ride not in progress!");
        }
        ride.setRideStatus(RideStatus.FINISHED);
        ride.setEndTime(LocalDateTime.now());
        sendRideMessageToPassengers(ride, RideMessageType.RIDE_FINISHED);
        rideRepository.save(ride);
        // check if future should be accepted
        findAndAcceptScheduledRide(ride.getDriver());
    }

    private void findAndAcceptScheduledRide(Driver driver) {
        Ride ride = rideRepository.getScheduledRideForDriver(driver);
        if (ride != null) {
            startScheduledRide(ride.getId());
        }
    }

    public void createRideRequest(RideRequest rideRequest, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Passenger passenger = passengerRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("No passenger found!"));
        Ride ride = new Ride();
        List<Passenger> pass = new ArrayList<>();
        pass.add(passenger);
        List<PassengerStatus> ready = new ArrayList<>();
        ready.add(PassengerStatus.Ready);
        for (String email: rideRequest.getPassengerEmails()) {
            passengerRepository.findByEmail(email).ifPresent(pass::add);
            ready.add(PassengerStatus.Waiting);
        }
        ride.setPassengers(pass);
        ride.setPassengersReady(ready);
        ride.setRideStatus(RideStatus.WAITING_FOR_PAYMENT);
        ride.setStartTime(rideRequest.getRide().getStartTime());
        ride.setEndTime(rideRequest.getRide().getEndTime());
        ride.setScheduledTime(parseScheduledTime(rideRequest.getScheduleTime()));
        ride.setPlaces(rideRequest.getRide().getPlaces());
        ride.setFare(rideRequest.getRide().getFare());
        ride.setDuration(rideRequest.getRide().getDuration());
        ride.setDistance(rideRequest.getRide().getDistance());
        ride.getPlaces().forEach(place -> routeRepository.saveAll(place.getRoutes()));
        ride.getPlaces().forEach(place -> place.setId(UUID.randomUUID()));
        placeRepository.saveAll(ride.getPlaces());
        System.out.println(ride);
        rideRepository.save(ride);
        for (String email: rideRequest.getPassengerEmails()) {
            sendRideInvitation(ride, email, passenger);
        }

        if (rideRequest.getPassengerEmails().size() == 0) {
            ride.setRideStatus(RideStatus.WAIT);
            assignSuitableDriverWhenNeeded(ride);
        }
        rideRepository.save(ride);

    }

    private void assignSuitableDriverWhenNeeded(Ride ride) {
        if (ride.getScheduledTime() != null) {
            if(ride.getScheduledTime().isAfter(LocalDateTime.now())) {
                assignSuitableDriver(ride);
            } else {
                // ride failed!
                RideMessageType type = RideMessageType.RIDE_FAILED_LATE;
                sendRideMessageToPassengers(ride, type);
                ride.setRideStatus(RideStatus.DENIED);
                rideRepository.save(ride);
            }
        } else {
            assignSuitableDriver(ride);
        }
    }

    private void startScheduledRide(UUID rideId) {
        Ride ride = rideRepository.findById(rideId).get();
        Driver driver = ride.getDriver();
        Ride activeRide = rideRepository.getActiveRideForDriver(driver.getId());
        if (activeRide == null) {
            ride.setRideStatus(RideStatus.ACCEPTED);
            rideRepository.save(ride);
            // notify them
            RideStatusUpdatedNotification notification = new RideStatusUpdatedNotification();
            notification.setRide(ride);
            notification.setId(UUID.randomUUID());
            notification.setReceiver(ride.getDriver());
            notification.setStatus(NotificationStatus.UNREAD);
            notificationService.send(notification, ride.getDriver());
        }
    }

    private void sendRideMessageToPassengers(Ride ride, RideMessageType type) {
        RideMessage rideMessage = new RideMessage();
        rideMessage.setRide(convertRideToDTO(ride));
        rideMessage.setType(type);
        for (Passenger pal: ride.getPassengers()) {
            messagingTemplate.convertAndSendToUser(pal.getUsername(), "/queue/ride", rideMessage);
        }
    }

    private LocalDateTime parseScheduledTime(String scheduledTime) {
        LocalTime parsedTime;

        if (scheduledTime.isEmpty() || scheduledTime.isBlank()) {
            return null;
        }

        try {
            parsedTime = LocalTime.parse(scheduledTime);
        } catch (Exception e) {
            return null;
        }

        LocalDateTime parsedDateTime = parsedTime.atDate(LocalDate.now());
        if (parsedDateTime.isBefore(LocalDateTime.now())) {
            parsedDateTime = parsedDateTime.plusDays(1);
        }
        return parsedDateTime;
    }

    private void sendRideInvitation(Ride ride, String email, Passenger inviter) {
        System.out.println("Sending invitation to: " + email);
        Passenger passenger = passengerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("No pal found!"));
        RideInvitationNotification notification = new RideInvitationNotification();
        notification.setId(UUID.randomUUID());
        notification.setInviter(inviter);
        notification.setInvitee(passenger);
        notification.setRide(ride);
        notification.setCreated(new Date());
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setBalance(passenger.getBalance().doubleValue());
        System.out.println(notification);
        notificationService.send(notification, passenger);
        passengerRepository.save(passenger);
    }

    public void acceptRideDriver(Driver driver, UUID rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new EndRideException("No ride with id: " + rideId));
        ride.setDriver(driver);
        if (ride.getScheduledTime() != null) {
            ride.setRideStatus(RideStatus.SCHEDULED);
            taskScheduling.scheduling(() -> startScheduledRide(ride.getId()), ride.getScheduledTime());
            notifyForScheduledRide(ride);
            startReminderCycle(ride);
        } else {
            Ride activeRide = rideRepository.getActiveRideForDriver(driver.getId());
            if(activeRide.getRideStatus().equals(RideStatus.ACCEPTED) || activeRide.getRideStatus().equals(RideStatus.IN_PROGRESS)) {
                ride.setStartTime(LocalDateTime.now().plusSeconds(activeRide.getDuration()));
                ride.setRideStatus(RideStatus.SCHEDULED);
                notifyForScheduledRide(ride);
                startReminderCycle(ride);
            } else {
                ride.setRideStatus(RideStatus.ACCEPTED);
            }
        }
        rideRepository.save(ride);
        // notify everyone
        sendRideMessageToPassengers(ride, RideMessageType.DRIVER_FOUND);
    }

    private void startReminderCycle(Ride ride) {
        taskScheduling.scheduling(() -> sendReminders(ride.getId(), ride.getPlaces().get(0).getName()), LocalDateTime.now().plusMinutes(2));
    }

    public void notifyForScheduledRide(Ride ride) {
        NewRideAssignedNotification notification = new NewRideAssignedNotification();
        notification.setId(UUID.randomUUID());
        notification.setRide(ride);
        notification.setCreated(new Date());
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setReceiver(ride.getDriver());
        notificationService.send(notification, ride.getDriver());
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void sendReminders(UUID rideId, String startingLocation) {
        Ride ride = rideRepository.findById(rideId).get();
        if(!ride.getRideStatus().equals(RideStatus.SCHEDULED)) return;
        RideReminderNotification notification = new RideReminderNotification();
        notification.setId(UUID.randomUUID());
        notification.setCreated(new Date());
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setReceiver(ride.getDriver());
        notification.setRide(ride);
        notification.setStartTime(ride.getScheduledTime());
        notification.setStartingLocation(startingLocation);
        notificationService.send(notification, ride.getDriver());
        taskScheduling.scheduling(() -> sendReminders(ride.getId(), startingLocation), LocalDateTime.now().plusMinutes(2));

    }

    public void acceptRidePassenger(Passenger passenger, UUID rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new EndRideException("No ride with id: " + rideId));
        BigDecimal fare = BigDecimal.valueOf(ride.getFare() / ride.getPassengers().size());

        if (passenger.getBalance().compareTo(fare) < 0) {
            // insufficent funds
            throw new InsufficientFundsException("Not enough funds!");
        }

        int passengerPosition = ride.getPassengers().indexOf(passenger);
        ride.getPassengersReady().set(passengerPosition, PassengerStatus.Ready);

        sendRideMessageToPassengers(ride, RideMessageType.PAL_UPDATE_STATUS);

        // look for a driver if everybody has accepted!
        if (ride.getPassengersReady().stream().allMatch(x -> x == PassengerStatus.Ready)) {
            ride.setRideStatus(RideStatus.WAIT);
            rideRepository.save(ride);
            assignSuitableDriverWhenNeeded(ride);
        }
    }

    public void acceptRide(UUID rideId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<Passenger> passenger = passengerRepository.findById(user.getId());
        if(passenger.isPresent()) {
            acceptRidePassenger(passenger.get(), rideId);
        } else {
            Driver driver = driverRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("No driver found!"));
            acceptRideDriver(driver, rideId);
        }
    }

    private void assignSuitableDriver(Ride ride) {
        System.out.println("Assigning suitable driver at: " + LocalDateTime.now());
        Driver driver = findSuitableDriver(ride);
        if (driver == null) {
            ride.setRideStatus(RideStatus.DENIED);
            rideRepository.save(ride);
            throw new RuntimeException("No driver found!");
        }
        ride.setDriver(driver);
        ride.setRideStatus(RideStatus.WAIT);
        RideMessage rideMessage = new RideMessage();
        rideMessage.setRide(convertRideToDTO(ride));
        rideMessage.setType(RideMessageType.DRIVER_FOUND);
        messagingTemplate.convertAndSendToUser(ride.getDriver().getUsername(), "/queue/ride", rideMessage);
        rideRepository.save(ride);
    }

    public Driver findSuitableDriver(Ride ride) {
        Driver closestAvailable = findClosestAvailableDriver(ride);
        return closestAvailable != null ? closestAvailable : findFastestUnavailable(ride);
    }

    private Driver findClosestAvailableDriver(Ride ride) {
        // Could possibly have status of SCHEDULED but no WAIT, ACCEPTED, IN PROGRESS
        List<Driver> drivers = driverRepository.findAvailableDrivers(ride);
        double startLat = ride.getPlaces().get(0).getLatitude();
        double startLon = ride.getPlaces().get(0).getLongitude();
        return drivers
                .stream()
                .min(Comparator.comparingDouble(driver -> {
                    double driverLat = driver.getVehicle().getLatitude();
                    double driverLon = driver.getVehicle().getLongitude();
                    return geoDistance(startLat, startLon, driverLat, driverLon);
                }))
                .orElse(null);
    }

    public static double geoDistance(double startLatitude, double startLongitude,
                                     double endLatitude, double endLongitude) {
        double earthRadius = 6371000; // meters
        double dLat = Math.toRadians(endLatitude - startLatitude);
        double dLon = Math.toRadians(endLongitude - startLongitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(startLatitude)) * Math.cos(Math.toRadians(endLatitude)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    private Driver findFastestUnavailable(Ride ride) {
        List<DriverRideDTO> drivers = driverRepository.findUnavailableDriversWithNoFutureRides(ride);
        DriverRideDTO minDriverRide = drivers
                                    .stream()
                                    .min(Comparator.comparing(x -> compareDriverEstimatedTimes(x, ride)))
                                    .orElse(null);
        return minDriverRide != null ? minDriverRide.getDriver() : null;
    }

    private double compareDriverEstimatedTimes(DriverRideDTO driverRideDTO, Ride ride) {
        Integer estimatedTime = ride.getDuration();
        if (ride.getStartTime() != null) {
            double secondsFromStart = ChronoUnit.SECONDS.between(LocalTime.now(), ride.getStartTime());
            double secondsEstimated = estimatedTime;
            return secondsEstimated - secondsFromStart;
        } else {
            double driverLat = driverRideDTO.getDriver().getVehicle().getLatitude();
            double driverLon = driverRideDTO.getDriver().getVehicle().getLongitude();
            double startLat = ride.getPlaces().get(0).getLatitude();
            double startLon = ride.getPlaces().get(0).getLongitude();

            double toStart = timeEstimator.estimateTime(driverLat, driverLon, startLat, startLon);
            double toFinish = estimatedTime;
            return toStart + toFinish;
        }

    }

    private void subtractFundsForRide(Ride ride) {
        double fare = ride.getFare() / ride.getPassengers().size();
        ride.getPassengers()
                .forEach(x -> x.setBalance(x.getBalance().subtract(BigDecimal.valueOf(fare))));
    }

    public void declineRidePassenger(Passenger passenger, UUID rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new EndRideException("No ride with id: " + rideId));
        int passengerPosition = ride.getPassengers().indexOf(passenger);
        ride.getPassengersReady().set(passengerPosition, PassengerStatus.Declined);
        ride.setRideStatus(RideStatus.DENIED);
        // notify the rest!
        sendRideMessageToPassengers(ride, RideMessageType.PAL_UPDATE_STATUS);
        rideRepository.save(ride);
    }


    public void declineRideDriver(Driver driver, UUID rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new EndRideException("No ride with id: " + rideId));
        ride.setRideStatus(RideStatus.DENIED);
        rideRepository.save(ride);
        assignSuitableDriver(ride);
    }

    public void declineRide(UUID rideId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<Passenger> passenger = passengerRepository.findById(user.getId());
        if(passenger.isPresent()) {
            declineRidePassenger(passenger.get(), rideId);
        } else {
            Driver driver = driverRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("No driver found!"));
            declineRideDriver(driver, rideId);
        }
    }

    public RideDTO getActiveRide(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<Passenger> optionalPassenger = passengerRepository.findById(user.getId());
        Ride ride;
        if (optionalPassenger.isEmpty()) {
            Driver driver = driverRepository.findById(user.getId()).orElseThrow();
            ride = rideRepository.getActiveRideForDriver(driver.getId());
        } else {
            Passenger passenger = optionalPassenger.get();
            ride = rideRepository.getActiveRideForPassenger(passenger);
        }
        // convert to dto
        return ride == null? null : convertRideToDTO(ride);
    }

    public RideDTO getRide(UUID rideId, Authentication authentication) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RuntimeException("No ride found!"));
        User user = (User) authentication.getPrincipal();
        if (!ride.getDriver().getId().equals(user.getId()) && ride.getPassengers().stream().map(Person::getId).noneMatch(x -> x.equals(user.getId()))) {
            throw new RuntimeException("You are not allowed to see this ride!");
        }
        return convertRideToDTO(ride);
    }

    public void toggleFavourite(UUID rideId, Authentication authentication) {
        Passenger passenger = passengerRepository.findById(((User) authentication.getPrincipal()).getId()).orElseThrow();
        Ride ride = rideRepository.findById(rideId).orElseThrow();
        if (!ride.getPassengers().contains(passenger)) {
            throw new RuntimeException("You are not allowed to toggle favourite for this ride!");
        }
        if (passenger.getFavouriteRoutes().contains(ride)) {
            passenger.getFavouriteRoutes().remove(ride);
        } else {
            passenger.getFavouriteRoutes().add(ride);
        }
        passengerRepository.save(passenger);
    }

    public boolean checkIfFavourite(UUID rideId, Authentication authentication) {
        Passenger passenger = passengerRepository.findById(((User) authentication.getPrincipal()).getId()).orElseThrow();
        Ride ride = rideRepository.findById(rideId).orElseThrow();
        if (!ride.getPassengers().contains(passenger)) {
            throw new RuntimeException("You are not allowed to toggle favourite for this ride!");
        }
        return passenger.getFavouriteRoutes().contains(ride);
    }

	public void abandonRide(UUID rideId, String reason, Authentication authentication) {
        Ride ride = rideRepository.findById(rideId).orElseThrow();
        Driver driver = driverRepository.findById(((User) authentication.getPrincipal()).getId()).orElseThrow();
        if (!ride.getDriver().getId().equals(driver.getId())) {
            throw new RuntimeException("You are not allowed to abandon this ride!");
        }
        ride.setRideStatus(RideStatus.DENIED);
        rideRepository.save(ride);
        RideCancellation rideCancellation = new RideCancellation();
        rideCancellation.setId(UUID.randomUUID());
        rideCancellation.setRide(ride);
        rideCancellation.setReason(reason);
        rideCancellationRepository.save(rideCancellation);
        sendRideMessageToPassengers(ride, RideMessageType.DRIVER_ABANDONED);
	}

    public List<PastRidesResponse> getPastRides(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return getPastRides(user);
    }

	public List<PastRidesResponse> getPastRides(User user) {
		List<Ride> pastRides = new ArrayList<Ride>();
		if(user.getRole().equals(Role.ROLE_DRIVER)) {
			Driver driver = driverRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
			pastRides = rideRepository.getPastRidesForDriver(driver);
		}
		else if(user.getRole().equals(Role.ROLE_PASSENGER)) {
			Passenger passenger = passengerRepository.findById(user.getId()).get();
			pastRides = rideRepository.getPastRidesForPassenger(passenger);
		}
        return pastRides.stream().map(MappingUtils::convertPastRidesResponse).toList();
	}

    public List<RideDTO> getSavedRoutes(Passenger passenger) {
        // TODO: Find saved routes instead of all
        return rideRepository.findAll().stream().map(MappingUtils::convertRideToDTO).toList();
    }

	public Ride getPastRideById(UUID rideId) {
		return rideRepository.findById(rideId).orElseThrow(() -> new EndRideException("No finished ride with id: " + rideId));
	}

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
