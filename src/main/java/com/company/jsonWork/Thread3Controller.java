package com.company.jsonWork;

/** Запись, дозапись 3.json */
public class Thread3Controller implements Runnable{

    private static Object locker = new Object();

    @Override
    public void run() {
        synchronized (locker){

        }
    }
}
