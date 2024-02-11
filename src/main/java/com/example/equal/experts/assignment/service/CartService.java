package com.example.equal.experts.assignment.service;

import com.example.equal.experts.assignment.entity.Cart;
import com.example.equal.experts.assignment.entity.CartItem;
import com.example.equal.experts.assignment.model.AddToCartRequest;
import com.example.equal.experts.assignment.model.CartDto;
import com.example.equal.experts.assignment.model.Product;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class CartService {

    private final Cart dummyCart;
    private final ProductService productService;

    public CartService(ProductService productService) {
        this.productService = productService;
        this.dummyCart = new Cart();
        dummyCart.setCartId("userCart");
    }

    public Cart addToCart(AddToCartRequest addToCartRequest) {

        if (Objects.isNull(addToCartRequest))
            throw new NullPointerException("cart request cant be null");

        String quantity = addToCartRequest.getQuantity();
        String productName = addToCartRequest.getProductName();
        String cartId = addToCartRequest.getCartId();

        double quantityDbl;
        try {
            quantityDbl = Double.parseDouble(quantity);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("invalid product quantity : " + quantity);
        }

        if (Objects.isNull(productName) || productName.trim().length() == 0) {
            throw new IllegalArgumentException("product name cant be null or empty");
        }

        if (Objects.isNull(cartId) || cartId.trim().length() == 0) {
            throw new IllegalArgumentException("cart id cant be null or empty");
        }

        Cart cart = getCart(cartId);
        Optional<CartItem> cartItemForProductOpt = getCartItemForProduct(cart, productName);

        CartItem cartItem = cartItemForProductOpt.orElseGet(() -> {
            CartItem newCartItem = new CartItem();
            newCartItem.setProductName(productName);
            newCartItem.setQuantity(quantityDbl);
            return newCartItem;
        });

        cart.getCartItems().add(cartItem);

        saveCart(cart);
        return cart;
    }

    public CartDto getCartDetails(String cartId) {

        Cart cart = getCart(cartId);

        double subTotalForCartItems = calculateSubTotalForCartItems(cart);
        double taxRate = 1.125;
        double taxPayable = subTotalForCartItems * taxRate;

        double totalPayableAmount = subTotalForCartItems + taxPayable;

        return CartDto
                .builder()
                .taxRate(taxRate)
                .taxApplied(taxPayable)
                .totalPrice(subTotalForCartItems)
                .totalPayable(totalPayableAmount)
                .build();
    }

    private double calculateSubTotalForCartItems(Cart cart) {

        Set<CartItem> cartItems = cart.getCartItems();

        double subTotalForCartItems = 0;
        for (CartItem cartItem : cartItems) {

            String productName = cartItem.getProductName();
            double quantity = cartItem.getQuantity();

            Optional<Product> productDetailsOpt = productService.getProductDetails(productName);
            if (productDetailsOpt.isPresent()) {
                Product product = productDetailsOpt.get();
                Double price = product.getPrice();

                if (price >= 0) {
                    double total = price * quantity;
                    subTotalForCartItems += total;
                }
            }
        }

        return subTotalForCartItems;
    }


    private void saveCart(Cart cart) {
        //persist back to DB;
    }

    private Optional<CartItem> getCartItemForProduct(Cart cart,
                                                     String productName) {

        return cart.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProductName().equals(productName))
                .findAny();
    }

    private Cart getCart(String cartId) {
        //fetch cart from repo
        return dummyCart;
    }
}
