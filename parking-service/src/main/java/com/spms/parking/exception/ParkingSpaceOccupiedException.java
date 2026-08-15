package com.spms.parking.exception;

public class ParkingSpaceOccupiedException extends RuntimeException {
    public ParkingSpaceOccupiedException(String message) {
        super(message);
    }
}
