package com.company.jsonWork;

import lombok.SneakyThrows;
import lombok.Value;
import org.checkerframework.common.value.qual.IntRange;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

/** Do Thread 4 work */
@Value
public class ConsoleWriter implements Runnable{

    ExecutorService executorService;

    @Override
    public void run() {
        int fileAmmount = FileType.values().length;
        List<ArrayBlockingQueue<List<FileModel>>> blockingQueues = IntStream
                .range(0, fileAmmount)
                .mapToObj(i -> new ArrayBlockingQueue<List<FileModel>>(1, true))
                .toList();
        List<Runnable> fileReaders = IntStream
                .range(0, fileAmmount)
                .mapToObj(i -> (Runnable) new FileReadController(FileType.values()[i], blockingQueues.get(i)))
                .toList();
        while (!executorService.isTerminated()){
            IntStream.range(0 , fileAmmount).forEach(i -> {
                System.out.println("File " + (i + 1));
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
