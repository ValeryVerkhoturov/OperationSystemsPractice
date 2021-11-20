package com.company.jsonWork;

import lombok.Getter;

import java.util.List;

public enum FileType {
    FIRST("src/main/java/com/company/jsonWork/files/file1.json", First.class),
    SECOND("src/main/java/com/company/jsonWork/files/file2.json", Second.class),
    THIRD("src/main/java/com/company/jsonWork/files/file3.json", Third.class);

    @Getter
    private final String path;

    @Getter
    private final Class<? extends FileModel> cls;

    FileType(String path, Class<? extends FileModel> cls) {
        this.path = path;
        this.cls = cls;
    }

    public static record First(String id, String text) implements FileModel{}

    public static record Second(String id, List<String> pictures) implements FileModel{}

    public static record Third(String id, List<String> urls) implements FileModel{}
}
