package com.company.jsonWork;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Value
public class FileReadController implements Runnable{

    // where to read
    FileType fileType;

    // output way
    BlockingQueue<List<FileModel>> blockingQueue;

    @Override
    public void run() {
        switch (fileType){
            case FIRST:
                synchronized (FileType.FIRST){
                    try {
                        readFile();
                    } catch (JsonProcessingException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case SECOND:
                synchronized (FileType.SECOND){
                    try {
                        readFile();
                    } catch (JsonProcessingException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case THIRD:
                synchronized (FileType.THIRD){
                    try {
                        readFile();
                    } catch (JsonProcessingException | InterruptedException e) {
                        e.printStackTrace();
                    }
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

    public void readFile() throws JsonProcessingException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<FileModel> list = Arrays.asList(objectMapper.readValue(fileType.getPath(), fileType.getCls()));
        blockingQueue.put(list);
    }
}
