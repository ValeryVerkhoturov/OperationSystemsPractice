package com.company.jsonWork;

import com.company.ConfProperities;
import lombok.Getter;

import java.util.List;

public enum FileType {
    FIRST(ConfProperities.getProperty("file1path"), First.class),
    SECOND(ConfProperities.getProperty("file2path"), Second.class),
    THIRD(ConfProperities.getProperty("file3path"), Third.class);

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
