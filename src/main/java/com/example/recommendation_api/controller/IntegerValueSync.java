package com.example.recommendation_api.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
@RestController
@RequestMapping("/api")

public class IntegerValueSync {

    private final Object valueLock = new Object();
    int input=1;

    public int valueInt (){
        synchronized (valueLock) {
            input = input * 2;
            return input;
        }
    }
    @GetMapping( value ="/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public int getValue() throws ExecutionException, InterruptedException {
        int input =1;
        ExecutorService executor = Executors.newFixedThreadPool(5);

        Callable<Integer> task =() -> valueInt();

        List<Future<Integer>> futures = new ArrayList<>();
        for(int i = 0; i<5; i++){
            Future<Integer> future = executor.submit(task);
            futures.add(future);
        }
        for(Future<Integer> future : futures){
            int result = future.get();
            System.out.println(result);
        }
        executor.shutdown();
        return input;
    }


}


