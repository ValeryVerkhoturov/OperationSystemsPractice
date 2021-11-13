package com.company.jsonWork;

/** Чтение из 1.json, 2.json, 3.json */
public class Thread4Controller implements Runnable{

    private final String filePath = "";

    private static Object locker = new Object();

    private final File1Template object;

    public Thread4Controller(int id, String text){
        object = new File1Template(id, text);
    }
    @Override
    public void run() {
        synchronized (locker){

        }
    }
}