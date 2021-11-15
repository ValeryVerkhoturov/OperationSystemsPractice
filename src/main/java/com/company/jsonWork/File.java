package com.company.jsonWork;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

enum File {
    FIRST("com/company/jsonWork/Files/file1.json"),
    SECOND("com/company/jsonWork/Files/file2.json"),
    THIRD("com/company/jsonWork/Files/file3.json");

    @Getter
    private String path;

    File(String path) {
        this.path = path;
    }

    public java.io.File getFileInstance(){
        return new java.io.File(path);
    }

    @Data
    public static class First {
        int id;
        String text;
    }

    @Data
    public static class Second {
        int id;
        List<String> pictures;
    }

    @Data
    public static class Third {
        int id;
        List<String> urls;
    }
}
