package com.company.vkFeedMine;

import com.company.jsonWork.FileType;
import com.company.jsonWork.FileWriteController;
import com.company.jsonWork.WriteIterations;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class FeedElementsMine {

    private static AtomicInteger iteration = new AtomicInteger(-1);

    public List<WebElement> findRows(WebElement column){
        List<WebElement> rows = new ArrayList<>();
        for (WebElement element: column.findElements(By.tagName("div")))
            try {
                if (element.isDisplayed()
                        && Objects.nonNull(element.getAttribute("class"))
                        && element.getAttribute("class").toLowerCase().trim().equals("feed_row"))
                    rows.add(element);
            } catch (org.openqa.selenium.StaleElementReferenceException ignore){}
        return rows;
    }

    public void mineFeedRowToFiles(WebElement row){
        saveToFiles(findId(row), findText(row), findPictureUrl(row), findUrls(row));
    }

    private void saveToFiles(String id, String text, List<String> pictureUrls, List<String> urls){
        HashMap<FileType, FileWriteController> fileWriters = new HashMap<>();
        fileWriters.put(FileType.FIRST, new FileWriteController(new FileType.First(id, text), FileType.FIRST));
        fileWriters.put(FileType.SECOND, new FileWriteController(new FileType.Second(id, pictureUrls), FileType.SECOND));
        fileWriters.put(FileType.THIRD, new FileWriteController(new FileType.Third(id, urls), FileType.THIRD));
        WriteIterations.addIteration(fileWriters);
    }

    private String findId(WebElement row){
        return row.findElement(By.tagName("div")).getAttribute("id");
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
        List<String> picturesUrl = null;
        try {
            picturesUrl = row
                    .findElement(By.tagName("div"))
                    .findElement(By.tagName("div"))
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
