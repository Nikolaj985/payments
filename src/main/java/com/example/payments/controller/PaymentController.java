package com.example.payments.controller;

import com.example.payments.json.CancelledPaymentView;
import com.example.payments.json.PaymentData;
import com.example.payments.json.SearchRequest;
import com.example.payments.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/new")
    public ResponseEntity createPayment(@RequestBody @Valid PaymentData paymentData, HttpServletRequest request) {
        return paymentService.createPayment(paymentData, request);
    }

    @GetMapping("/cancel/{id}")
    public ResponseEntity cancelPayment(@PathVariable Long id) {
        return paymentService.cancelPayment(id);
    }

    @PostMapping("/active")
    public List<Long> getAllCancelledPaymentIds(@RequestBody SearchRequest search) {
        return paymentService.getAllCancelledPaymentIds(search);
    }

    @GetMapping("/cancelled/{id}")
    public CancelledPaymentView getAllPayments(@PathVariable Long id) {
        return paymentService.getCancelledPayment(id);
    }

}
