package com.example.colorbackend.controller;

import com.example.colorbackend.model.ColorScheme;
import com.example.colorbackend.repository.ColorSchemeRepository;
import com.example.colorbackend.service.ColorAiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/colors")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ColorController {

    private final ColorSchemeRepository repository;
    private final ColorAiService colorAiService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/generate")
    public ColorScheme generate(@RequestBody PromptRequest req) {
        String aiReply = colorAiService.getColorSuggestion(req.prompt);

        try {
            // Parse JSON trả về từ HuggingFace
            JsonNode root = objectMapper.readTree(aiReply);

            String content = root.isArray() ? root.get(0).asText() : root.toString();
            JsonNode colors = objectMapper.readTree(content);

            ColorScheme cs = new ColorScheme(
                    null,
                    req.prompt,
                    colors.path("primary").asText("#3366FF"),
                    colors.path("secondary").asText("#99CCFF"),
                    colors.path("alert").asText("#FFCC00"),
                    colors.path("danger").asText("#FF3300"),
                    colors.path("success").asText("#33CC33"),
                    colors.path("info").asText("#0099CC"),
                    0,
                    0
            );
            return repository.save(cs);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response: " + aiReply, e);
        }
    }

    @PostMapping("/vote")
    public void vote(@RequestBody VoteRequest req) {
        ColorScheme latest = repository.findTopByOrderByIdDesc();
        if (latest != null) {
            if ("upvote".equals(req.type)) latest.setUpvotes(latest.getUpvotes() + 1);
            else if ("downvote".equals(req.type)) latest.setDownvotes(latest.getDownvotes() + 1);
            repository.save(latest);
        }
    }

    @PostMapping("/regenerate")
    public ColorScheme regenerate(@RequestBody RegenerateRequest req) {
        return generate(new PromptRequest("Re-generated from: " + req.reason));
    }

    @GetMapping("/latest")
    public ColorScheme latest() {
        return repository.findTopByOrderByIdDesc();
    }

    record PromptRequest(String prompt) {}
    record VoteRequest(String type) {}
    record RegenerateRequest(String reason) {}
}
