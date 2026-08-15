package com.spms.payment.controller;

import com.spms.payment.dto.PaymentDTOs.PaymentRequest;
import com.spms.payment.entity.Payment;
import com.spms.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> pay(@Valid @RequestBody PaymentRequest request) {
        Payment payment = paymentService.processPayment(request);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @GetMapping("/receipt/{receiptNumber}")
    public ResponseEntity<Payment> getReceipt(@PathVariable String receiptNumber) {
        Payment payment = paymentService.getPaymentByReceipt(receiptNumber);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }
}
