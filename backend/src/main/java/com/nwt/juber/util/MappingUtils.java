package com.nwt.juber.util;

import com.nimbusds.jose.util.Pair;
import com.nwt.juber.dto.PersonDTO;
import com.nwt.juber.dto.RideDTO;
import com.nwt.juber.dto.request.AdditionalRideRequests;
import com.nwt.juber.dto.request.RideRequest;
import com.nwt.juber.dto.request.ride.*;
import com.nwt.juber.dto.response.PastRidesResponse;
import com.nwt.juber.model.*;
import org.hibernate.Hibernate;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Transactional
public class MappingUtils {


    public static RideDTO convertRideToDTO(Ride ride) {
        RideDTO dto = new RideDTO();
        dto.setId(ride.getId());
        dto.setFare(ride.getFare());
        dto.setPlaces(ride.getPlaces());
        dto.setPassengers(ride.getPassengers().stream().map(MappingUtils::convertPersonToDTO).toList());
        dto.setRideStatus(ride.getRideStatus());
        dto.setPassengersReady(ride.getPassengersReady());
        dto.setDriver(convertPersonToDTO(ride.getDriver()));
        dto.setScheduledTime(ride.getScheduledTime());
        dto.setStartTime(ride.getStartTime());
        dto.setEndTime(ride.getEndTime());
        Hibernate.initialize(dto.getPlaces());
        Hibernate.initialize(dto.getPassengers());
        Hibernate.initialize(dto.getPassengersReady());
        dto.getPlaces().forEach(place -> Hibernate.initialize(place.getRoutes()));
        return dto;
    }

    public static PersonDTO convertPersonToDTO(Person person) {
        if (person == null) {
            return null;
        }
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(person.getId());
        personDTO.setFirstName(person.getFirstName());
        personDTO.setLastName(person.getLastName());
        personDTO.setPhoneNumber(person.getPhoneNumber());
        personDTO.setImageUrl(person.getImageUrl());
        personDTO.setEmail(person.getEmail());
        personDTO.setCity(person.getCity());
        return personDTO;
    }

    public static PastRidesResponse convertPastRidesResponse(Ride ride) {
        String startPlaceName = "";
        String endPlaceName = "";

        if(ride.getPlaces().size() > 0) {
            startPlaceName = ride.getPlaces().get(0).getName();
            endPlaceName = ride.getPlaces().get(ride.getPlaces().size() - 1).getName();
        }
        String formattedDate = ride.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));
        String startTime = ride.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String endTime = ride.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        Long endTimestamp = ride.getEndTime().atZone(ZoneId.systemDefault()).toEpochSecond();

        return new PastRidesResponse(ride.getId(), startPlaceName, endPlaceName, formattedDate, startTime, endTime, ride.getFare(), endTimestamp);
    }

    public static Pair<String, String> getStartAndEndPlaceNames(Ride ride) {
        String startPlaceName = "";
        String endPlaceName = "";

        if(ride != null && ride.getPlaces().size() > 0) {
            startPlaceName = ride.getPlaces().get(0).getName();
            endPlaceName = ride.getPlaces().get(ride.getPlaces().size() - 1).getName();
        }

        return Pair.of(startPlaceName, endPlaceName);
    }

    public static RideRequest convertRideRequestDTOtoEntity(RideRequestDTO rideRequest) {
        return new RideRequest(
                mapRideDTOtoEntity(rideRequest.getRide()),
                mapAdditionalRideRequestsDTOtoEntity(rideRequest.getAdditionalRequests()),
                rideRequest.getScheduleTime(),
                rideRequest.getPassengerEmails()
        );
    }

    private static VehicleType mapVehicleDTOtoEntity(VehicleTypeDTO dto) {
        return new VehicleType(
                dto.getId(),
                dto.getName(),
                dto.getPrice()
        );
    }

    private static AdditionalRideRequests mapAdditionalRideRequestsDTOtoEntity(AdditionalRideRequestsDTO dto) {
        return new AdditionalRideRequests(
                dto.isBabyFriendly(),
                dto.isPetFriendly(),
                mapVehicleDTOtoEntity(dto.getVehicleType())
        );
    }

    private static Route mapRouteDTOtoEntity(RouteDTO dto) {
        return new Route(
                dto.getName(),
                dto.getDistance(),
                dto.getDuration(),
                dto.getCoordinatesEncoded(),
                dto.getSelected()
        );
    }

    private static Place mapPlaceDTOtoEntity(PlaceDTO dto) {
        return new Place(
                dto.getName(),
                dto.getOption(),
                dto.getRoutes().stream().map(MappingUtils::mapRouteDTOtoEntity).toList(),
                dto.getLatitude(),
                dto.getLongitude()
        );
    }

    private static Ride mapRideDTOtoEntity(com.nwt.juber.dto.request.ride.RideDTO dto) {
        return new Ride(
                dto.getPlaces().stream().map(MappingUtils::mapPlaceDTOtoEntity).toList(),
                dto.getFare(),
                dto.getDuration(),
                dto.getDistance()
        );
    }
}
