package com.company;

import com.company.processes.DataBaseWriter;
import com.company.processes.ProcessesRunner;
import com.company.vkFeedMine.BrowserMiner;
import com.company.vkFeedMine.FeedElementsMine;
import lombok.SneakyThrows;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        // Preparation
        deleteJsonFiles();
        dropDataBase();

        // Tasks 11 - 16
        BrowserMiner browserMiner = new BrowserMiner();
        WebElement column = browserMiner.mine();
        FeedElementsMine.findRows(column).forEach(FeedElementsMine::mineFeedRowToFiles);
        browserMiner.closeBrowser();

        // Tasks 17 - 23
        new ProcessesRunner().run();
    }

    private static void deleteJsonFiles(){
        Stream.of("file1path", "file2path", "file3path", "outputpath").map(Properities::getProperty).map(Path::of).forEach(path -> {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    public static void dropDataBase(){
        DataBaseWriter.database.drop();
    }
}
