package com.example.equal.experts.assignment.service;

import com.example.equal.experts.assignment.entity.Cart;
import com.example.equal.experts.assignment.entity.CartItem;
import com.example.equal.experts.assignment.exception.ProductNotFoundException;
import com.example.equal.experts.assignment.model.AddToCartRequest;
import com.example.equal.experts.assignment.model.CartDto;
import com.example.equal.experts.assignment.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class CartService implements CommandLineRunner {

    private final Cart dummyCart;
    private final ProductService productService;

    @Value("${tax.rate}")
    private double taxRate;

    public CartService(ProductService productService) {
        this.productService = productService;
        this.dummyCart = new Cart();
        dummyCart.setCartId("c1");
    }

    public Cart addToCart(AddToCartRequest addToCartRequest) {

        if (Objects.isNull(addToCartRequest))
            throw new NullPointerException("cart request cant be null");

        double quantity = addToCartRequest.getQuantity();
        String productName = addToCartRequest.getProductName();

        Optional<Product> productDetailsOpt = productService.getProductDetails(productName);
        productDetailsOpt.orElseThrow(() -> new ProductNotFoundException("No product available with product name : " + productName));

        String cartId = addToCartRequest.getCartId();

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
            newCartItem.setQuantity(0);
            newCartItem.setCartId("c1");
            return newCartItem;
        });

        cartItem.setQuantity(cartItem.getQuantity() + quantity);

        addCartItem(cart, cartItem);

        saveCart(cart);
        return cart;
    }

    private void addCartItem(Cart cart, CartItem cartItem) {

        Set<CartItem> cartItems = cart.getCartItems();

        if (Objects.isNull(cartItems)) {
            cartItems = new HashSet<>();
            cart.setCartItems(cartItems);
        }

        cartItems.add(cartItem);
    }

    public CartDto getCartDetails(String cartId) {

        Cart cart = getCart(cartId);

        double subTotalForCartItems = calculateSubTotalForCartItems(cart);
        double taxPayable = getTaxAmount(subTotalForCartItems, taxRate);

        double totalPayableAmount = subTotalForCartItems + taxPayable;

        return CartDto
                .builder()
                .cartItems(cart.getCartItems())
                .taxRate(taxRate)
                .taxApplied(taxPayable)
                .totalPrice(subTotalForCartItems)
                .totalPayable(totalPayableAmount)
                .build();
    }

    private double getTaxAmount(double input, double taxRate) {
        double taxMultiple = taxRate / 100;
        return input * taxMultiple;
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

        if (Objects.isNull(cart.getCartItems()))
            return Optional.empty();

        return cart.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProductName().equals(productName))
                .findAny();
    }

    private Cart getCart(String cartId) {
        //fetch cart from repo
        return dummyCart;
    }

    @Override
    public void run(String... args) throws Exception {

        AddToCartRequest addToCartRequest = AddToCartRequest
                .builder()
                .cartId("c1")
                .productName("cornflakes")
                .quantity(1)
                .build();

        addToCart(addToCartRequest);
        addToCart(addToCartRequest);

        addToCartRequest.setProductName("weetabix");
        addToCart(addToCartRequest);

        CartDto cartDto = getCartDetails("c1");
        System.out.println(cartDto.toString());

    }
}
