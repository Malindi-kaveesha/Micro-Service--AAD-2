package com.spms.parking.service;

import com.spms.parking.client.PaymentClient;
import com.spms.parking.client.UserClient;
import com.spms.parking.client.VehicleClient;
import com.spms.parking.dto.PaymentDTOs.PaymentRequest;
import com.spms.parking.dto.PaymentDTOs.PaymentResponse;
import com.spms.parking.entity.ParkingSpace;
import com.spms.parking.entity.Reservation;
import com.spms.parking.exception.ParkingSpaceNotFoundException;
import com.spms.parking.exception.ParkingSpaceOccupiedException;
import com.spms.parking.exception.ReservationNotFoundException;
import com.spms.parking.repository.ParkingSpaceRepository;
import com.spms.parking.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingService {

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private VehicleClient vehicleClient;

    @Autowired
    private PaymentClient paymentClient;

    public ParkingSpace addParkingSpace(ParkingSpace space) {
        return parkingSpaceRepository.save(space);
    }

    public List<ParkingSpace> listParkingSpaces(String location, Boolean isOccupied) {
        if (location != null && isOccupied != null) {
            return parkingSpaceRepository.findByLocationAndIsOccupied(location, isOccupied);
        } else if (location != null) {
            return parkingSpaceRepository.findByLocation(location);
        } else if (isOccupied != null) {
            return parkingSpaceRepository.findByIsOccupied(isOccupied);
        }
        return parkingSpaceRepository.findAll();
    }

    public Reservation reserveSpace(Long spaceId, Long vehicleId, Long userId) {
        // 1. Verify User Exists via Feign Client
        Boolean userExists = false;
        try {
            userExists = userClient.exists(userId);
        } catch (Exception e) {
            throw new RuntimeException("User service is currently unavailable. Reservation failed.", e);
        }
        if (userExists == null || !userExists) {
            throw new RuntimeException("Driver with user ID " + userId + " does not exist.");
        }

        // 2. Verify Vehicle Exists via Feign Client
        Boolean vehicleExists = false;
        try {
            vehicleExists = vehicleClient.exists(vehicleId);
        } catch (Exception e) {
            throw new RuntimeException("Vehicle service is currently unavailable. Reservation failed.", e);
        }
        if (vehicleExists == null || !vehicleExists) {
            throw new RuntimeException("Vehicle with ID " + vehicleId + " does not exist.");
        }

        // 3. Verify Parking Space availability
        ParkingSpace space = parkingSpaceRepository.findById(spaceId)
                .orElseThrow(() -> new ParkingSpaceNotFoundException("Parking space not found with ID: " + spaceId));

        if (space.isOccupied()) {
            throw new ParkingSpaceOccupiedException("Parking space is already occupied.");
        }

        // 4. Update Space Status
        space.setOccupied(true);
        parkingSpaceRepository.save(space);

        // 5. Create Reservation
        Reservation reservation = new Reservation();
        reservation.setParkingSpaceId(spaceId);
        reservation.setVehicleId(vehicleId);
        reservation.setUserId(userId);
        reservation.setStartTime(LocalDateTime.now());
        reservation.setActive(true);

        return reservationRepository.save(reservation);
    }

    public Reservation releaseSpace(Long reservationId, String cardNumber) {
        // 1. Find Reservation
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found with ID: " + reservationId));

        if (!reservation.isActive()) {
            throw new RuntimeException("Reservation is already inactive/released.");
        }

        // 2. Find Parking Space
        ParkingSpace space = parkingSpaceRepository.findById(reservation.getParkingSpaceId())
                .orElseThrow(() -> new ParkingSpaceNotFoundException("Parking space not found with ID: " + reservation.getParkingSpaceId()));

        // 3. Mark Space as Available
        space.setOccupied(false);
        parkingSpaceRepository.save(space);

        // 4. Calculate Time & Cost (Hourly rate)
        reservation.setEndTime(LocalDateTime.now());
        long seconds = Duration.between(reservation.getStartTime(), reservation.getEndTime()).toSeconds();
        
        // Simulating a realistic minimum time of 3 minutes (0.05 hours) for short tests to verify payment cost
        double hours = seconds / 3600.0;
        if (hours < 0.05) {
            hours = 0.05;
        }
        double rawCost = hours * space.getHourlyRate();
        double roundedCost = Math.round(rawCost * 100.0) / 100.0;
        reservation.setTotalCost(roundedCost);
        reservation.setActive(false);

        // 5. Call Payment Service via Feign Client
        PaymentResponse paymentResponse = null;
        try {
            PaymentRequest paymentRequest = new PaymentRequest(reservation.getId(), roundedCost, cardNumber);
            paymentResponse = paymentClient.processPayment(paymentRequest);
        } catch (Exception e) {
            // Save state as released but payment pending
            reservationRepository.save(reservation);
            throw new RuntimeException("Payment service is currently unavailable. Space released, but payment not processed.", e);
        }

        if (paymentResponse != null) {
            reservation.setReceiptNumber(paymentResponse.getReceiptNumber());
        }

        return reservationRepository.save(reservation);
    }
}
