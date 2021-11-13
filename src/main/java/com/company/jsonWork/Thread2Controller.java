package com.company.jsonWork;

/** Запись, дозапись 2.json */
public class Thread2Controller implements Runnable{

    private static Object locker = new Object();

    @Override
    public void run() {
        synchronized (locker){

        }
    }
}