package com.company.vkFeedMine;

import com.company.jsonWork.ConsoleWriter;
import com.company.jsonWork.FileType;
import com.company.jsonWork.FileWriteController;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@UtilityClass
public class FeedElementsMine {

    public List<WebElement> findRows(WebElement column){
        return column.findElements(By.tagName("div"))
                .stream()
                .filter(WebElement::isDisplayed)
                .filter(e -> Objects.nonNull(e.getAttribute("class")))
                .filter(e -> e.getAttribute("class").toLowerCase().trim().equals("feed_row"))
                .collect(Collectors.toList());
    }

    public void mineFeedRowToFiles(WebElement row){
        row = row.findElement(By.tagName("div"));

        String id = row.getAttribute("id");
        row = row.findElement(By.tagName("div"));
        String text;
        List<String> urls;
        try {
            WebElement wallPost = row.findElement(By.className("wall_post_text"));
            text = row.findElement(By.className("wall_post_text")).getText();
            urls = wallPost.findElements(By.tagName("a")).stream().map(e -> e.getAttribute("href")).collect(Collectors.toList());
        } catch (org.openqa.selenium.NoSuchElementException e){
            text = "";
            urls = new ArrayList<>();
        }
        List<String> picturesUrl;
        try {
            // WebElement wallPics = row.findElement(By.className("page_post_sized_thumbs  clear_fix"));
            picturesUrl = row.findElements(By.tagName("a"))
                    .stream()
                    .filter(e -> Objects.nonNull(e.getAttribute("aria-label")))
                    .filter(e -> e.getAttribute("aria-label").equals("фотография"))
                    .map(e -> e.getAttribute("style"))
                    .map(s -> s.substring(s.indexOf("url(")+5, s.indexOf("\")") - 1))
                    .collect(Collectors.toList());
        }
        catch (InvalidSelectorException e){
            picturesUrl = new ArrayList<>();
        }
        saveToFiles(id, text, urls, picturesUrl);
    }

    private void saveToFiles(String id, String text, List<String> urls, List<String> picturesUrl){
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.execute(new FileWriteController(new FileType.First(id, text), FileType.FIRST));
        executorService.execute(new FileWriteController(new FileType.Second(id, urls), FileType.SECOND));
        executorService.execute(new FileWriteController(new FileType.Third(id, picturesUrl), FileType.THIRD));
        executorService.shutdown();
        new Thread(new ConsoleWriter(executorService)).start();
    }

}
