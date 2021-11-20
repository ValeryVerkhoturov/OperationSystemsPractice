package com.company.jsonWork;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;
import org.openqa.selenium.remote.tracing.opentelemetry.SeleniumSpanExporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
                    readFile();
                }
                break;
            case SECOND:
                synchronized (FileType.SECOND){
                    readFile();
                }
                break;
            case THIRD:
                synchronized (FileType.THIRD){
                    readFile();
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

    public void readFile(){
        ObjectMapper objectMapper = new ObjectMapper();
        List<FileModel> list;
        File file = new File(fileType.getPath());
        try {
            if (file.exists())
                list = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, fileType.getCls()));
            else
                list = new ArrayList<>();
            blockingQueue.put(list);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
