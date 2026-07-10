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
@RequestMapping("/api/v1/stock")
public class WithSynchronized {
    int stock = 5;

    private final Object valueLock = new Object();

    public int buy() throws InterruptedException {
        synchronized (valueLock) {
          //  int stock = getValueStockFromDB();
            if (stock > 0) {
                int before = stock;
                Thread.sleep(2000);
                stock = stock - 1;
             //   updateValueStockToDB(stock);
                System.out.println(Thread.currentThread().getName() + "Changed stock from" + before + "to" + stock);

                return stock;


            } else {
                System.out.println(Thread.currentThread().getName() + " out of order" + stock);
                return stock;
            }
        }

    }
    @GetMapping("/buy-safe")
    public Integer buyProductSafe() throws Exception {
     stock = 5;
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Callable<Integer> task = () -> buy();
        List<Future<Integer>> futures = new ArrayList<>();
        for ( int i=0; i<4; i++){
            futures.add(executor.submit(task));
        }
         for(Future<Integer> future : futures){
             int result = future.get();
             System.out.println("Returned value = " + result);
         }
         executor.shutdown();
         System.out.println("final stock available" + ":" + stock);
        return stock;
    }

}
