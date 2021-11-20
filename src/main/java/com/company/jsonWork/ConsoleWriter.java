package com.company.jsonWork;

import lombok.Value;

import java.io.File;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/** Do Thread 4 work */
@Value
public class ConsoleWriter implements Runnable{

    ExecutorService executorService;

    @Override
    public void run() {
        int filesAmmount = FileType.values().length;
        List<ArrayBlockingQueue<List<FileModel>>> blockingQueues = IntStream
                .range(0, filesAmmount)
                .mapToObj(i -> new ArrayBlockingQueue<List<FileModel>>(1, true))
                .toList();
        List<Runnable> fileReaders = IntStream
                .range(0, filesAmmount)
                .mapToObj(i -> (Runnable) new FileReadController(FileType.values()[i], blockingQueues.get(i)))
                .toList();
        observeFiles(filesAmmount, blockingQueues, fileReaders);
    }

    private void observeFiles(int filesAmmount, List<ArrayBlockingQueue<List<FileModel>>> blockingQueues, List<Runnable> fileReaders){
        while (!executorService.isTerminated()){
            IntStream.range(0 , filesAmmount).forEach(i -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!new File(FileType.values()[i].getPath()).exists()) {
                    System.out.println("\nFile " + (i + 1) + " не существует");
                    return;
                }
                System.out.println("\nFile " + (i + 1));
                fileReaders.get(i).run();
                try {
                    blockingQueues.get(i).take().forEach(System.out::println);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
