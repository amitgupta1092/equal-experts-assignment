package com.example.equal.experts.assignment.service;

import com.example.equal.experts.assignment.exception.ProductNotFoundException;
import com.example.equal.experts.assignment.model.AddToCartRequest;
import com.example.equal.experts.assignment.model.CartDto;
import com.example.equal.experts.assignment.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class CartServiceTest {

    private CartService cartService;

    private ProductService productService;

    @BeforeEach
    void setup() {
        productService = Mockito.mock(ProductService.class);

        Product cornflakes = Product
                .builder()
                .title("cornflakes")
                .price(2.52)
                .build();

        Product weetabix = Product
                .builder()
                .title("weetabix")
                .price(9.98)
                .build();

        Mockito.when(productService.getProductDetails("cornflakes")).thenReturn(Optional.of(cornflakes));
        Mockito.when(productService.getProductDetails("weetabix")).thenReturn(Optional.of(weetabix));

        cartService = new CartService(productService);
        cartService.setTaxRate(12.5);
    }

    @Test
    public void testSanityUseCase() {

        AddToCartRequest addToCartRequest = AddToCartRequest
                .builder()
                .cartId("c1")
                .productName("cornflakes")
                .quantity(1)
                .build();

        cartService.addToCart(addToCartRequest);
        cartService.addToCart(addToCartRequest);

        addToCartRequest.setProductName("weetabix");
        cartService.addToCart(addToCartRequest);

        CartDto cartDto = cartService.getCartDetails("c1");
        Assertions.assertNotNull(cartDto);
        Assertions.assertEquals(15.02, cartDto.getTotalPrice());
        Assertions.assertEquals(1.88, cartDto.getTaxApplied());
        Assertions.assertEquals(16.90, cartDto.getTotalPayable());
    }

    @Test
    public void addToCart_NullInput() {
        Assertions.assertThrows(NullPointerException.class, () -> cartService.addToCart(null));
    }

    @Test
    public void addToCart_InvalidProductName() {

        AddToCartRequest addToCartRequest = AddToCartRequest
                .builder()
                .cartId("c1")
                .productName("")
                .quantity(1)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> cartService.addToCart(addToCartRequest));

        addToCartRequest.setProductName(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> cartService.addToCart(addToCartRequest));

    }

    @Test
    public void addToCart_InvalidCartId() {

        AddToCartRequest addToCartRequest = AddToCartRequest
                .builder()
                .cartId("")
                .productName("cornflakes")
                .quantity(1)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> cartService.addToCart(addToCartRequest));

        addToCartRequest.setCartId(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> cartService.addToCart(addToCartRequest));

    }

    @Test
    public void addToCart_InvalidQuantity() {

        AddToCartRequest addToCartRequest = AddToCartRequest
                .builder()
                .cartId("c1")
                .productName("cornflakes")
                .quantity(0)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> cartService.addToCart(addToCartRequest));
    }

    @Test
    public void addToCart_ProductNotFound() {

        AddToCartRequest addToCartRequest = AddToCartRequest
                .builder()
                .cartId("c1")
                .productName("pencil")
                .quantity(1)
                .build();
        Assertions.assertThrows(ProductNotFoundException.class, () -> cartService.addToCart(addToCartRequest));
    }
}
