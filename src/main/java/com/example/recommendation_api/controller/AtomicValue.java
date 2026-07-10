package com.example.recommendation_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/v1/stock")
public class AtomicValue {

    AtomicInteger stock = new AtomicInteger(5);

    @GetMapping("/buy-atomic")
    public Integer buyProductSafe() throws Exception {

        stock.set(5);

        ExecutorService executor = Executors.newFixedThreadPool(4);

        Callable<Integer> task = () -> {
                int result = stock.updateAndGet(v -> {
                    if(v > 0){
                        return v - 1;
                }

                    return v;
                    });

               System.out.println(Thread.currentThread().getName() + "updated value" + result );

            return result;
        };


        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            futures.add(executor.submit(task));
        }

        for (Future<Integer> future : futures) {
            Integer result = future.get();
            System.out.println("Thread result = " + result);
        }

        executor.shutdown();
        System.out.println(stock.get());

        return stock.get();
    }
}