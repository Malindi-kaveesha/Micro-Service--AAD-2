package com.spms.vehicle.controller;

import com.spms.vehicle.entity.Vehicle;
import com.spms.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<Vehicle> register(@Valid @RequestBody Vehicle vehicle) {
        Vehicle savedVehicle = vehicleService.registerVehicle(vehicle);
        return new ResponseEntity<>(savedVehicle, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getById(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @GetMapping("/plate/{licensePlate}")
    public ResponseEntity<Vehicle> getByLicensePlate(@PathVariable String licensePlate) {
        Vehicle vehicle = vehicleService.getVehicleByLicensePlate(licensePlate);
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Vehicle>> getByOwner(@PathVariable Long ownerId) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByOwner(ownerId);
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @PostMapping("/{id}/entry")
    public ResponseEntity<Vehicle> recordEntry(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.trackEntry(id);
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @PostMapping("/{id}/exit")
    public ResponseEntity<Vehicle> recordExit(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.trackExit(id);
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        boolean exists = vehicleService.checkVehicleExists(id);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}
