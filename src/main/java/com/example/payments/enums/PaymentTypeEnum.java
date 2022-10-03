package com.example.payments.enums;

import java.math.BigDecimal;

public enum PaymentTypeEnum {
    TYPE_1(new BigDecimal("0.05")),
    TYPE_2(new BigDecimal("0.1")),
    TYPE_3(new BigDecimal("0.15"));

    public final BigDecimal coefficient;
    PaymentTypeEnum(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }
}
