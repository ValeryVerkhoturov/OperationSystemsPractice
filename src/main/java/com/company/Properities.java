package com.company;

import lombok.experimental.UtilityClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@UtilityClass
public class Properities {

    private static final Properties properties = new Properties();

    static {
        String[] paths = {"src/main/resources/conf.properties", "src/main/resources/keystore.properties"};
        for (String path : paths) {
            try(FileInputStream fileInputStream = new FileInputStream(path)){
                properties.load(fileInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }

}
