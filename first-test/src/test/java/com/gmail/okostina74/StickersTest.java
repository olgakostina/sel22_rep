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

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

public class StickersTest {
    private WebDriver driverChrome;
    private WebDriverWait wait;

    @Before
    public void start(){
        driverChrome = new ChromeDriver();
        driverChrome.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    public void stickers() {
        try {
            String[] boxes = {"box-most-popular", "box-campaigns", "box-latest-products"};
            int count = 0;
            driverChrome.get("http://localhost/litecart/");
            for (String box : boxes){
                String cssSel = "#" + box + " li.product.column.shadow.hover-light";
                int productCount = driverChrome.findElements(By.cssSelector(cssSel)).size();
                for (int i = 1; i<=productCount; i++){
                    count++;
                    List<WebElement> stickers = driverChrome.findElements(By.cssSelector(cssSel + ":nth-child(" + i + ") .sticker"));
                    if (stickers == null  |
                            stickers.size()>1) {
                        fail("Stickers less or more than necessary");
                    }
                }
            }
        }
        catch (WebDriverException ex){
            driverChrome.quit();
            driverChrome = null;
            System.out.println("Exception: " + ex);
        }
    }

    @After
    public void stop(){
        driverChrome.quit();
        driverChrome = null;
    }
}
