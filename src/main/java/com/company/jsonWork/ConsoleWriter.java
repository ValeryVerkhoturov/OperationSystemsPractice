package com.company.jsonWork;

import lombok.SneakyThrows;
import lombok.Value;

import java.io.File;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/** Do Thread 4 work */
@Value
public class ConsoleWriter implements Runnable{

    FileType fileType;

    @SneakyThrows
    @Override
    public void run() {
        System.out.println("Чтение");
        if (!new File(fileType.getPath()).exists())
            return;
        BlockingQueue<List<FileModel>> blockingQueue = new ArrayBlockingQueue<>(1, true);
        FileReadController fileReadController = new FileReadController(fileType, blockingQueue, true);
        fileReadController.run();
    }
}
