package com.example.equal.experts.assignment.service;

import com.example.equal.experts.assignment.model.Product;
import org.springframework.core.convert.ConversionService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final EqualExpertsService equalExpertsService;
    private final ConversionService conversionService;

    public ProductService(@NonNull final EqualExpertsService equalExpertsService,
                          ConversionService conversionService) {
        this.equalExpertsService = equalExpertsService;
        this.conversionService = conversionService;
    }

    public Optional<Product> getProductDetails(String productName) {
        Product product = equalExpertsService.getProductDetailsByProductName(productName);
        return Optional.ofNullable(product);
    }
}
