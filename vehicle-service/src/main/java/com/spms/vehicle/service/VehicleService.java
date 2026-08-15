package com.spms.vehicle.service;

import com.spms.vehicle.entity.Vehicle;
import com.spms.vehicle.exception.LicensePlateAlreadyExistsException;
import com.spms.vehicle.exception.VehicleNotFoundException;
import com.spms.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Vehicle registerVehicle(Vehicle vehicle) {
        if (vehicleRepository.existsByLicensePlate(vehicle.getLicensePlate())) {
            throw new LicensePlateAlreadyExistsException("License plate already registered: " + vehicle.getLicensePlate());
        }

        // Verify that the owner (user) exists in the user-service
        Boolean userExists = false;
        try {
            userExists = restTemplate.getForObject("http://user-service/api/users/exists/" + vehicle.getOwnerId(), Boolean.class);
        } catch (Exception e) {
            throw new RuntimeException("User service is currently unavailable. Cannot verify owner ID.", e);
        }

        if (userExists == null || !userExists) {
            throw new RuntimeException("Owner with user ID " + vehicle.getOwnerId() + " does not exist.");
        }

        return vehicleRepository.save(vehicle);
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + id));
    }

    public Vehicle getVehicleByLicensePlate(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with license plate: " + licensePlate));
    }

    public List<Vehicle> getVehiclesByOwner(Long ownerId) {
        return vehicleRepository.findByOwnerId(ownerId);
    }

    public Vehicle trackEntry(Long id) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setStatus("IN");
        return vehicleRepository.save(vehicle);
    }

    public Vehicle trackExit(Long id) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setStatus("OUT");
        return vehicleRepository.save(vehicle);
    }

    public boolean checkVehicleExists(Long id) {
        return vehicleRepository.existsById(id);
    }
}
