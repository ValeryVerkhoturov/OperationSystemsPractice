package com.company.jsonWork;

import lombok.Getter;

import java.util.List;

enum FileType {
    FIRST("com/company/jsonWork/files/file1.json", First.class),
    SECOND("com/company/jsonWork/files/file2.json", Second.class),
    THIRD("com/company/jsonWork/files/file3.json", Third.class);

    @Getter
    private String path;

    @Getter
    private Class<FileModel> cls;

    FileType(String path, Class cls) {
        this.path = path;
        this.cls = cls;
    }

    record First(int id, String text) implements FileModel{}

    record Second(int id, List<String> pictures) implements FileModel{}

    record Third(int id, List<String> urls) implements FileModel{}
}
