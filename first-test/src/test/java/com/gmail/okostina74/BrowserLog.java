package com.gmail.okostina74;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.List;

import static junit.framework.TestCase.*;

public class BrowserLog {
    WebDriver driverC;
    WebDriver driverF;
    WebDriver driverI;
    private DriverBase driverCh = new DriverBase(driverC, DriverBase.CHROME);
    private DriverBase driverFF = new DriverBase(driverF,DriverBase.FF);
    private DriverBase driverIE = new DriverBase(driverI,DriverBase.IE);

    public void browserLog(DriverBase driver) throws WebDriverException{
        Helpers.loginAdmin(driver, "Catalog | My Store");
        List<WebElement> products = driver.getDriver().findElements(By.cssSelector(".row td:nth-child(3)"));
        for (int i = 1; i < products.size(); i++){
            if (products.get(i).findElements(By.cssSelector("img")).size()>0) {
                Helpers.click(products.get(i).findElement(By.cssSelector("a")), driver);
                driver.getDriver().navigate().back();
                products = driver.getDriver().findElements(By.cssSelector(".row td:nth-child(3)"));
            }
        }
        assertEquals(0, driver.getDriver().manage().logs().get("browser").getAll().size());
    }

    @Before
    public void start(){
        driverCh.initDriver();
        //driverFF.initDriver();
        //    driverIE.initDriver();
    }

    @Test
    public void logTest() {
        try {
            driverCh.getPage("http://localhost/litecart/admin/?app=catalog&doc=catalog&category_id=1");
            browserLog(driverCh);
            //driverFF.getPage("http://localhost/litecart/admin/?app=catalog&doc=catalog&category_id=1");
            //browserLog(driverFF);
            //    driverIE.getPage("http://localhost/litecart/admin/?app=catalog&doc=catalog&category_id=1");
            //    newProduct(driverIE);
        }
        catch (WebDriverException ex) {
            fail("Warning, exception: " + ex);
        }
    }

    @After
    public void stop(){
        //driverIE.stopDriver();
        driverCh.stopDriver();
        //driverFF.stopDriver();
    }

}
