package com.company.processes;

import com.company.ConfProperities;
import com.company.jsonWork.FileType;

/** Process 2 deamon */
public class ProcessesRunner implements Runnable {

    private static String[] portProperties = new String[]{"file1port", "file2port", "file3port"};
    public void run() {
        startWriters();
        System.out.println("Писатели запущены");
        startReaders();
        System.out.println("Читатели запущены");
    }

    private void startReaders(){
        int fileNum = 0;
        for (FileType fileType: FileType.values()) {
            Thread thread = new Thread(
                    new FileReader(
                            fileType,
                            ConfProperities.getProperty("localaddress"),
                            Integer.parseInt(ConfProperities.getProperty(portProperties[fileNum]))));
            thread.setDaemon(true);
            thread.start();
            fileNum++;
        }
    }

    private void startWriters(){
        int fileNum = 0;
        for (FileType fileType: FileType.values()) {
            Thread thread = new Thread(
                    new DataBaseWriter(
                            fileType,
                            ConfProperities.getProperty("localaddress"),
                            Integer.parseInt(ConfProperities.getProperty(portProperties[fileNum]))));
            thread.start();
            fileNum++;
        }
    }
}
