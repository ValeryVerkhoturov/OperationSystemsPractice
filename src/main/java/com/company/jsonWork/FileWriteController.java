package com.company.jsonWork;

import com.company.ConfProperities;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Value
public class FileWriteController implements Runnable{

    // what to write
    FileModel object;

    // where to write
    FileType fileType;

    @Override
    public void run() {
        switch (fileType){
            case FIRST:
                synchronized (FileType.FIRST){
                    writeInFile();
                }
                break;
            case SECOND:
                synchronized (FileType.SECOND){
                    writeInFile();
                }
                break;
            case THIRD:
                synchronized (FileType.THIRD){
                    writeInFile();
                }
                break;
            default:
                try {
                    throw new FileNotFoundException("Unexpected file");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
        }
    }

    private void writeInFile() {
        List<FileModel> list = readExistingFile();
        list.add(object);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        File file = new File(fileType.getPath());
        checkExistingOutputDirectory();
        try {
            checkExistingFile(file);
            objectMapper.writeValue(file, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<FileModel> readExistingFile(){
        BlockingQueue<List<FileModel>> blockingQueue = new ArrayBlockingQueue<>(1, true);
        FileReadController fileReadController = new FileReadController(fileType, blockingQueue, false);
        fileReadController.run();
        List<FileModel> list;
        try {
            list = blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            list = new ArrayList<>();
        }
        return list;
    }

    private void checkExistingOutputDirectory(){
        File directory = new File(ConfProperities.getProperty("outputpath"));
        if (!directory.exists())
            directory.mkdirs();
    }

    private void checkExistingFile(File file) throws IOException {
        if (!file.exists())
            file.createNewFile();
    }
}
