package com.company.jsonWork;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/** Запись, дозапись 1.json */
public class File1WriteController implements Runnable{

    private final String filePath = "src/main/resources/1.json";

    private static Object locker = new Object();

    private final File1Template object;

    public File1WriteController(int id, String text){
        object = new File1Template(id, text);
    }
    
    @Override
    public void run() {
        synchronized (locker){
            ObjectMapper mapper = new ObjectMapper();
            try {
                List<File1Template> objects = mapper.readValue(new File(filePath), new TypeReference<List<File1Template>>(){});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
