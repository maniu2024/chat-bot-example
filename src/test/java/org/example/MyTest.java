package org.example;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyTest {


    public static void main(String[] args) {
        String abc = "abckkkkjkl999ikjksjdkfj";
        String[] split = abc.split("999");
        System.out.println(split[0]);
    }


    @Test
    public void virtualThread() {
        Runnable runnable = () -> {
            System.out.println("Hello, www.didispace.com");
        };

        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 100; i++) {
                executorService.submit(runnable);
            }
        }
    }

}
