package com.gmail.okostina74;

import net.bytebuddy.utility.RandomString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static junit.framework.TestCase.fail;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class NewCustomer {
    WebDriver driverC;
    WebDriver driverF;
    WebDriver driverI;
    private DriverBase driverCh = new DriverBase(driverC, DriverBase.CHROME);
    private DriverBase driverFF = new DriverBase(driverF,DriverBase.FF);
    private DriverBase driverIE = new DriverBase(driverI,DriverBase.IE);

    public void logoutCustomer(DriverBase driver) throws WebDriverException{
        List<WebElement> lists = driver.getDriver().findElements(By.cssSelector(".content .list-vertical li:not(.category-1)"));
        for (WebElement list : lists){
            String text = list.getText();
            if (text.equals("Logout")) {
                Helpers.click(list.findElement(By.cssSelector("a")), driver);
                break;
            }
        }
    }
    public void newCustomer(DriverBase driver) throws WebDriverException{

        Helpers.click(driver.getDriver().findElement(By.cssSelector("[name=login_form] a")), driver);
        WebDriverWait wait = new WebDriverWait(driver.getDriver(), 10);
        wait.until(titleIs("Create Account | My Store"));

        Helpers.enterText(driver.getDriver().findElement(By.name("firstname")), "Olga");
        Helpers.enterText(driver.getDriver().findElement(By.name("lastname")), "Kostina");
        Helpers.enterText(driver.getDriver().findElement(By.name("address1")), "Harding Avenu");
        Helpers.enterText(driver.getDriver().findElement(By.name("city")), "Miami");
        Helpers.enterText(driver.getDriver().findElement(By.name("phone")), "+12345678901");
        Helpers.enterText(driver.getDriver().findElement(By.name("password")), "123456q");
        Helpers.enterText(driver.getDriver().findElement(By.name("confirmed_password")), "123456q");
        String email = RandomString.make(8) + "@mail.ru";
        Helpers.enterText(driver.getDriver().findElement(By.name("email")), email);

        if (driver.getName().equals(DriverBase.IE)) {
            Select select = new Select(driver.getDriver().findElement(By.name("country_code")));
            select.selectByIndex(224);
        }
        else {
            Helpers.click(driver.getDriver().findElement(By.cssSelector("span.select2-selection__arrow")), driver);
            driver.getDriver().findElement(By.cssSelector("input.select2-search__field")).
                    sendKeys("United States" + Keys.ENTER);
        }
        Select select = new Select(driver.getDriver().findElement(By.cssSelector("select[name=zone_code]")));
        select.selectByValue("FL");
        Helpers.enterText(driver.getDriver().findElement(By.name("postcode")), "12345");

        Helpers.click(driver.getDriver().findElement(By.name("create_account")), driver);
        wait.until(titleIs("Online Store | My Store"));
        logoutCustomer(driver);
        wait.until(titleIs("Online Store | My Store"));
        Helpers.enterText(driver.getDriver().findElement(By.name("email")), email);
        Helpers.enterText(driver.getDriver().findElement(By.name("password")), "123456q");
        Helpers.click(driver.getDriver().findElement(By.name("login")), driver);
        wait.until(titleIs("Online Store | My Store"));
        logoutCustomer(driver);
        wait.until(titleIs("Online Store | My Store"));
    }


    @Before
    public void start(){
        driverCh.initDriver();
        driverFF.initDriver();
        driverIE.initDriver();
    }

    @Test
    public void productTest() {
        try {
            driverCh.getPage("http://localhost/litecart/");
            newCustomer(driverCh);
            driverFF.getPage("http://localhost/litecart/");
            newCustomer(driverFF);
            driverIE.getPage("http://localhost/litecart/");
            newCustomer(driverIE);
        }
        catch (WebDriverException ex) {
            fail("Warning, exception: " + ex);
        }
    }

    @After
    public void stop(){
        driverIE.stopDriver();
        driverCh.stopDriver();
        driverFF.stopDriver();
    }
}
