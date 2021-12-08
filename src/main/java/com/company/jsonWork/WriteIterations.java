package com.company.jsonWork;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class WriteIterations {

    private static final ConcurrentHashMap<FileType, Queue<FileWriteController>> fileWritersMap = new ConcurrentHashMap<>();

    private static AtomicInteger iteration = new AtomicInteger(-1);

    static {
        for (FileType fileType: FileType.values())
            fileWritersMap.put(fileType, new ConcurrentLinkedDeque<>());
    }

    public void addIteration(HashMap<FileType, FileWriteController> fileWriterMap){
        for (FileType fileType:fileWriterMap.keySet())
            fileWritersMap.get(fileType).offer(fileWriterMap.get(fileType));

        new Thread(WriteIterations::startIterations).start();
    }

    @SneakyThrows
    private synchronized void startIterations(){
        ArrayList<Runnable> controllerList = new ArrayList<>();

        while (Arrays.stream(FileType.values()).anyMatch(fileType -> fileWritersMap.get(fileType).size() > 0)) {
            switch (iteration.addAndGet(1) % (FileType.values().length + 1)) {
                case 0 -> {
                    System.out.println("Итерация 1I");
                    Optional.ofNullable(fileWritersMap.get(FileType.FIRST).poll()).ifPresent(controllerList::add);
                    Optional.ofNullable(fileWritersMap.get(FileType.SECOND).poll()).ifPresent(controllerList::add);
                    Optional.ofNullable(fileWritersMap.get(FileType.THIRD).poll()).ifPresent(controllerList::add);
                }
                case 1 -> {
                    System.out.println("Итерация 2I");
                    controllerList.add(
                            new FileReadController(
                                    FileType.FIRST, new ArrayBlockingQueue<>(1, true), true));
                    Optional.ofNullable(fileWritersMap.get(FileType.SECOND).poll()).ifPresent(controllerList::add);
                    Optional.ofNullable(fileWritersMap.get(FileType.THIRD).poll()).ifPresent(controllerList::add);
                }
                case 2 -> {
                    System.out.println("Итерация 3I");
                    Optional.ofNullable(fileWritersMap.get(FileType.FIRST).poll()).ifPresent(controllerList::add);
                    controllerList.add(
                            new FileReadController(
                                    FileType.SECOND, new ArrayBlockingQueue<>(1, true), true));
                    Optional.ofNullable(fileWritersMap.get(FileType.THIRD).poll()).ifPresent(controllerList::add);
                }
                case 3 -> {
                    System.out.println("Итерация 4I");
                    Optional.ofNullable(fileWritersMap.get(FileType.FIRST).poll()).ifPresent(controllerList::add);
                    Optional.ofNullable(fileWritersMap.get(FileType.SECOND).poll()).ifPresent(controllerList::add);
                    controllerList.add(
                            new FileReadController(
                                    FileType.THIRD, new ArrayBlockingQueue<>(1, true), true));
                }
                default -> throw new Exception("Unexpected Iteration");
            }
        }

        startAndJoinThreads(controllerList);
    }

    private void startAndJoinThreads(List<Runnable> runnableList){
        List<Thread> threadList = runnableList.stream().map(Thread::new).toList();
        threadList.forEach(Thread::start);
        threadList.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
