package com.example.recommendation_api.thread;

import java.util.concurrent.Callable;

public class CallableThread implements Callable<String> {
    @Override
    public String call() throws Exception {
        long start = System.currentTimeMillis();
        System.out.println("Callable start:" + Thread.currentThread().getName());

        Thread.sleep(5000);

        long end = System.currentTimeMillis();
        System.out.println("Callable end:" + Thread.currentThread().getName());

        return "Time taken :" + (end - start) + "ms";
    }


}
