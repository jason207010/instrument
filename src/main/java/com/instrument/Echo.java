package com.instrument;

public class Echo implements Runnable {

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            echo();
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void echo() {
        System.out.println("1+1=3");
    }
}
