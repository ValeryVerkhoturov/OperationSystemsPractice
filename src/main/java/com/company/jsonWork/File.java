package com.company.jsonWork;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

public enum File {
    FIRST(First.class, "com/company/jsonWork/Files/file1.json"),
    SECOND(Second.class, "com/company/jsonWork/Files/file2.json"),
    THIRD(Third.class, "com/company/jsonWork/Files/file3.json");

    @Getter
    private final Class<?> aClass;

    @Getter
    private final String path;

    File(Class<?> aClass, String path) {
        this.aClass = aClass;
        this.path = path;
    }

    @Data
    @Builder
    public static class First {
        int id;
        String text;
    }

    @Data
    @Builder
    public static class Second {
        int id;
        List<String> pictures;
    }

    @Data
    @Builder
    public static class Third {
        int id;
        List<String> urls;
    }
}
