package com.example.colorbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ColorAiService {

    @Value("${huggingface.api.token}")
    private String apiToken;

    @Value("${huggingface.model.name}")
    private String modelName;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getColorSuggestion(String prompt) {
        String url = "https://api-inference.huggingface.co/models/" + modelName;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payload = Map.of("inputs", prompt);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }
}
