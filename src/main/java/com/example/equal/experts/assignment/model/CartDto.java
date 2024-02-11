package com.example.equal.experts.assignment.model;

import com.example.equal.experts.assignment.entity.CartItem;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class CartDto {

    private String cartId;
    private Set<CartItem> cartItems;

    private double totalPrice;
    private double taxRate;
    private double taxApplied;
    private double totalPayable;
}
