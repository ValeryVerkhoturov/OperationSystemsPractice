package com.company;

import com.company.jsonWork.FileType;
import com.company.jsonWork.FileWriteController;
import com.company.vkFeedMine.BrowserMiner;
import com.company.vkFeedMine.FeedElementsMine;
import org.openqa.selenium.WebElement;

public class Main {

    public static void main(String[] args) {
        // tasks 11 - 16
        WebElement column = new BrowserMiner().mine();
        FeedElementsMine.findRows(column).forEach(FeedElementsMine::mineFeedRowToFiles);
    }
}
