package com.company.processes;

import com.company.jsonWork.FileType;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/** Process 2 deamon */
public class ProcessesRunner implements Runnable {

    @SneakyThrows
    public void run() {
        startWriters();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Писатели запущены");
        startReaders();
        System.out.println("Читатели запущены");
    }

    private void startReaders(){
        for (FileType fileType: FileType.values()) {
            Thread thread = new Thread(new FileReader(fileType));
            thread.setDaemon(true);
            thread.start();
        }
    }

    private void startWriters(){
        for (FileType fileType: FileType.values())
            new Thread(new DataBaseWriter(fileType)).start();
    }
}
