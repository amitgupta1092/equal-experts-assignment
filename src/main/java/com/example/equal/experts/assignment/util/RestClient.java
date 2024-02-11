package com.example.equal.experts.assignment.util;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class RestClient {

    private final RestTemplate restTemplate;

    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T postForEntity(String endpoint, Object request, Class<T> responseType, Map<String, String> headers) {

        HttpHeaders defaultHeaders = getDefaultHeaders();
        addInputHeaders(defaultHeaders, headers);
        HttpEntity<Object> requestEntity = new HttpEntity<>(request, defaultHeaders);
        ResponseEntity<T> responseEntity = restTemplate.postForEntity(endpoint, requestEntity, responseType);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity.getBody();
        }

        throw new RuntimeException("unable to send post request");
    }

    private void addInputHeaders(HttpHeaders defaultHeaders, Map<String, String> headers) {
        if (Objects.isNull(headers) || Objects.isNull(defaultHeaders)) return;

        headers.forEach(defaultHeaders::set);
    }

    public <T> T getForEntity(String endpoint,
                              Class<T> responseType,
                              Map<String, Object> uriVariables,
                              Map<String, Object> queryParams) {

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(endpoint);

        if (Objects.nonNull(queryParams)) {
            queryParams.forEach(uriComponentsBuilder::queryParam);
        }

        URI uri;

        if (Objects.nonNull(uriVariables)) {
            UriComponents uriComponents = uriComponentsBuilder.buildAndExpand(uriVariables);
            uri = uriComponents.toUri();
        } else {
            uri = uriComponentsBuilder.build().toUri();
        }

        HttpHeaders defaultHeaders = getDefaultHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<>(defaultHeaders);

        ResponseEntity<T> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, responseType);
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity.getBody();
        }
        throw new RuntimeException("unable to send http get request");
    }

    private HttpHeaders getDefaultHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(supportedMediaTypes);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return httpHeaders;
    }
}
