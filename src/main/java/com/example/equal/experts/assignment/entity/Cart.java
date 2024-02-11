package com.example.equal.experts.assignment.entity;

import lombok.Data;

import java.util.Set;

@Data
public class Cart {

    private String cartId;
    private Set<CartItem> cartItems;
}
