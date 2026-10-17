package com.codehunt.carsales.controller;

import com.codehunt.carsales.service.AIQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIQueryService aiService;

    public AIController(AIQueryService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ask")
    public ResponseEntity<String> ask(@RequestBody String question) {
        return ResponseEntity.ok(aiService.process(question));
    }
}