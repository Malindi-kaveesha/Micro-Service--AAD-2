package com.spms.parking.dto;

public class PaymentDTOs {

    public static class PaymentRequest {
        private Long reservationId;
        private Double amount;
        private String cardNumber;

        public PaymentRequest() {}

        public PaymentRequest(Long reservationId, Double amount, String cardNumber) {
            this.reservationId = reservationId;
            this.amount = amount;
            this.cardNumber = cardNumber;
        }

        // Getters and Setters
        public Long getReservationId() { return reservationId; }
        public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
        public String getCardNumber() { return cardNumber; }
        public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    }

    public static class PaymentResponse {
        private Long id;
        private Long reservationId;
        private Double amount;
        private String status;
        private String receiptNumber;
        private String transactionDate;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getReservationId() { return reservationId; }
        public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getReceiptNumber() { return receiptNumber; }
        public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }
        public String getTransactionDate() { return transactionDate; }
        public void setTransactionDate(String transactionDate) { this.transactionDate = transactionDate; }
    }
}
