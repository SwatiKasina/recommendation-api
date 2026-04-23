package com.example.recommendation_api.controller;


import com.example.recommendation_api.thread.CallableThread;
import com.example.recommendation_api.thread.CurryCreation;
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

    public String getThread () {

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

    public String getCallableThread () throws ExecutionException, InterruptedException {
        System.out.println("Main thread start :" + Thread.currentThread().getName());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable <String> task = new CallableThread();
        Future<String> future = executor.submit(task);
        System.out.println("Main thread continues :");
        String result = future.get();
        return "Task Completed";
    }

    @GetMapping
            (value = "limited",
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCallableThreadOperations(
            @RequestParam(name="numThreads") int numThreads,
            @RequestParam(name="numTasks") int numTasks
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


       @GetMapping
               (value = "/curry",
               produces = MediaType.APPLICATION_JSON_VALUE
               )

        public String makeCurry () throws ExecutionException, InterruptedException {

          System.out.println("Sneha main thread stars" + Thread.currentThread().getName());
          ExecutorService executorService = Executors.newFixedThreadPool(4);

           CurryCreation a1 = new CurryCreation("Swati washing bowls");
           CurryCreation a2 = new CurryCreation("chandra cutting veggies");
           CurryCreation a3 = new CurryCreation("Sneha Cooking Curry");
           CurryCreation a4 = new CurryCreation("raha serving to glass bowls");



           Future <String> future = executorService.submit(a1);
           Future <String> future1 = executorService.submit(a2);
           Future <String> future2 = executorService.submit(a3);
           Future <String> future3 = executorService.submit(a4);

           List <String> result =  new ArrayList<>();
            result.add(future.get());
           result.add(future1.get());
           result.add(future2.get());
           result.add(future3.get());

           executorService.shutdown();

           System.out.println("Curry completed");

           return result.toString();
       }


}

