package com.spms.parking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parkingSpaceId;
    private Long vehicleId;
    private Long userId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private boolean isActive = true;
    private Double totalCost = 0.0;
    private String receiptNumber;

    public Reservation() {}

    public Reservation(Long id, Long parkingSpaceId, Long vehicleId, Long userId, LocalDateTime startTime, LocalDateTime endTime, boolean isActive, Double totalCost, String receiptNumber) {
        this.id = id;
        this.parkingSpaceId = parkingSpaceId;
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = isActive;
        this.totalCost = totalCost;
        this.receiptNumber = receiptNumber;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getParkingSpaceId() { return parkingSpaceId; }
    public void setParkingSpaceId(Long parkingSpaceId) { this.parkingSpaceId = parkingSpaceId; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }
    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }
}
