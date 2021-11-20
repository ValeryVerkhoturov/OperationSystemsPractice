package com.company;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConfProperities {

    Properties properties;

    static {
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = new FileInputStream("src/main/resources/conf.properties");
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Optional.of(fileInputStream).ifPresent(fiStream -> {
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
