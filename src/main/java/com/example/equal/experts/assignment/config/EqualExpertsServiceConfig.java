package com.example.equal.experts.assignment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("equal.experts.service")
@Data
public class EqualExpertsServiceConfig {

    private String baseUrl;
    private String productApiEndpoint;
}
