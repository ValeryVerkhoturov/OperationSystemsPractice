package com.company.processes;

import com.company.ConfProperities;
import com.company.jsonWork.FileType;

/** Process 2 deamon */
public class ProcessesRunner implements Runnable {

    public void run() {
        runWriters();
        System.out.println("Писатели запущены");
        runReaders();
        System.out.println("Читатели запущены");
    }

    private void runReaders(){
        String[] portProperties = new String[]{"file1port", "file2port", "file3port"};
        int fileNum = 0;
        for (FileType fileType: FileType.values()) {
            Thread thread = new Thread(
                    new FileReader(
                            fileType,
                            ConfProperities.getProperty("localaddress"),
                            Integer.parseInt(ConfProperities.getProperty(portProperties[fileNum]))));
            thread.run();
            fileNum++;
        }
    }

    private void runWriters(){
        String[] portProperties = new String[]{"file1port", "file2port", "file3port"};
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
