package com.example.payments.specification;

import com.example.payments.entity.PaymentEntity;
import com.example.payments.json.SearchRequest;
import org.springframework.data.jpa.domain.Specification;

public final class PaymentSpecification {
    public static Specification<PaymentEntity> build(SearchRequest search) {
        Specification<PaymentEntity> specification = (root, query, cb) -> cb.isFalse(root.get("cancelled"));

        if (search.getMaxValue() != null) {
            specification = specification.and((root, query, cb) -> cb.le(root.get("amount"), search.getMaxValue()));
        }

        if (search.getMinValue() != null) {
            specification = specification.and((root, query, cb) -> cb.ge(root.get("amount"), search.getMinValue()));
        }

        return specification;
    }

    public static Specification<PaymentEntity> build(Long id) {
        Specification<PaymentEntity> specification = (root, query, cb) -> cb.isTrue(root.get("cancelled"));

        return specification.and((root, query, cb) -> cb.equal(root.get("id"), id));
    }
}
