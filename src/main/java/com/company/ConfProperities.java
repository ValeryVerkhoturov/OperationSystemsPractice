package com.company;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConfProperities {

    FileInputStream fileInputStream;

    Properties properties;

    static {
        try{
            fileInputStream = new FileInputStream("src/main/resources/conf.properties");
            properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/conf.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert fileInputStream != null;
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }

}
