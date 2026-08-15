package com.spms.parking.controller;

import com.spms.parking.dto.ReservationDTOs.*;
import com.spms.parking.entity.ParkingSpace;
import com.spms.parking.entity.Reservation;
import com.spms.parking.service.ParkingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @PostMapping("/spaces")
    public ResponseEntity<ParkingSpace> addSpace(@Valid @RequestBody ParkingSpace space) {
        ParkingSpace savedSpace = parkingService.addParkingSpace(space);
        return new ResponseEntity<>(savedSpace, HttpStatus.CREATED);
    }

    @GetMapping("/spaces")
    public ResponseEntity<List<ParkingSpace>> getSpaces(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean isOccupied) {
        List<ParkingSpace> spaces = parkingService.listParkingSpaces(location, isOccupied);
        return new ResponseEntity<>(spaces, HttpStatus.OK);
    }

    @PostMapping("/reserve")
    public ResponseEntity<Reservation> reserve(@RequestBody ReserveRequest request) {
        Reservation reservation = parkingService.reserveSpace(
                request.getSpaceId(),
                request.getVehicleId(),
                request.getUserId()
        );
        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }

    @PostMapping("/release/{reservationId}")
    public ResponseEntity<Reservation> release(
            @PathVariable Long reservationId,
            @RequestBody ReleaseRequest request) {
        Reservation reservation = parkingService.releaseSpace(reservationId, request.getCardNumber());
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }
}
