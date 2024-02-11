package com.example.equal.experts.assignment.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class CartItem {
    @EqualsAndHashCode.Include
    private String productName;

    @EqualsAndHashCode.Include
    private double quantity;

    public String cartId;
}
