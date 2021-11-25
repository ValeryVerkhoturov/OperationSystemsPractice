package com.company;

import lombok.experimental.UtilityClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

@UtilityClass
public class Properities {

    private static Properties properties = new Properties();

    static {
        FileInputStream fileInputStream = null;
        String[] paths = new String[]{"src/main/resources/conf.properties", "src/main/resources/keystore.properties"};
        try{
            for (String path: paths) {
                fileInputStream = new FileInputStream(path);
                properties.load(fileInputStream);
                fileInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Optional.ofNullable(fileInputStream).ifPresent(fiStream -> {
                try {
                    fiStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }

}
