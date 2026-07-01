package com.example.recommendation_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RestController
@RequestMapping("api/v1/thread")
@RequiredArgsConstructor


public class Atomicity {
    int dontsync =1;
    int value =1;
    private final Object valueLock = new Object();
    public  int multiply() {
        dontsync = dontsync * 2;

        synchronized (valueLock) {
            value = value * 2;
            return value;
        }
    }


    @GetMapping(
            value = "/number"
            , produces = MediaType.APPLICATION_JSON_VALUE
    )

    public int getNum() throws ExecutionException, InterruptedException {
        value =1;
        System.out.println("Main Thread Start" + Thread.currentThread().getName());

        //2/4/8/16/32

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Callable<Integer> task = () -> {
            Thread.sleep(5000);
            return multiply();
        };

        List<Future<Integer>> futures = new ArrayList<>();
        //Submit future
        for(int i = 0; i< 5; i++) {
            Future<Integer> future = executorService.submit(task);
            futures.add(future);
        }

        for (Future<Integer> future : futures){
            int result = future.get();
            System.out.println(result);
        }



        executorService.shutdown();


        return value;
    }
}