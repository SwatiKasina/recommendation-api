package com.example.recommendation_api.thread;

public class RunnableThread implements Runnable{
    @Override
    public void run() {

         long startTime = System.currentTimeMillis();
         System.out.println("Worker started at :" + startTime + " | Thread : " + Thread.currentThread().getName());

    try {
        Thread.sleep(6000);
    } catch (InterruptedException e){
        System.out.println("Thread interrupted : " + e.getMessage());
    }

    long endTime = System.currentTimeMillis();
    System.out.println("Worker finished at: " + endTime + " | Thread :" + Thread.currentThread().getName());


     System.out.println("Total time taken:" + (endTime - startTime) + "ms");











    }
}
