package com.example.recommendation_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("api/v1/thread")
@RequiredArgsConstructor


public class AtomicityInt {

 AtomicInteger value = new AtomicInteger(1);

 @GetMapping(value ="/atomic", produces = MediaType.APPLICATION_JSON_VALUE)
 public  int atomicityInt() throws ExecutionException, InterruptedException {
     value.set(1);
     ExecutorService executor = Executors.newFixedThreadPool(5);
     Callable<Integer> task =() -> {
         int result = value.updateAndGet(v -> v * 2);
         System.out.println(Thread.currentThread().getName() + " " + result);
         return result;
     };


        List<Future<Integer>> futures = new ArrayList<>();
        //Submit future
        for(int i = 0; i< 5; i++) {
            Future<Integer> future = executor.submit(task);
            futures.add(future);
        }
        for (Future<Integer> future : futures){
            int result = future.get();
            System.out.println(result);
        }
        executor.shutdown();
        return  value.get();

    }
}