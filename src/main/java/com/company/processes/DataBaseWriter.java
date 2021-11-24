package com.company.processes;

import com.company.ConfProperities;
import com.company.Credentails;
import com.company.jsonWork.FileModel;
import com.company.jsonWork.FileType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.*;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/** Process 1 */
public class DataBaseWriter implements Runnable{

    FileType fileType;

    public static final List<MongoCollection<Document>> collections;

    public DataBaseWriter(FileType fileType){
        this.fileType = fileType;
    }

    static {
        ConnectionString connectionString = new ConnectionString("mongodb+srv://"
                + Credentails.DB_USER + ":"
                + Credentails.DB_PASSWORD + "@"
                + Credentails.DB_CLUSTER
                + ".w1rpn.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(ConfProperities.getProperty("dbname"));
        collections = List.of(ConfProperities.getProperty("file1dbcollection"),
                ConfProperities.getProperty("file2dbcollection"),
                ConfProperities.getProperty("file3dbcollection"))
                .stream().map(mongoDatabase::getCollection).toList();
    }

    @SneakyThrows
    @Override
    public void run() {
        ServerSocket serverSocket = new ServerSocket(fileType.getPort(), 20, InetAddress.getByName(fileType.getHostname()));
        Socket socket = serverSocket.accept();

        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        DataInputStream dis = new DataInputStream(bis);

        long fileLength = dis.readLong();
        System.out.println(fileLength);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);

        ObjectMapper objectMapper = new ObjectMapper();
        List<FileModel> list = objectMapper.readValue(bis.readNBytes(Math.toIntExact(fileLength)), objectMapper.getTypeFactory().constructCollectionType(List.class, fileType.getCls()));
        insertInCollection(list);
        serverSocket.close();
        socket.close();
        bis.close();
        dis.close();
        baos.close();
        bos.close();
    }

    private void insertInCollection(List<FileModel> list){
        Document document = new Document();
        switch (fileType){
            case FIRST -> list.stream().map(item -> (FileType.First) item).forEach(this::insertInCollection);
            case SECOND -> list.stream().map(item -> (FileType.Second) item).forEach(this::insertInCollection);
            case THIRD -> list.stream().map(item -> (FileType.Third) item).forEach(this::insertInCollection);
        }
    }

    private void insertInCollection(FileType.First pojo){
        Document document = new Document();
        document.put("_id", pojo.id());
        document.put("text", pojo.text());
        FileType.FIRST.getCollection().insertOne(document);
    }

    private void insertInCollection(FileType.Second pojo){
        Document document = new Document();
        document.put("_id", pojo.id());
        document.put("pictures", pojo.pictures());
        FileType.SECOND.getCollection().insertOne(document);
    }

    private void insertInCollection(FileType.Third pojo){
        Document document = new Document();
        document.put("_id", pojo.id());
        document.put("urls", pojo.urls());
        FileType.THIRD.getCollection().insertOne(document);
    }

}
