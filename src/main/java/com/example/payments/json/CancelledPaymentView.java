package com.example.payments.json;

import com.example.payments.entity.PaymentEntity;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelledPaymentView {
    private Long id;
    private BigDecimal cancelFee;

    public static CancelledPaymentView of(PaymentEntity payment) {
        if(payment == null){
            return null;
        }

        return CancelledPaymentView.builder()
                .id(payment.getId())
                .cancelFee(payment.getCancelFee())
                .build();
    }

}
