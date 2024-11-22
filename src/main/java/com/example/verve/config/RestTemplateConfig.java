package com.example.verve.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfig {

    /**
     * Defines a RestTemplate bean that can be autowired into other services.
     *
     * @return a new instance of RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public ResponseEntity<String> exchange(String url, HttpMethod method, Object object, Class<String> responseType) {
        try {
            // Create HttpHeaders (optional, if you need to set custom headers)
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");  // You can customize the headers as needed
            
            // Create HttpEntity, which includes the body (your object) and headers
            HttpEntity<Object> entity = new HttpEntity<>(object, headers);
            
            // Instantiate RestTemplate (or use your injected RestTemplate bean)
            RestTemplate restTemplate = new RestTemplate();
            
            // Make the HTTP request and return the response
            ResponseEntity<String> response = restTemplate.exchange(url, method, entity, responseType);
            
            return response;  // Return the response
        } catch (Exception e) {
            // Handle any errors (optional)
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }
}

