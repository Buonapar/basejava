package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainConcurrency {
    public static final int THREADS_NUMBER = 10000;
    private int counter;
    private static final Object LOCK1 = new Object();
    private static final Object LOCK2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName());

        Thread thread0 = new Thread() {
            @Override
            public void run() {
                System.out.println(getName() + ", " + getState());
                throw new IllegalStateException();
            }
        };
        thread0.start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getState());
            }

            private void inc() {
                synchronized (this) {
//                    counter++;
                }
            }

        }).start();

        System.out.println(thread0.getState());

        final MainConcurrency mainConcurrency = new MainConcurrency();
        List<Thread> threads = new ArrayList<>(THREADS_NUMBER);

        for (int i = 0; i < THREADS_NUMBER; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    mainConcurrency.inc();
                }
            });
            thread.start();
            threads.add(thread);
        }

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(mainConcurrency.counter);

//        deadLock(LOCK1, LOCK2);
//        deadLock(LOCK2, LOCK1);

        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3, 4, 8}));
        System.out.println(oddOrEven(new ArrayList<>(Arrays.asList(1, 2, 3, 3, 2, 3, 4, 8, 1))));
    }

    private synchronized void inc() {
//        synchronized (this) {
//        synchronized (MainConcurrency.class) {
        counter++;
//                wait();
//                readFile
//                ...
//        }
    }

    private static void deadLock(Object lock1, Object lock2) {
        new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Заблокирован поток " + Thread.currentThread().getName());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Ожидаю разблокировки потока " + Thread.currentThread().getName() + "...");
                synchronized (lock2) {
                    System.out.println("Заблокирован поток " + Thread.currentThread().getName());
                }
            }
        }).start();
    }

    private static int minValue(int[] values) {
        AtomicInteger count = new AtomicInteger((int) Arrays.stream(values).distinct().count() - 1);
        return Arrays.stream(values).sorted().distinct().reduce(0, ((left, right) -> left + (int) (right * Math.pow(10, count.getAndDecrement()))));
    }


    private static List<Integer> oddOrEven(List<Integer> integers) {
        return integers.stream().filter(x ->
                (integers.stream().reduce(0, Integer::sum) % 2 == 0) == (x % 2 != 0))
                .collect(Collectors.toList());
    }
}
