package com.example.equal.experts.assignment.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CartItem {
    @EqualsAndHashCode.Include
    private String productName;

    private double quantity;

    @EqualsAndHashCode.Include
    public String cartId;
}
