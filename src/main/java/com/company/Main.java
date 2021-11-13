package com.company;

import com.company.vkFeedMine.BrowserMiner;
import com.company.vkFeedMine.FeedElementsMine;
import org.openqa.selenium.WebElement;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        WebElement column = (new BrowserMiner()).mine();
        List<WebElement> rows = FeedElementsMine.findRows(column);
        System.out.println("Строки  найдены" + rows.size());
        rows.stream().forEach(FeedElementsMine::mineFeedRow);
    }
}
