package com.gmail.okostina74;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import static junit.framework.TestCase.fail;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class LinkToNewWindow {
    WebDriver driverC;
    WebDriver driverF;
    WebDriver driverI;
    private DriverBase driverCh = new DriverBase(driverC, DriverBase.CHROME);
    private DriverBase driverFF = new DriverBase(driverF,DriverBase.FF);
    private DriverBase driverIE = new DriverBase(driverI,DriverBase.IE);

    public void linkToNewWindow(DriverBase driver) throws WebDriverException{
        //log to admin
        Helpers.loginAdmin(driver,"Countries | My Store");

        //go to "Add New Country"
        WebElement webElement = driver.getDriver().findElement(By.cssSelector("#content .button"));
        WebDriverWait wait = new WebDriverWait(driver.getDriver(), 10);
        Helpers.click(webElement, driver);
        wait.until(titleIs("Add New Country | My Store"));

        //open new window via linking on each a from a list
        List<WebElement> links = driver.getDriver().findElements(By.cssSelector("#content td a:not(#address-format-hint)"));
        for (int i = 0; i<links.size(); i++) {
            //remember start windows handles
            String mainWindow = driver.getDriver().getWindowHandle();
            Set<String> oldWindows = driver.getDriver().getWindowHandles();
            // click to link
            Helpers.click(links.get(i), driver);
            String newWindow = wait.until(Helpers.thereIsWindowOtherThan(oldWindows));
            Assert.assertTrue(newWindow != null);
            driver.getDriver().switchTo().window(newWindow);
            driver.getDriver().close();
            driver.getDriver().switchTo().window(mainWindow);
        }
    }
    @Test
    public void productTest() {
        try {
            driverCh.initDriver();
            driverCh.getPage("http://localhost/litecart/admin/?app=countries&doc=countries");
            linkToNewWindow(driverCh);

            driverFF.initDriver();
            driverFF.getPage("http://localhost/litecart/admin/?app=countries&doc=countries");
            linkToNewWindow(driverFF);

            driverIE.initDriver();
            driverIE.getPage("http://localhost/litecart/admin/?app=countries&doc=countries");
            linkToNewWindow(driverIE);
        }
        catch (WebDriverException ex) {
            fail("Warning, exception: " + ex);
        }
     }

    @After
    public void stop(){
        driverIE.stopDriver();
        driverFF.stopDriver();
        driverCh.stopDriver();
    }
}
