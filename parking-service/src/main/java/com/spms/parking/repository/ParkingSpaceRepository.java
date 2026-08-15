package com.spms.parking.repository;

import com.spms.parking.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {
    List<ParkingSpace> findByLocation(String location);
    List<ParkingSpace> findByIsOccupied(boolean isOccupied);
    List<ParkingSpace> findByLocationAndIsOccupied(String location, boolean isOccupied);
}
