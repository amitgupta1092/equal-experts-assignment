package com.example.equal.experts.assignment.service;

import com.example.equal.experts.assignment.config.EqualExpertsServiceConfig;
import com.example.equal.experts.assignment.model.Product;
import com.example.equal.experts.assignment.util.RestClient;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EqualExpertsService {

    private final EqualExpertsServiceConfig equalExpertsServiceConfig;
    private final RestClient restClient;

    public EqualExpertsService(@NonNull final EqualExpertsServiceConfig equalExpertsServiceConfig,
                               @NonNull final RestClient restClient) {
        this.equalExpertsServiceConfig = equalExpertsServiceConfig;
        this.restClient = restClient;
    }

    public Product getProductDetailsByProductName(String productName) {

        String endpoint = equalExpertsServiceConfig.getBaseUrl()
                + equalExpertsServiceConfig.getProductApiEndpoint();

        return restClient.getForEntity(endpoint,
                Product.class,
                Map.of("product", productName),
                null);
    }
}
