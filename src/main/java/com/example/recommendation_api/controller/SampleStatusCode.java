package com.example.recommendation_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SampleStatusCode {
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Users fetched successfully");
        return ResponseEntity.ok(response);
    }

    //  500 Internal Server Error (to test monitoring)
    @GetMapping("/error")
    public ResponseEntity<?> triggerError() {
        throw new RuntimeException("Simulated server error");
    }
}
