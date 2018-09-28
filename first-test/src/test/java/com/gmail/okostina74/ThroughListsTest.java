package com.gmail.okostina74;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class ThroughListsTest {
    private WebDriver driverChrome;
    private WebDriverWait wait;

    @Before
    public void start(){
        try {
            driverChrome = new ChromeDriver();
            driverChrome.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            wait = new WebDriverWait(driverChrome, 10);
            driverChrome.get("http://localhost/litecart/admin");
            driverChrome.findElement(By.name("username")).sendKeys("admin");
            driverChrome.findElement(By.name("password")).sendKeys("admin");
            driverChrome.findElement(By.name("login")).click();
            wait.until(titleIs("My Store"));
        }
        catch (WebDriverException e){
            System.out.println("Attention! Exception: " + e);
            driverChrome.quit();
            driverChrome = null;
        }
    }

    @Test
    public void ThroughLists(){
        try {
            int menuCount;
            menuCount = driverChrome.findElements(By.cssSelector("li#app-")).size();
            for (int i = 1; i <= menuCount; i++) {
                driverChrome.findElement(By.cssSelector("li#app-:nth-child(" + i + ")")).click();
                driverChrome.findElement(By.cssSelector("h1"));
                if (driverChrome.findElements(By.cssSelector("ul.docs")) != null) {
                    int submenuCount = driverChrome.findElements(By.cssSelector("ul.docs>li")).size();
                    for (int j = 1; j <= submenuCount; j++) {
                        driverChrome.findElement(By.cssSelector("ul.docs>li:nth-child(" + j + ")")).click();
                        driverChrome.findElement(By.cssSelector("h1"));
                    }
                }
            }
        }
        catch (WebDriverException e){
                System.out.println("Attention! Exception " + e);
                driverChrome.quit();
                driverChrome = null;
        }

    }

    @After
    public void stop(){
        driverChrome.quit();
        driverChrome = null;
    }
}
