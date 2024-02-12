package com.example.equal.experts.assignment.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddToCartRequest {
    private String cartId;
    private String productName;
    private double quantity;
}
