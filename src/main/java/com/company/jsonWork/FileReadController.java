package com.company.jsonWork;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Value
public class FileReadController implements Runnable{

    // where to read
    FileType fileType;

    // output way
    BlockingQueue<List<FileModel>> blockingQueue;

    boolean isSynchronized;

    @Override
    public void run() {
        if (!isSynchronized){
            readFile();
            return;
        }
        synchronized (fileType){
            readFile();
        }
    }

    private void readFile(){
        ObjectMapper objectMapper = new ObjectMapper();
        List<FileModel> list;
        File file = new File(fileType.getPath());
        try {
            if (file.exists())
                list = objectMapper.readValue(file,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, fileType.getCls()));
            else
                list = new ArrayList<>();
            blockingQueue.put(list);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
