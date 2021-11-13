package com.company.vkFeedMine;

import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class FeedElementsMine {

    public List<WebElement> findRows(WebElement column){
        List<WebElement> feedRows = new ArrayList<>();
        for (WebElement item : column.findElements(By.tagName("div")))
        {
            try{
                if (!item.isDisplayed())
                    continue;
            }
            catch (StaleElementReferenceException e){
                continue;
            }
            if (Objects.isNull(item.getAttribute("class")))
                continue;
            if (!item.getAttribute("class").toLowerCase().trim().equals("feed_row"))
                continue;
            feedRows.add(item);
        }
        return feedRows;
    }

    public void mineFeedRow(WebElement row){
        row = row.findElement(By.tagName("div"));

        String id = row.getAttribute("id");
        System.out.println("id" + id);

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

        List<String> pics;
        try {
            //WebElement wallPics = row.findElement(By.className("page_post_sized_thumbs  clear_fix"));
            pics = row.findElements(By.tagName("a"))
                    .stream()
                    .filter(e -> Objects.nonNull(e.getAttribute("aria-label")))
                    .filter(e -> e.getAttribute("aria-label").equals("фотография"))
                    .map(e -> e.getAttribute("style"))
                    .map(s -> s.substring(s.indexOf("url(")+5, s.indexOf("\")") - 1))
                    .collect(Collectors.toList());
        }
        catch (InvalidSelectorException e){
            pics = new ArrayList<>();
        }
        System.out.println("text" + text);
        System.out.println("urls: ");
        urls.stream().forEach(System.out::println);
        System.out.println("pics: ");
        pics.stream().forEach(System.out::println);

    }

}
