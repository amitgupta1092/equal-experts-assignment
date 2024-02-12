package com.example.equal.experts.assignment.model;

import com.example.equal.experts.assignment.entity.CartItem;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;
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

    public String toString() {

        StringBuilder sb = new StringBuilder();

        if (Objects.isNull(cartItems)
                || cartItems.size() == 0) {
            return "Your cart is empty !";
        }

        cartItems.forEach(cartItem -> {
            sb.append("Cart contains ")
                    .append(cartItem.getQuantity())
                    .append(" x ")
                    .append(cartItem.getProductName())
                    .append("\n");
        });

        sb.append("subTotal ")
                .append(totalPrice)
                .append("\n")
                .append("Tax ")
                .append(taxApplied)
                .append("\n")
                .append("Total ")
                .append(totalPayable)
                .append("\n");

        return sb.toString();
    }
}
