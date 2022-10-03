package com.example.payments.json;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchRequest {
    private Double minValue;
    private Double maxValue;
}
