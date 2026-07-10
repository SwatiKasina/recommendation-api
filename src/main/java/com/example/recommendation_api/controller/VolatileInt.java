package com.example.recommendation_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api")

public class VolatileInt {
    volatile int stock =1;

    @GetMapping("/volatile")
    public int volatileExample() throws Exception{
        stock = 1;

        ExecutorService executor = Executors.newFixedThreadPool(10);
        Callable<Integer> task = () ->{
           if(stock>0) {
               Thread.sleep(1000);
               stock = stock - 1;

               System.out.println(Thread.currentThread().getName() + ":" + "Bought stock " + stock);
               return stock;
           }else{
               System.out.println(Thread.currentThread().getName() + ":" +  "out of stock");
           }
            return 0;
        };

        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            futures.add(executor.submit(task));
        }

        for (Future<Integer> future : futures) {
            System.out.println(future.get());
        }

        executor.shutdown();
        System.out.println("Final stock =" + stock);
        return stock;
    }

}
