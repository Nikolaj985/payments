package com.example.payments.entity;

import com.example.payments.enums.CurrencyEnum;
import com.example.payments.enums.PaymentTypeEnum;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentTypeEnum paymentType;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private CurrencyEnum currency;

    @Column(name = "debtor_iban", nullable = false)
    private String debtorIban;

    @Column(name = "creditor_iban", nullable = false)
    private String creditorIban;

    @Column(name = "details")
    private String details;

    @Column(name = "bic")
    private String bic;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "cancelled", columnDefinition="boolean default false")
    private boolean cancelled;

    @Column(name = "cancel_fee")
    private BigDecimal cancelFee;

    @Column(name = "service_responce", columnDefinition="TEXT")
    private String serviceResponse;

}
