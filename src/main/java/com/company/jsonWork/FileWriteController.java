package com.company.jsonWork;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Builder
public class FileWriteController implements Runnable{

    private final File file;

    @Override
    public void run() {
        switch (file){
            case FIRST:
                synchronized (File.FIRST){
                    writeInFirstFile(file);
                }
                break;
            case SECOND:
                synchronized (File.SECOND){
                    writeInSecondFile(file);
                }
                break;
            case THIRD:
                synchronized (File.THIRD){
                    writeInThirdFile(file);
                }
                break;
            default:
                try {
                    throw new FileNotFoundException("Unexpected file writing");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
        }
//        synchronized (file){
//            ObjectMapper mapper = new ObjectMapper();
//            try {
//                List objects = mapper.readValue(new java.io.File(file.getPath()), new TypeReference<List<file.>>(){});
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private void writeInFirstFile(File file){
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<File.First> objects = mapper.readValue(file.getFileInstance(), new TypeReference<List<File.First>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeInSecondFile(File file) {

    }

    private void writeInThirdFile(File file) {

    }
}
