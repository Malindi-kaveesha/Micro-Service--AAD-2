package com.spms.vehicle.repository;

import com.spms.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    List<Vehicle> findByOwnerId(Long ownerId);
    boolean existsByLicensePlate(String licensePlate);
}
