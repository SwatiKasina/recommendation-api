package com.example.recommendation_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor

public class WithoutSynchronized {

    int stock = 5;

@GetMapping(value = "/buy", produces = MediaType.APPLICATION_JSON_VALUE)

    public Integer buyProduct () throws ExecutionException, InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(4);

    Callable<Integer> task = () -> {
        if (stock > 0){
            int before = stock;
            Thread.sleep(2000);
            stock = stock -1;
            System.out.println(Thread.currentThread().getName() + " changed stock from " + before + " to" + stock);

            return stock;

         } else {
            System.out.println(Thread.currentThread().getName() + " stock not available" + stock);
            return stock ;
        }


    };

    List<Future<Integer>> futures = new ArrayList<>();
    for ( int i=0; i<4; i++){
        Future<Integer> f1 = executor.submit(task);
        futures.add(f1);
    }

    for(Future<Integer> future : futures){
        Integer result = future.get();
        System.out.println(result);

    }
    executor.shutdown();
    System.out.println("final stock" + stock);

    return stock;
}
}
