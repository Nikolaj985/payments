package com.example.payments.service.impl;

import com.example.payments.constant.CommonConstants;
import com.example.payments.entity.PaymentEntity;
import com.example.payments.enums.PaymentTypeEnum;
import com.example.payments.json.CancelledPaymentView;
import com.example.payments.json.PaymentData;
import com.example.payments.json.SearchRequest;
import com.example.payments.json.UserCountry;
import com.example.payments.repository.PaymentRepository;
import com.example.payments.service.PaymentService;
import com.example.payments.specification.PaymentSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;

    @Override
    public ResponseEntity createPayment(PaymentData paymentData, HttpServletRequest request) {

        String response = "";
        if (paymentData.getPaymentType() == PaymentTypeEnum.TYPE_1 || paymentData.getPaymentType() == PaymentTypeEnum.TYPE_2) {
            response = getData(paymentData.getPaymentType() == PaymentTypeEnum.TYPE_1
                    ? CommonConstants.USD_RATES_URL
                    : CommonConstants.WEATHER_URL);
        }

        PaymentEntity payment = PaymentEntity.builder()
                .paymentType(paymentData.getPaymentType())
                .amount(paymentData.getAmount())
                .creditorIban(paymentData.getCreditorIban())
                .debtorIban(paymentData.getDebtorIban())
                .currency(paymentData.getCurrency())
                .bic(paymentData.getBic())
                .dateTime(LocalDateTime.now())
                .details(paymentData.getDetails())
                .serviceResponse(response)
                .build();

        paymentRepository.save(payment);

        getUserCountry(request);

        return ResponseEntity.status(HttpStatus.CREATED).body("Payment accepted");
    }

    @Override
    @Transactional
    public ResponseEntity cancelPayment(Long id) {
        PaymentEntity paymentToCancel = paymentRepository.getReferenceById(id);

        try {
            Validate.isTrue(paymentToCancel.getId() != null
                    && paymentToCancel.getDateTime().isBefore(LocalDate.now().atTime(LocalTime.MAX))
                    && !paymentToCancel.isCancelled(), "Payment can't be cancelled!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("No payment found!");
        }

        paymentToCancel.setCancelFee(calculateFee(paymentToCancel));
        paymentToCancel.setCancelled(true);
        paymentRepository.save(paymentToCancel);
        return ResponseEntity.status(HttpStatus.CREATED).body("Payment cancelled");
    }

    @Override
    public List<Long> getAllCancelledPaymentIds(SearchRequest search) {
        return paymentRepository.findAll(PaymentSpecification.build(search))
                .stream()
                .map(PaymentEntity::getId)
                .collect(Collectors.toList());
    }

    @Override
    public CancelledPaymentView getCancelledPayment(Long id) {
        return CancelledPaymentView.of(paymentRepository.findOne(PaymentSpecification.build(id)).orElse(null));
    }

    private BigDecimal calculateFee(PaymentEntity paymentToCancel) {
        LocalDateTime now = LocalDateTime.now();
        BigDecimal hoursPassed = BigDecimal.valueOf(Math.floor(paymentToCancel.getDateTime().until(now, ChronoUnit.HOURS)));
        return paymentToCancel.getPaymentType().coefficient.multiply(hoursPassed);
    }

    private String getData(String url) {
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            InputStream response = connection.getInputStream();
            String responseText = new BufferedReader(
                    new InputStreamReader(response, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            return responseText;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return StringUtils.EMPTY;
    }

    private void getUserCountry(HttpServletRequest request) {
        String response = getData(CommonConstants.COUNTRY_URL + request.getRemoteAddr() + "?fields=16385");
        try {
            UserCountry userCountry = objectMapper.readValue(response, UserCountry.class);
            if (userCountry.getStatus().equals("success")) {
                System.out.println(userCountry.getCountry());
            }
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }
}
