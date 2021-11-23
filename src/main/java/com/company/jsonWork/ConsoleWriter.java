package com.company.jsonWork;

import lombok.SneakyThrows;
import lombok.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/** Do Thread 4 work */
@Value
public class ConsoleWriter implements Runnable{

    ExecutorService executorService;

    @Override
    public void run() {
        List<ArrayBlockingQueue<List<FileModel>>> blockingQueues = new ArrayList<>();
        List<Runnable> fileReaders = new ArrayList<>();
        for (FileType fileType: FileType.values()) {
            ArrayBlockingQueue<List<FileModel>> arrayBlockingQueue = new ArrayBlockingQueue<List<FileModel>>(1, true);
            blockingQueues.add(arrayBlockingQueue);
            fileReaders.add(new FileReadController(fileType, arrayBlockingQueue, true));
        }
        observeFiles(blockingQueues, fileReaders);
    }

    @SneakyThrows
    private void observeFiles(List<ArrayBlockingQueue<List<FileModel>>> blockingQueues, List<Runnable> fileReaders){
        synchronized (ConsoleWriter.class){
            while (!executorService.isTerminated()) {
                TimeUnit.MILLISECONDS.sleep(100);
                int fileNum = 0;
                for (FileType fileType : FileType.values()) {
                    if (!new File(fileType.getPath()).exists())
                        System.out.println("\nFile " + (fileNum + 1) + " не существует");
                    else {
                        System.out.println("\nFile " + (fileNum + 1));
                        fileReaders.get(fileNum).run();
                        blockingQueues.get(fileNum).take().forEach(System.out::println);
                    }
                    fileNum++;
                }
            }
        }
    }
}
