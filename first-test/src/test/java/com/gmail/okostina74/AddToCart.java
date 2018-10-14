package com.gmail.okostina74;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static junit.framework.TestCase.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;

import static junit.framework.TestCase.fail;

public class AddToCart {
    WebDriver driverC;
    WebDriver driverF;
    WebDriver driverI;
    private DriverBase driverCh = new DriverBase(driverC, DriverBase.CHROME);
    private DriverBase driverFF = new DriverBase(driverF,DriverBase.FF);
    private DriverBase driverIE = new DriverBase(driverI,DriverBase.IE);

    public void addToCart(DriverBase driver) throws TimeoutException{
        //Add three products to a cart
        WebElement webElement;
        WebDriverWait wait;
        for (int i = 0; i<3; i++) {
            webElement = driver.getDriver().findElement(By.cssSelector(".product .link"));
            Helpers.click(webElement, driver);
            if (Helpers.isElementPresent(driver.getDriver(), By.cssSelector("td.options"))){
                Select select = new Select(driver.getDriver().findElement(By.cssSelector("td.options select")));
                select.selectByValue("Small");
            }
            webElement = driver.getDriver().findElement(By.cssSelector("button[name=add_cart_product]"));
            Helpers.click(webElement, driver);
            for (int j = 0; j < 11; j++) {
                if (j>10) throw new TimeoutException();
                else{
                    try {
                        Integer itemCount = Integer.parseInt(
                                driver.getDriver().findElement(By.cssSelector("span.quantity")).getText());
                        if (itemCount > i ) {
                            break;
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) { }
                }
            }
            driver.getDriver().navigate().back();
        }
        //Open the cart from main page
    //    JavascriptExecutor jse = (JavascriptExecutor)driver.getDriver();
    //    jse.executeScript("window.scrollTop", "");
        webElement =driver.getDriver().findElement(By.cssSelector("div#cart .link"));
        Helpers.click(webElement, driver);
        //Remove all product
        List<WebElement> products = driver.getDriver().findElements(By.cssSelector(".shortcut a"));
        while (true) {
            if (Helpers.isElementPresent(driver.getDriver(), By.cssSelector(".shortcut a")))
                Helpers.click(products.get(0),driver);
            //Remember tab row which must be hide
            WebElement tableRow = driver.getDriver().findElement(By.cssSelector("td.item"));
            webElement = driver.getDriver().findElement(By.cssSelector("button[name=remove_cart_item]"));
            Helpers.click(webElement,driver);
            wait = new WebDriverWait(driver.getDriver(), 10);
            wait.until(stalenessOf(tableRow));
            if (Helpers.isElementPresent(driver.getDriver(), By.cssSelector(".shortcut a")))
                products = driver.getDriver().findElements(By.cssSelector(".shortcut a"));
            if (!Helpers.isElementPresent(driver.getDriver(), By.cssSelector("form"))) {
                driver.getDriver().navigate().back();
                break;
            }
        }
        String text = driver.getDriver().findElement(By.cssSelector("span.quantity")).getText();
        if (!text.equals("0"))
            fail("Not all products are deleted from the Cart in Browser: " + driver.getName());
    }


    @Test
    public void productTest() {
        try {
            driverCh.initDriver();
            driverCh.getPage("http://localhost/litecart/");
            addToCart(driverCh);

            driverFF.initDriver();
            driverFF.getPage("http://localhost/litecart/");
            addToCart(driverFF);

            driverIE.initDriver();
            driverIE.getPage("http://localhost/litecart/");
            addToCart(driverIE);
        }
        catch (WebDriverException ex) {
            fail("Warning, exception: " + ex);
        }
        catch (TimeoutException ex){
            fail("ex");
        }
    }

    @After
    public void stop(){
        driverIE.stopDriver();
        driverCh.stopDriver();
        driverFF.stopDriver();
    }
}
