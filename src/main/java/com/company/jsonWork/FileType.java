package com.company.jsonWork;

import com.company.ConfProperities;
import com.company.processes.DataBaseWriter;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

public enum FileType {
    FIRST(First.class,
            ConfProperities.getProperty("file1path"),
            ConfProperities.getProperty("localaddress"),
            Integer.parseInt(ConfProperities.getProperty("file1port")),
            DataBaseWriter.collections.get(0)),
    SECOND(Second.class,
            ConfProperities.getProperty("file2path"),
            ConfProperities.getProperty("localaddress"),
            Integer.parseInt(ConfProperities.getProperty("file2port")),
            DataBaseWriter.collections.get(1)),
    THIRD(Third.class,
            ConfProperities.getProperty("file3path"),
            ConfProperities.getProperty("localaddress"),
            Integer.parseInt(ConfProperities.getProperty("file3port")),
            DataBaseWriter.collections.get(2));

    @Getter
    private final Class<? extends FileModel> cls;

    @Getter
    private final String path;

    @Getter
    private  final String hostname;

    @Getter
    private  final int port;

    @Getter
    private final MongoCollection<Document> collection;

    FileType(Class<? extends FileModel> cls, String path, String hostname, int port, MongoCollection<Document> collection) {
        this.cls = cls;
        this.path = path;
        this.hostname = hostname;
        this.port = port;
        this.collection = collection;
    }

    public static record First(String id, String text) implements FileModel{}

    public static record Second(String id, List<String> pictures) implements FileModel{}

    public static record Third(String id, List<String> urls) implements FileModel{}
}
