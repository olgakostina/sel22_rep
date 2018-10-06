package com.gmail.okostina74;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static junit.framework.TestCase.fail;

public class ProductTest {
    final static String PRICE_REGULAR = "R";
    final static String PRICE_CAMPAIGNS = "C";
    final static String PRICE = "P";
    WebDriver driverC;
    WebDriver driverF;
    WebDriver driverI;
    private DriverBase driverCh = new DriverBase(driverC, DriverBase.CHROME);
    private DriverBase driverFF = new DriverBase(driverF,DriverBase.FF);
    private DriverBase driverIE = new DriverBase(driverI,DriverBase.IE);

    //* this method check that price has correct font style and color.
    public void checkPrices(WebElement price, String priceType, String productName, DriverBase driver) throws WebDriverException{
        RGB color = new RGB(price.getCssValue("color"));
        String fontPrice = price.getTagName();
        if (priceType.equals(PRICE)){
            if (!color.getR().equals(color.getG()) | !color.getR().equals(color.getB()))
                fail("Price of " + productName + " has incorrect color in browser " + driver.getName());
            if (!fontPrice.toLowerCase().equals("span"))
                fail("Price of " + productName + " has incorrect font in browser " + driver.getName());
        }
        if (priceType.equals(PRICE_REGULAR)){
            if (!color.getR().equals(color.getG()) | !color.getR().equals(color.getB()))
                fail("Regular price of " + productName + " has incorrect color in browser " + driver.getName());
            if (!fontPrice.toLowerCase().equals("s"))
                fail("Regular price of " + productName + " has incorrect font in browser " + driver.getName());
        }
        if (priceType.equals(PRICE_CAMPAIGNS)){
            if (!color.getG().equals("0") | !color.getB().equals("0"))
                fail("Campaign price of " + productName + " has incorrect color in driver " + driver.getName());
            if (!fontPrice.toLowerCase().equals("strong"))
                fail("Campaign price of " + productName + " has incorrect font in driver " + driver.getName());
        }
    }

    //* this method format String value of a font-size attribute to Integer
    public Double formatFontSize(String fontSize) {
        Double size = Double.parseDouble(fontSize.split("px")[0]);
        return size;
    }

    //* this is general test, which is run for each browser. It checks:
    //* 1. Prices must be equal on main and product page
    //* 2. Names must be equal on main and product page
    //* 3. Price must be black and have class span on main and product pages
    //* 4. Regular price must be gray and have class s on main and product pages
    //* 5. Campaign price must be gray and have class strong on main and product pages
    public void checkProduct(DriverBase driver) throws WebDriverException {
        String[] boxes = {"box-campaigns", "box-latest-products"};
        for (String box : boxes) {
            List<WebElement> products = driver.getDriver().findElements(By.cssSelector("#" + box + " li.product"));
            for (int i = 0; i<products.size(); i++){
                String regPriceMain;
                String camPriceMain = null;
                String nameMain = products.get(i).findElement(By.cssSelector(".name")).getText();
                String sticker = products.get(i).findElement(By.cssSelector("div.sticker")).getText();
                if (sticker.toUpperCase().equals("NEW")) {
                    WebElement price = products.get(i).findElement(By.cssSelector(".price"));
                    regPriceMain = price.getText();
                    checkPrices(price,PRICE, nameMain, driver);
                }
                else {
                    WebElement price = products.get(i).findElement(By.cssSelector(".regular-price"));
                    checkPrices(price,PRICE_REGULAR, nameMain, driver);
                    regPriceMain = price.getText();
                    String regPriceMainSize = price.getCssValue("font-size");
                    price = products.get(i).findElement(By.cssSelector(".campaign-price"));
                    camPriceMain = price.getText();
                    checkPrices(price, PRICE_CAMPAIGNS, nameMain, driver);
                    String camPriceMainSize = price.getCssValue("font-size");
                    if (formatFontSize(regPriceMainSize) >= formatFontSize(camPriceMainSize))
                        fail ("Regular price more or equal campaign price on main page for product: " + nameMain + ", browser: " + driver.getName());
                }
                if (driver.getName().equals(DriverBase.IE)) products.get(i).findElement(By.cssSelector(".link")).sendKeys(Keys.RETURN);
                else products.get(i).click();
                String nameProduct = driver.getDriver().findElement(By.cssSelector("h1.title")).getText();
                if (!nameMain.equals(nameProduct))
                    fail("Incorrect product page for " + nameProduct +  " in browser " + driver.getName());
                else {
                    String regPriceProduct;
                    String camPriceProduct = null;
                    sticker = driver.getDriver().findElement(By.cssSelector(".main-image .sticker")).getText();
                    if (sticker.toUpperCase().equals("SALE")) {
                        WebElement price = driver.getDriver().findElement(By.cssSelector(".information .regular-price"));
                        regPriceProduct = price.getText();
                        checkPrices(price,PRICE_REGULAR, nameProduct, driver);
                        String regPriceSize = price.getCssValue("font-size");
                        price = driver.getDriver().findElement(By.cssSelector(".information .campaign-price"));
                        camPriceProduct = price.getText();
                        checkPrices(price, PRICE_CAMPAIGNS, nameProduct, driver);
                        String camPriceSize = price.getCssValue("font-size");
                        if (formatFontSize(regPriceSize) >= formatFontSize(camPriceSize))
                            fail ("Regular price more or equal campaign price on product page for product: " + nameMain + ", browser: " + driver.getName());
                    }
                    else {

                        WebElement price = driver.getDriver().findElement(By.cssSelector(".information .price"));
                        regPriceProduct = price.getText();
                        checkPrices(price,PRICE, nameProduct, driver);
                    }

                    if ((camPriceMain != null && camPriceProduct == null) || (camPriceMain == null && camPriceProduct != null))
                        fail("Product " + nameProduct + " has Sale price only on one page. Driver " + driver.getName());
                    if(regPriceProduct == null) fail("There is no price");
                    if (!regPriceMain.equals(regPriceProduct))
                        fail("Product " + nameProduct + " has incorrect regular price. Driver " + driver.getName());
                    if (camPriceMain != null && camPriceProduct != null && !camPriceMain.equals(camPriceProduct))
                        fail("Product " + nameProduct + " has incorrect campaign price. Driver " + driver.getName());
                    driver.getDriver().navigate().back();
                    products = driver.getDriver().findElements(By.cssSelector("#" + box + " li.product"));
                }
            }
        }

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
            checkProduct(driverCh);
            driverFF.getPage("http://localhost/litecart/");
            checkProduct(driverFF);
            driverIE.getPage("http://localhost/litecart/");
            checkProduct(driverIE);
        }
        catch (WebDriverException ex) {
            System.out.println("Warning, exception: " + ex);
        }
    }

    @After
    public void stop(){
        driverIE.stopDriver();
        driverCh.stopDriver();
        driverFF.stopDriver();
    }
}

