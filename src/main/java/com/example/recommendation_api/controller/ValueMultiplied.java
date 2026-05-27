package com.example.recommendation_api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ValueMultiplied {

int value = 1;
    ExecutorService executor = Executors.newFixedThreadPool(5);


    Callable<Integer> task = new Callable<Integer>() {
        @Override
        public Integer call() throws Exception {
           value = value * 2;

            return value;
        }
    };

    public void runTask() throws ExecutionException, InterruptedException {

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


    }

    public static void main (String[]args) throws ExecutionException, InterruptedException{
    ValueMultiplied vm = new ValueMultiplied();
        try {
            vm.runTask();
        } catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
    }

}



