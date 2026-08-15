package com.spms.parking.dto;

public class ReservationDTOs {

    public static class ReserveRequest {
        private Long spaceId;
        private Long vehicleId;
        private Long userId;

        // Getters and Setters
        public Long getSpaceId() { return spaceId; }
        public void setSpaceId(Long spaceId) { this.spaceId = spaceId; }
        public Long getVehicleId() { return vehicleId; }
        public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }

    public static class ReleaseRequest {
        private String cardNumber;

        // Getters and Setters
        public String getCardNumber() { return cardNumber; }
        public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    }
}
