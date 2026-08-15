package com.spms.payment.service;

import com.spms.payment.dto.PaymentDTOs.PaymentRequest;
import com.spms.payment.entity.Payment;
import com.spms.payment.exception.InvalidCardException;
import com.spms.payment.exception.PaymentNotFoundException;
import com.spms.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment processPayment(PaymentRequest request) {
        String card = request.getCardNumber().replaceAll("\\s+", "");
        if (!card.matches("\\d{16}")) {
            throw new InvalidCardException("Invalid card format: card number must be exactly 16 digits.");
        }

        Payment payment = new Payment();
        payment.setReservationId(request.getReservationId());
        payment.setAmount(request.getAmount());
        payment.setCardNumber("XXXX-XXXX-XXXX-" + card.substring(12));
        payment.setTransactionDate(LocalDateTime.now());

        // Simulate failure if card ends with "9999"
        if (card.endsWith("9999")) {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            throw new RuntimeException("Payment transaction declined by issuing bank (simulated failure).");
        }

        payment.setStatus("SUCCESS");
        payment.setReceiptNumber("RCPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return paymentRepository.save(payment);
    }

    public Payment getPaymentByReceipt(String receiptNumber) {
        return paymentRepository.findByReceiptNumber(receiptNumber)
                .orElseThrow(() -> new PaymentNotFoundException("Payment receipt not found: " + receiptNumber));
    }
}
