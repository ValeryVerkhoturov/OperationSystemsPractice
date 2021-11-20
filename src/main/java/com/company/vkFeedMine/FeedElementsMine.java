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
                .toList();
    }

    public void mineFeedRowToFiles(WebElement row){
        row = row.findElement(By.tagName("div"));

        String id = row.getAttribute("id");
        row = row.findElement(By.tagName("div"));
        String text = null;
        try {
            text = row.findElement(By.className("wall_post_text")).getText().replace("\n", " ");
        } catch (org.openqa.selenium.NoSuchElementException ignore){}

        List<String> urls = null;
        try {
            WebElement wallPost = row.findElement(By.className("wall_post_text"));
            urls = wallPost.findElements(By.tagName("a")).stream().map(e -> e.getAttribute("href")).toList();
        } catch (org.openqa.selenium.NoSuchElementException ignore){}
        if (Objects.nonNull(urls) && urls.size() == 0)
            urls = null;

        List<String> picturesUrl = null;
        try {
            picturesUrl = row.findElements(By.tagName("a"))
                    .stream()
                    .filter(e -> Objects.nonNull(e.getAttribute("aria-label")))
                    .filter(e -> e.getAttribute("aria-label").equals("фотография"))
                    .map(e -> e.getAttribute("style"))
                    .map(s -> s.substring(s.indexOf("url(")+5, s.indexOf("\")") - 1))
                    .toList();
        }
        catch (InvalidSelectorException ignore){}
        if (Objects.nonNull(picturesUrl) && picturesUrl.size() == 0)
            urls = null;

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

    private List<String> findUrls (WebElement row){
        List<String> urls = null;
        try {
            WebElement wallPost = row.findElement(By.className("wall_post_text"));
            urls = wallPost.findElements(By.tagName("a")).stream().map(e -> e.getAttribute("href")).toList();
        } catch (org.openqa.selenium.NoSuchElementException ignore){}
        if (Objects.nonNull(urls) && urls.size() == 0)
            urls = null;
        return urls;
    }
}
