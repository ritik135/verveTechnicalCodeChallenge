package com.example.verve.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.verve.config.RestTemplateConfig;

@Service
public class HttpRequestService {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestService.class);
    
    @Autowired
    private RestTemplateConfig restTemplate;

    // Method to fire HTTP request (GET or POST)
    public void fireHttpRequest(String endpoint, int count, String method) {
        String url = UriComponentsBuilder.fromHttpUrl(endpoint)
                                        .queryParam("uniqueCount", count)
                                        .toUriString();

        try {
            if ("POST".equalsIgnoreCase(method)) {
                String payload = "{\"uniqueCount\": " + count + "}"; 
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "application/json");
                HttpEntity<String> entity = new HttpEntity<>(payload, headers);
                logger.info("Firing HTTP POST request to: {}", url);
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                logger.info("HTTP response status: {}", response.getStatusCode());
            } else {
                logger.info("Firing HTTP GET request to: {}", url);
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
                logger.info("HTTP response status: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error firing HTTP request: {}", e.getMessage(), e);
        }
    }
}

