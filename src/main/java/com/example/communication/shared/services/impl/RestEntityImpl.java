package com.example.communication.shared.services.impl;

import com.example.communication.shared.persistance.models.PostEntity;
import com.example.communication.shared.services.RestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
@Service
public class RestEntityImpl<T> implements RestEntity {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ResponseEntity<T> post(PostEntity postEntity){
        RestTemplate restTemplate = new RestTemplate();
        // Set the request URL
        String baseUrl =postEntity.getUrl();
        String endpoint = postEntity.getEndpoint();

        // Build the complete URL with parameters
        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path(endpoint)
                .build();

        // Set the request headers
        HttpHeaders headers = postEntity.getHeaders();
        //create http request body

        // Create the HTTP entity with headers and body
        HttpEntity<Object> entity = new HttpEntity<>(postEntity.getBody(), headers);

        // Send the POST request
        ResponseEntity response = restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, postEntity.getBody().getClass());

        // Print the response
        log.info("Rest response status code: {} ,message: {}",response.getStatusCode(),response.getBody());
        return response;
    }
}
