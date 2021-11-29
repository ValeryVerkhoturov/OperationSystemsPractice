package com.company.jsonWork;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class WriteIterations {

    private static final ConcurrentHashMap<FileType, Queue<FileWriteController>> fileWritersMap = new ConcurrentHashMap<>();

    public static ExecutorService executorService = Executors.newFixedThreadPool(FileType.values().length);

    private static AtomicInteger iteration = new AtomicInteger(-1);

    static {
        for (FileType fileType: FileType.values())
            fileWritersMap.put(fileType, new ConcurrentLinkedDeque<>());
    }

    public void addIteration(HashMap<FileType, FileWriteController> fileWriterMap){
        for (FileType fileType:fileWriterMap.keySet()) {
            fileWritersMap.get(fileType).offer(fileWriterMap.get(fileType));
        }
        startIterations();
    }

    @SneakyThrows
    private synchronized void startIterations(){
        while (Arrays.stream(FileType.values()).allMatch(fileType -> fileWritersMap.get(fileType).size() > 0)) {
            switch (iteration.addAndGet(1) % (FileType.values().length + 1)) {
                case 0 -> {
                    System.out.println("Итерация 1I");
                    executorService.execute(fileWritersMap.get(FileType.FIRST).remove());
                    executorService.execute(fileWritersMap.get(FileType.SECOND).remove());
                    executorService.execute(fileWritersMap.get(FileType.THIRD).remove());
                }
                case 1 -> {
                    System.out.println("Итерация 2I");
                    executorService.execute(
                            new FileReadController(
                                    FileType.FIRST, new ArrayBlockingQueue<>(1, true), true));
                    executorService.execute(fileWritersMap.get(FileType.SECOND).remove());
                    executorService.execute(fileWritersMap.get(FileType.THIRD).remove());
                }
                case 2 -> {
                    System.out.println("Итерация 3I");
                    executorService.execute(fileWritersMap.get(FileType.FIRST).remove());
                    executorService.execute(
                            new FileReadController(
                                    FileType.SECOND, new ArrayBlockingQueue<>(1, true), true));
                    executorService.execute(fileWritersMap.get(FileType.THIRD).remove());
                }
                case 3 -> {
                    System.out.println("Итерация 4I");
                    executorService.execute(fileWritersMap.get(FileType.FIRST).remove());
                    executorService.execute(fileWritersMap.get(FileType.SECOND).remove());
                    executorService.execute(
                            new FileReadController(
                                    FileType.THIRD, new ArrayBlockingQueue<>(1, true), true));
                }
                default -> throw new Exception("Unexpected Iteration");
            }
        }
    }
}
