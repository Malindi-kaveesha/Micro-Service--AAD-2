package com.spms.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PaymentDTOs {

    public static class PaymentRequest {
        @NotNull(message = "Reservation ID is required")
        private Long reservationId;

        @NotNull(message = "Amount is required")
        private Double amount;

        @NotBlank(message = "Card number is required")
        private String cardNumber;

        // Getters & Setters
        public Long getReservationId() { return reservationId; }
        public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
        public String getCardNumber() { return cardNumber; }
        public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    }
}
