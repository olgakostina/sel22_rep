package com.gmail.okostina74;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class NewProduct {
    WebDriver driverC;
    WebDriver driverF;
    WebDriver driverI;
    private DriverBase driverCh = new DriverBase(driverC, DriverBase.CHROME);
    private DriverBase driverFF = new DriverBase(driverF,DriverBase.FF);
   // private DriverBase driverIE = new DriverBase(driverI,DriverBase.IE);

    public void newProduct(DriverBase driver) throws WebDriverException {
        Helpers.loginAdmin(driver);
        //General tabs
        List<WebElement> webLists = driver.getDriver().findElements((By.cssSelector("li#app-")));
        for (WebElement list : webLists) {
            if (list.findElement(By.cssSelector("span.name")).getText().equals("Catalog")) {
                Helpers.click(list.findElement(By.cssSelector("a")), driver);
                break;
            }
        }
        WebDriverWait wait = new WebDriverWait(driver.getDriver(), 10);
        ;
        wait.until(titleIs("Catalog | My Store"));

        webLists = driver.getDriver().findElements((By.cssSelector("a.button")));
        for (WebElement list : webLists) {
            if (list.getText().equals("Add New Product")) {
                Helpers.click(list, driver);
                break;
            }
        }
        wait.until(titleIs("Add New Product | My Store"));
        webLists = driver.getDriver().findElements(By.cssSelector(".tabs li a"));
        for (int i = 0; i < webLists.size(); i++) {
            String textTab = webLists.get(i).getText();
            if (textTab.equals("General")) {
                //input data to General page:
                Helpers.enterText(driver.getDriver().findElement(By.name("name[en]")), "Queen Duck");
                Helpers.enterText(driver.getDriver().findElement(By.name("code")), "rd0006");
                List<WebElement> checkBoxes = driver.getDriver().findElements(By.cssSelector("label"));
                for (WebElement checkBox : checkBoxes) {
                    String text = checkBox.getText();
                    if (text.equals("Enabled")) {
                        Helpers.checkElement(checkBox.findElement(By.cssSelector("input")));
                        break;
                    }
                }
                checkBoxes = driver.getDriver().findElements(By.cssSelector("[name^=categories]"));
                for (WebElement checkBox : checkBoxes) {
                    String text = checkBox.getAttribute("value");
                    if (text.equals("1")) {
                        Helpers.checkElement(checkBox);
                    } else {
                        Helpers.uncheckCheckBox(checkBox);
                    }
                }
                checkBoxes = driver.getDriver().findElements(By.cssSelector("[name^=product_groups]"));
                for (WebElement checkBox : checkBoxes) {
                    Helpers.checkElement(checkBox);
                }
                Helpers.enterText(driver.getDriver().findElement(By.cssSelector("input[name=quantity]")), "10.00", true);
                Path testFilePath = Paths.get(".\\queen_duck.jpg");
                String realPath = "" + testFilePath.toAbsolutePath().normalize();
                driver.getDriver().findElement(By.cssSelector("input[type=file]")).sendKeys(realPath);
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Calendar instance = Calendar.getInstance();
                String dateFrom = df.format(instance.getTime());
                instance.add(Calendar.DAY_OF_MONTH, 3);
                String dateTo = df.format(instance.getTime());
                Helpers.enterText(
                        driver.getDriver().findElement(By.name("date_valid_from")), dateFrom
                );
                Helpers.enterText(
                        driver.getDriver().findElement(By.name("date_valid_to")), dateTo
                );

            }
            //Information
            else if (textTab.equals("Information")) {
                webLists.get(i).click();
                Select select = new Select(driver.getDriver().findElement(By.name("manufacturer_id")));
                select.selectByValue("1");

                Helpers.enterText(driver.getDriver().findElement(By.cssSelector("[name^=short_description]")), "Queen Duck. It's the one");
                WebElement element = driver.getDriver().findElement(By.className("trumbowyg-editor"));
                element.click();
                Helpers.enterText(element, "It's a Queen of your bad and life");
                webLists = driver.getDriver().findElements(By.cssSelector(".tabs li a"));
            } else if (textTab.equals("Prices")) {
                webLists.get(i).click();
                Helpers.enterText(
                        driver.getDriver().findElement(By.cssSelector("input[name=purchase_price]")), "30", true
                );
                Helpers.enterText(
                        driver.getDriver().findElement((By.cssSelector("[name='prices[USD]']"))), "30", true
                );
                webLists = driver.getDriver().findElements(By.cssSelector(".tabs li a"));
            }

        }
        Helpers.click(driver.getDriver().findElement(By.name("save")), driver);
            wait.until(titleIs("Catalog | My Store"));
        webLists=driver.getDriver().findElements(By.cssSelector(".row td:nth-child(3)"));
        boolean flag =false;
        for (WebElement webList : webLists) {
            String text = webList.findElement(By.cssSelector("a")).getText();
            if (text.equals("Queen Duck")) {
                flag = true;
                break;
            }
        }
        if (!flag) fail("product is not added");
    }


    @Before
    public void start(){
        driverCh.initDriver();
        driverFF.initDriver();
        //    driverIE.initDriver();
    }

    @Test
    public void productTest() {
        try {
            driverCh.getPage("http://localhost/litecart/admin");
            newProduct(driverCh);
            driverFF.getPage("http://localhost/litecart/admin");
            newProduct(driverFF);
            //    driverIE.getPage("http://localhost/litecart/admin");
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
        driverFF.stopDriver();
    }
}
