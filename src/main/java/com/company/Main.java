package com.company;

import com.company.processes.ProcessesRunner;
import com.company.vkFeedMine.BrowserMiner;
import com.company.vkFeedMine.FeedElementsMine;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.DBCollection;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.SneakyThrows;
import org.bson.Document;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
//        // tasks 11 - 16
//        BrowserMiner browserMiner = new BrowserMiner();
//        WebElement column = browserMiner.mine();
//        FeedElementsMine.findRows(column).forEach(FeedElementsMine::mineFeedRowToFiles);
//        browserMiner.closeBrowser();
        ProcessesRunner processesRunner = new ProcessesRunner();
        processesRunner.run();
    }
}
