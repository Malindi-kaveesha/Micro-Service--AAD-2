package com.spms.vehicle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "License plate is required")
    @Column(unique = true)
    private String licensePlate;

    @NotBlank(message = "Vehicle type is required (e.g. CAR, SUV)")
    private String type;

    @NotNull(message = "Owner ID is required")
    private Long ownerId;

    private String status = "OUT"; // "IN" or "OUT" (entry/exit tracking)

    public Vehicle() {}

    public Vehicle(Long id, String licensePlate, String type, Long ownerId, String status) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.type = type;
        this.ownerId = ownerId;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
