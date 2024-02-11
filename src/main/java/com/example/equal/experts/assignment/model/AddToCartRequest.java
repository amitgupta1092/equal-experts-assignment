package com.example.equal.experts.assignment.model;

import lombok.Data;

@Data
public class AddToCartRequest {
    private String cartId;
    private String productName;
    private String quantity;
}
