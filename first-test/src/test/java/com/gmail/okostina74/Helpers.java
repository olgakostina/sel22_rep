package com.gmail.okostina74;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    public static void loginAdmin(WebDriver driver) throws WebDriverException {
        WebDriverWait wait;
        wait = new WebDriverWait(driver, 10);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        wait.until(titleIs("My Store"));
    }

    public static void enterText(WebElement webElement, String str) {
    //    webElement.clear();
        webElement.sendKeys(str);
    }
    public static void enterText(WebElement webElement, String str, boolean preFormat) {
    //    webElement.clear();
        webElement.sendKeys(Keys.HOME + str);
    }
    public static void click(WebElement el, DriverBase driver){
        if (driver.getName().equals(DriverBase.IE))
            el.sendKeys(Keys.RETURN);
        else
            el.click();
    }
}
