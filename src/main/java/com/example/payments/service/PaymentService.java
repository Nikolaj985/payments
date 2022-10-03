package com.example.payments.service;

import com.example.payments.json.CancelledPaymentView;
import com.example.payments.json.PaymentData;
import com.example.payments.json.SearchRequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PaymentService {
    ResponseEntity createPayment(PaymentData paymentData, HttpServletRequest request);

    ResponseEntity cancelPayment(Long id);

    List<Long> getAllCancelledPaymentIds(SearchRequest search);

    CancelledPaymentView getCancelledPayment(Long id);
}
