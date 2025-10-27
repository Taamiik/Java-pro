package org.practice;

import org.practice.task3.ThreadPool;

public class Main {
    public static void main(String[] args) {
        // task1
        // TestRunner.runTests(TestClass.class);

        // task2
        // Streams.start();

        // task3
        ThreadPool threadPool = new ThreadPool(2);

        for (int i = 1; i < 10; i++) {
            int taskId = i;
            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000);
                    System.out.println("Начало задачи " + taskId + " в потоке " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        threadPool.shutdown();
        threadPool.awaitTermination();
        System.out.println("Главный поток  = Финал - " + Thread.currentThread().getName());

        try {
            threadPool.execute(() -> {
                System.out.println("Эта задача не должна быть выполнена: " + Thread.currentThread().getName());
            });
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
