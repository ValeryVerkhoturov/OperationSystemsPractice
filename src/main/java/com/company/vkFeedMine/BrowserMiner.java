package com.company.vkFeedMine;

import com.company.Properities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.Objects;

public class BrowserMiner {

    private final WebDriver webDriver;

    public BrowserMiner(){
        // Set up browser
        System.setProperty("webdriver.chrome.driver", Properities.getProperty("chromedriver"));
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(Properities.getProperty("chromeoption"));
        chromeOptions.setLogLevel(ChromeDriverLogLevel.SEVERE);
        webDriver = new ChromeDriver(chromeOptions);
    }

    /**
     *
     * @return feed column WebElement
     */
    public WebElement mine(){
        openPage();
        WebElement column = mineColumn();
        return Objects.requireNonNull(column);
    }

    private void openPage(){
        webDriver.get(Properities.getProperty("vkfeedpage"));
    }

    public void closeBrowser(){
        webDriver.close();
    }

    private WebElement mineColumn(){
        List<WebElement> webElements = webDriver.findElements(By.id("feed_rows"));
        for (WebElement item : webElements)
        {
            if (!item.isDisplayed())
                continue;
            return item;
        }
        return null;
    }

}
