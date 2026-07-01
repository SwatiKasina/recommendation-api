package com.example.recommendation_api.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/v1/thread")
public class CompletableThread {
    @GetMapping(value = "/swati", produces = MediaType.APPLICATION_JSON_VALUE)

    public Integer getName() {
        int input = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
           return  input;
        }, executorService)

                .thenApply(finalResult -> {
                    return finalResult * finalResult;

                });
        int result = completableFuture.join();
        return result;
    }
}