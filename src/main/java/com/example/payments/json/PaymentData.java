package com.example.payments.json;

import com.example.payments.enums.CurrencyEnum;
import com.example.payments.enums.PaymentTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
public class PaymentData {
    @NotNull
    private PaymentTypeEnum paymentType;

    @NotNull
    @Min(0)
    private BigDecimal amount;

    @NotNull
    private CurrencyEnum currency;

    @NotNull
    private String debtorIban;

    @NotNull
    private String creditorIban;

    private String details;

    private String bic;

    @AssertTrue(message = "Incorrect payment data provided!")
    private boolean isPaymentTypeValid() {
        return paymentType == PaymentTypeEnum.TYPE_1 && currency == CurrencyEnum.EUR && StringUtils.isNotBlank(details) && StringUtils.isBlank(bic)
                || paymentType == PaymentTypeEnum.TYPE_2 && currency == CurrencyEnum.USD && StringUtils.isBlank(bic)
                || paymentType == PaymentTypeEnum.TYPE_3 && StringUtils.isNotBlank(bic);
    }
}
