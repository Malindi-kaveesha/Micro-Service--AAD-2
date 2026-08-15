package com.spms.parking.exception;

public class ParkingSpaceNotFoundException extends RuntimeException {
    public ParkingSpaceNotFoundException(String message) {
        super(message);
    }
}
