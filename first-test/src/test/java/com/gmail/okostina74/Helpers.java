package com.gmail.okostina74;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

//this class contains different little functions with small functionality.
public class Helpers {

//* this class extract R, G, B values from color attribute
    public static class RGB {
        private String R;
        private String G;
        private String B;

        RGB (String color){

            if (color.substring(0,4).toLowerCase().equals("rgba")) {
                String[] colorParts = color.substring(5, color.length() - 1).split(", ");
                this.R = colorParts[0];
                this.G = colorParts[1];
                this.B = colorParts[2];
            }
            else if (color.substring(0,4).toLowerCase().equals("rgb("))
            {
                String[] colorParts = color.substring(4, color.length() - 1).split(", ");
                this.R = colorParts[0];
                this.G = colorParts[1];
                this.B = colorParts[2];
            }
        }

        public String getR(){
            return this.R;
        }
        public String getG(){
            return this.G;
        }
        public String getB(){
            return this.B;
        }
    }

    //* this method format String value of a font-size attribute to Integer
    public static Double formatFontSize(String fontSize) {
        Double size = Double.parseDouble(fontSize.split("px")[0]);
        return size;
    }

    public static void loginAdmin(DriverBase driver, String title) throws WebDriverException {
        WebDriverWait wait;
        wait = new WebDriverWait(driver.getDriver(), 10);
        driver.getDriver().findElement(By.name("username")).sendKeys("admin");
        driver.getDriver().findElement(By.name("password")).sendKeys("admin");
        click(driver.getDriver().findElement(By.name("login")),driver);
        wait.until(titleIs(title));
    }

    public static void enterText(WebElement webElement, String str) {
    //    webElement.clear();
        webElement.sendKeys(str);
    }
    public static void enterText(WebElement webElement, String str, boolean preFormat) {
        webElement.clear();
        webElement.sendKeys(Keys.HOME + str);
    }
    public static void click(WebElement el, DriverBase driver){
        if (driver.getName().equals(DriverBase.IE))
            el.sendKeys(Keys.RETURN);
        else
            el.click();
    }

    public static void checkElement (WebElement el){
        String atr = el.getAttribute("checked");
        if (atr == null) {
            el.click();
        }
    }
    public static void uncheckCheckBox (WebElement el) {
        String atr = el.getAttribute("checked");
        if (atr != null && atr.equals("true")) {
            el.click();
        }
    }

    public static boolean isElementPresent(WebDriver driver, By locator){
        try{
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            return driver.findElements(locator).size() > 0;
        }
        finally {
            driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
        }
    }

    public static ExpectedCondition<String> thereIsWindowOtherThan(final Set<String> oldHandles) {
        return new ExpectedCondition<String>() {
            @Override
            public String apply(WebDriver driver) {
                Set<String> handles = driver.getWindowHandles();
                handles.removeAll(oldHandles);
                return handles.size() > 0 ? handles.iterator().next() : null;
            }
        };
    }
}


