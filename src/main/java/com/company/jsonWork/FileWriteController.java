package com.company.jsonWork;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.SneakyThrows;
import lombok.Value;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Value
public class FileWriteController implements Runnable{

    // what to write
    FileModel object;

    // where to write
    FileType fileType;

    @SneakyThrows
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

    private void writeInFile() throws JsonProcessingException, InterruptedException {
        BlockingQueue<List<FileModel>> blockingQueue = new ArrayBlockingQueue<>(1, true);
        FileReadController fileReadController = new FileReadController(fileType, blockingQueue);
        fileReadController.readFile();
        List<FileModel> list = blockingQueue.take();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
}
