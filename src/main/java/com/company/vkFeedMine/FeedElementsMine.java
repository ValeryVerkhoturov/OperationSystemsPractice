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

@UtilityClass
public class FeedElementsMine {

    public List<WebElement> findRows(WebElement column){
        return column
                .findElements(By.tagName("div"))
                .stream()
                .filter(e -> Objects.nonNull(e.getAttribute("class")))
                .filter(e -> e.getAttribute("class").toLowerCase().trim().equals("feed_row"))
                .toList();
    }

    public void mineFeedRowToFiles(WebElement row){
        saveToFiles(findId(row), findText(row), findUrls(row), findPictureUrl(row));
    }

    private void saveToFiles(String id, String text, List<String> urls, List<String> picturesUrl){
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.execute(new FileWriteController(new FileType.First(id, text), FileType.FIRST));
        executorService.execute(new FileWriteController(new FileType.Second(id, urls), FileType.SECOND));
        executorService.execute(new FileWriteController(new FileType.Third(id, picturesUrl), FileType.THIRD));
        executorService.shutdown();
        new Thread(new ConsoleWriter(executorService)).start();
    }

    private String findId(WebElement row){
        return row
                .findElement(By.tagName("div"))
                .getAttribute("id");
    }

    private String findText(WebElement row){
        String text = null;
        try {
            text = row
                    .findElement(By.tagName("div"))
                    .findElement(By.tagName("div"))
                    .findElement(By.className("wall_post_text"))
                    .getText().replace("\n", " ");
        } catch (org.openqa.selenium.NoSuchElementException ignore){}
        return text;
    }

    private List<String> findUrls (WebElement row){
        List<String> urls = null;
        try {
            urls = row
                    .findElement(By.tagName("div"))
                    .findElement(By.tagName("div"))
                    .findElement(By.className("wall_post_text"))
                    .findElements(By.tagName("a"))
                    .stream()
                    .map(e -> e.getAttribute("href"))
                    .toList();
        } catch (org.openqa.selenium.NoSuchElementException ignore){}
        return filterEmptyLists(urls);
    }

    private List<String> findPictureUrl(WebElement row){
        row = row.findElement(By.tagName("div")).findElement(By.tagName("div"));
        List<String> picturesUrl = null;
        try {
            picturesUrl = row
                    .findElements(By.tagName("a"))
                    .stream()
                    .filter(e -> Objects.nonNull(e.getAttribute("aria-label")))
                    .filter(e -> e.getAttribute("aria-label").equals("фотография"))
                    .map(e -> e.getAttribute("style"))
                    .map(s -> s.substring(s.indexOf("url(")+5, s.indexOf("\")") - 1))
                    .toList();
        }
        catch (InvalidSelectorException ignore){}
        return filterEmptyLists(picturesUrl);
    }

    private <T> List<T> filterEmptyLists(List<T> list){
        if (Objects.isNull(list))
            return null;
        list = list.stream().filter(Objects::nonNull).toList();
        if (list.size() == 0)
            return null;
        return list;
    }
}
