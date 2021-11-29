package com.company.processes;

import com.company.Properities;
import com.company.jsonWork.FileModel;
import com.company.jsonWork.FileType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.stream.Stream;

/** Process 1 */
public class DataBaseWriter implements Runnable{

    FileType fileType;

    public static final List<MongoCollection<Document>> collections;

    public static final MongoDatabase database;

    public DataBaseWriter(FileType fileType){
        this.fileType = fileType;
    }

    static {
        ConnectionString connectionString = new ConnectionString("mongodb+srv://"
                + Properities.getProperty("dbuser") + ":"
                + Properities.getProperty("dbpassword") + "@"
                + Properities.getProperty("cluster")
                + ".w1rpn.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString).build();
        MongoClient mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(Properities.getProperty("dbname"));
        collections = Stream.of(Properities.getProperty("file1dbcollection"),
                Properities.getProperty("file2dbcollection"),
                Properities.getProperty("file3dbcollection")).
                map(database::getCollection).toList();
    }

    @SneakyThrows
    @Override
    public void run() {
        @Cleanup ServerSocket serverSocket = new ServerSocket(fileType.getPort(), 1, InetAddress.getByName(fileType.getHostname()));
        @Cleanup Socket socket = serverSocket.accept();

        @Cleanup BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());

        ObjectMapper objectMapper = new ObjectMapper();
        List<FileModel> list = objectMapper.readValue(bufferedInputStream.readAllBytes(), objectMapper.getTypeFactory().constructCollectionType(List.class, fileType.getCls()));

        insertInCollection(list);
    }

    private void insertInCollection(List<FileModel> list){
        switch (fileType){
            case FIRST -> list.stream().map(item -> (FileType.First) item).forEach(this::insertInCollection);
            case SECOND -> list.stream().map(item -> (FileType.Second) item).forEach(this::insertInCollection);
            case THIRD -> list.stream().map(item -> (FileType.Third) item).forEach(this::insertInCollection);
        }
        System.out.println("Список занесен в бд");
    }

    private void insertInCollection(FileType.First object){
        Document document = new Document();
        document.put("_id", object.id());
        document.put("text", object.text());
        FileType.FIRST.getCollection().insertOne(document);
    }

    private void insertInCollection(FileType.Second object){
        Document document = new Document();
        document.put("_id", object.id());
        document.put("pictures", object.pictures());
        FileType.SECOND.getCollection().insertOne(document);
    }

    private void insertInCollection(FileType.Third object){
        Document document = new Document();
        document.put("_id", object.id());
        document.put("urls", object.urls());
        FileType.THIRD.getCollection().insertOne(document);
    }

}
