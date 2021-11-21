package com.company;

import lombok.experimental.UtilityClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

@UtilityClass
public class ConfProperities {

    private static Properties properties;

    static {
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = new FileInputStream("src/main/resources/conf.properties");
            properties = new Properties();
            properties.load(fileInputStream);
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
