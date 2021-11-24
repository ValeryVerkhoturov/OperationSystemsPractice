package com.company;

import com.company.processes.ProcessesRunner;
import com.company.vkFeedMine.BrowserMiner;
import com.company.vkFeedMine.FeedElementsMine;
import lombok.SneakyThrows;
import org.openqa.selenium.WebElement;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        // tasks 11 - 16
        BrowserMiner browserMiner = new BrowserMiner();
        WebElement column = browserMiner.mine();
        FeedElementsMine.findRows(column).forEach(FeedElementsMine::mineFeedRowToFiles);
        browserMiner.closeBrowser();
        // tasks 17 - 23
        new ProcessesRunner().run();
    }
}
