package com.example.recommendation_api.controller;


import com.example.recommendation_api.thread.CallableThread;
import com.example.recommendation_api.thread.RunnableThread;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RestController
@RequestMapping("api/v1/thread")
@RequiredArgsConstructor


public class ThreadController {
    @GetMapping
            (value = "/runnable",
                    produces = MediaType.APPLICATION_JSON_VALUE
            )

    public String getThread() {

        System.out.println("Main thread start :" + Thread.currentThread().getName());
        Thread thread = new Thread(new RunnableThread());
        thread.start();
        System.out.println("Main thread continues:" + Thread.currentThread().getName());
        return "Thread check";
    }

    @GetMapping
            (value = "/callable",
                    produces = MediaType.APPLICATION_JSON_VALUE
            )

    public String getCallableThread() throws ExecutionException, InterruptedException {
        System.out.println("Main thread start :" + Thread.currentThread().getName());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> task = new CallableThread();
        Future<String> future = executor.submit(task);
        System.out.println("Main thread continues :");
        String result = future.get();
        return "Task Completed";
    }

    @GetMapping
            (value = "limited",
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCallableThreadOperations(
            @RequestParam(name = "numThreads") int numThreads,
            @RequestParam(name = "numTasks") int numTasks
    )
            throws ExecutionException, InterruptedException {
        System.out.println("Main thread START: " + Thread.currentThread().getName());

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        List<Future<String>> futures = new ArrayList<>();
        for (int i = 1; i <= numTasks; i++) {
            CallableThread task = new CallableThread();
            Future<String> future = executor.submit(task);
            futures.add(future);
        }

        System.out.println("Main continues...");

        StringBuilder result = new StringBuilder();

        int count = 1;
        for (Future<String> future : futures) {
            String res = future.get(); // waits
            result.append("Task ")
                    .append(count++)
                    .append(": ")
                    .append(res)
                    .append("\n");
        }

        executor.shutdown();

        return result.toString();
    }

    // ---------------------------------------------------------------------------------------------------------------

    @GetMapping(
            value = "/start/curry",
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public String makeCurry() throws ExecutionException, InterruptedException {
        System.out.println("Main Thread Started :" + Thread.currentThread().getName());
        ExecutorService executors = Executors.newFixedThreadPool(2);

        List<Future<String>> futures = new ArrayList<>();  // create an empty list to store future result i dont have result yet
        Future<String> washingFuture = executors.submit(washDishes);
        Future<String> cuttingFuture = executors.submit(cutVeggies);
        futures.add(washingFuture);
        futures.add(cuttingFuture);

        System.out.println("Main thread continues :" + Thread.currentThread().getName());
        StringBuilder result = new StringBuilder();

        int count = 1;
        for (Future<String> future : futures) {
            String res = future.get();
            result.append("Task").append(count++).append(": ").append(res).append("\n");
        }

        System.out.println(" Sneha started curry : " + Thread.currentThread().getName());
        Thread.sleep(2000);
        System.out.println("Sneha ended curry : " + Thread.currentThread().getName());

        result.append("Sneha completed cooking curry");


        System.out.println(" Raha Started serving : " + Thread.currentThread().getName());
        Thread.sleep(2000);
        System.out.println(" Raha Ended  serving : " + Thread.currentThread().getName());

        result.append("Raha finished serving curry");

        return result.toString();
    }


    private Callable<String> washDishes = () -> {
        System.out.println("Washing dishes started" + Thread.currentThread().getName());
        Thread.sleep(5000);
        System.out.println("Washing dishes ended" + Thread.currentThread().getName());
        return "Swati completed washing dishes";
    };


    private Callable<String> cutVeggies = () -> {
        System.out.println("Cutting veggies started" + Thread.currentThread().getName());
        Thread.sleep(2000);
        System.out.println("Cutting veggies ended" + Thread.currentThread().getName());
        return "Chandu finished cutting veggies";
    };

//============================================================================================
    @GetMapping
            (
                    value = "/start/curry/completable",
                    produces = MediaType.APPLICATION_JSON_VALUE
            )

    public String completableMethod(){
        System.out.println("Main thread started :" + Thread.currentThread().getName());
        StringBuilder result = new StringBuilder();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        CompletableFuture <String> washingFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return washDishes.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor);

        CompletableFuture <String> cuttingVeggiesFuture = CompletableFuture.supplyAsync(() -> {
           try {
               return cutVeggies.call();

           } catch (Exception e){
               throw new RuntimeException(e);
           }
        }, executor);

        CompletableFuture<String> finalFuture = washingFuture.thenCombine(cuttingVeggiesFuture,
                (washDishes, cutVeggies) -> {
            result.append("Task 1 :" ).append(washDishes).append("\n");
            result.append("Task 2 :").append(cutVeggies).append("\n");
            return result.toString();
                })

                .thenCompose(previousResult ->
                        CompletableFuture.supplyAsync(()-> {
                            try {
                                System.out.println("Sneha Started curry" + Thread.currentThread().getName());
                                Thread.sleep(2000);
                                System.out.println("Sneha ended curry" + Thread.currentThread().getName());
                           return previousResult + "Task 3: Sneha Completed Curry\n";

                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }, executor )
        )
                .thenCompose(previousResult ->
                        CompletableFuture.supplyAsync(() -> {
                            try{
                                System.out.println("Raha curry start" + Thread.currentThread().getName());
                                  Thread.sleep(4000);
                                  System.out.println("Raha curry end" + Thread.currentThread().getName());
                           return previousResult + "Task 4 : Raha finished serving\n";

                            } catch (Exception e){
                                throw  new RuntimeException(e);
                            }

        }, executor )
                );
            System.out.println("Main thread continues before join");
            String response = finalFuture.join();
            System.out.println("Main continues after join");
            executor.shutdown();
            System.out.println("Main Continues... after all shutdown");
            return response;
    }
    //========================================================================================
    @GetMapping (
            value = "/start/curry/sequence",
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public  String curryInSequence () {
        System.out.println("Main Thread Started :" + Thread.currentThread().getName());
        ExecutorService executors = Executors.newFixedThreadPool(2);
        StringBuilder result = new StringBuilder();

        CompletableFuture<String> finalFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return washDishes.call();
            } catch (Exception e){
                throw new RuntimeException(e);
            }

        }, executors)

                .thenCompose(washDishes -> {
                    result.append("Task 1" ).append(washDishes).append("\n");
                    return CompletableFuture.supplyAsync(() -> {
                        try {
                            return cutVeggies.call();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }, executors);
                })

                .thenCompose(veggiesResult -> {
                    result.append("Task 2 : ").append(veggiesResult).append("\n");

                    return CompletableFuture.supplyAsync(() -> {
                        try {
                            System.out.println("Sneha Curry START: " + Thread.currentThread().getName());
                            Thread.sleep(2000);
                            System.out.println("Sneha Curry END: " + Thread.currentThread().getName());

                            return "Sneha done with curry";
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }, executors);
                })

                .thenCompose(curryResult -> {
                    result.append("Task 3 : ").append(curryResult).append("\n");

                    return CompletableFuture.supplyAsync(() -> {
                        try {
                            System.out.println("Raha Serving START: " + Thread.currentThread().getName());
                            Thread.sleep(4000);
                            System.out.println("Raha Serving END: " + Thread.currentThread().getName());

                            return "Raha done with Serving";
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }, executors);
                })

                .thenApply(servingResult -> {
                    result.append("Task 4 : ").append(servingResult).append("\n");
                    return result.toString();
                });

        String response = finalFuture.join();

        executors.shutdown();

        return response;
    }
  //  ---------------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping(
            value = "/value",
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public int getValue() throws ExecutionException, InterruptedException {
        System.out.println("Main thread Started" + Thread.currentThread().getName());

        final int[] value = {1};
        ExecutorService executor = Executors.newFixedThreadPool(5) ;

        Callable<Integer> t1 = () ->{

                value[0] = value[0] * 2;
                return value[0];

        };

        List<Future<Integer>> futures = new ArrayList<>();
        for(int i =0; i< 5; i++){
            Future<Integer> f1 = executor.submit(t1);
            futures.add(f1);
        }

        for (Future<Integer> future : futures){
            int result =future.get();
            System.out.println(result);
        }
        executor.shutdown();
        return value[0];
    }
}