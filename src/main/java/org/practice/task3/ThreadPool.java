package org.practice.task3;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
    // Внутренняя очередь задач реализована с использованием LinkedList<Runnable>.
    private final LinkedList<Runnable> taskQueue = new LinkedList<>();
    private final Object lock = new Object();
    private volatile boolean isTerminated = false;
    private final List<Thread> workerThreads = new LinkedList<>();

    public ThreadPool(int poolSize) {
        // Конструктор принимает размер пула (количество рабочих потоков).
        // При создании пул сразу инициализирует и запускает все потоки.
        for (int i = 0; i < poolSize; i++) {
            Thread worker = new Thread(new Worker());
            workerThreads.add(worker); // регистрация потока
            worker.start();
            System.out.println("Рабочий поток создан: " + worker.getName());
        }
    }

    public void execute(Runnable task) {
        // Метод execute(Runnable) добавляет переданную задачу в очередь выполнения.
        if (task == null)
            throw new NullPointerException();
        synchronized (lock) {
            if(isTerminated){
                throw new IllegalStateException("Пул остановлен - новые задачи не принимаются");
            }

            taskQueue.add(task);
            System.out.println("Задача добавлена в очередь: " + taskQueue.size());
            lock.notifyAll();
        }

    }
    // Метод shutdown() предотвращает прием новых задач пулом
    // (попытки добавления задач выбрасывают IllegalStateException),
    // и все потоки завершают работу после выполнения оставшихся задач.
    public void shutdown(){
        synchronized (lock) {
            isTerminated = true;
            lock.notifyAll();
        }
    }
    // Дополнительный метод awaitTermination() работает аналогично стандартным пулам потоков
    // и ожидает завершения всех рабочих потоков.
    public void awaitTermination(){
        for (Thread worker : workerThreads) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Все рабочие потоки завершили выполнение");
    }

    private class Worker implements Runnable {
        @Override
        public void run() {
            System.out.println("Запуск рабочего потока: " + Thread.currentThread().getName());

            Runnable task;
            // Свободный поток немедленно приступает к выполнению доступной задачи.
            while (true) {
                synchronized (lock) {
                    // Если очередь задач пуста и установлен флаг завершения, поток прекращает работу
                    while (taskQueue.isEmpty()) {
                        if (isTerminated) {
                            System.out.println("Поток завершает работу: " + Thread.currentThread().getName() + " и больше не принимает задач");
                            return;
                        }
                        try {
                            System.out.println("Ожидание задачи: " + Thread.currentThread().getName());
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    task = taskQueue.removeFirst();
                    System.out.println("Задача взята на выполнение: " + Thread.currentThread().getName());
                }
                try {
                    task.run();
                    System.out.println("Задача выполняется...");
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}