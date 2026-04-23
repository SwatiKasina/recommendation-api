package com.example.recommendation_api.thread;

import java.util.concurrent.Callable;

public class CurryCreation implements Callable<String> {
    private String taskName;

    public CurryCreation (String taskName) {

        this.taskName = taskName;
    }

    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName() + taskName);
        Thread.sleep(2000);
        return taskName;
    }
}
