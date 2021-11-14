package com.company.jsonWork;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Builder
public class FileWriteController implements Runnable{

    private final File file;

    @Override
    public void run() {
        synchronized (file){
            ObjectMapper mapper = new ObjectMapper();
            try {
                List objects = mapper.readValue(new java.io.File(file.getPath()), new TypeReference<List<file.>>(){});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
