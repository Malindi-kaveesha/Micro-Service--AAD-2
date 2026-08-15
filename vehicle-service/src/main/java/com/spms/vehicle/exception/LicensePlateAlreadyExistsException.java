package com.spms.vehicle.exception;

public class LicensePlateAlreadyExistsException extends RuntimeException {
    public LicensePlateAlreadyExistsException(String message) {
        super(message);
    }
}
