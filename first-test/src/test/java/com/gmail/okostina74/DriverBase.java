package com.gmail.okostina74;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.fail;

public class DriverBase {
    private WebDriver driver;
    private String name;
    final static String CHROME = "Chrome";
    final static String FF = "Firefox";
    final static String IE = "IE";

    DriverBase(WebDriver driver, String name){
        this.driver = driver;
        this.name = name;
    }
    public void setDriver(WebDriver driver) {
        this.driver = driver;

    }
    public WebDriver getDriver(){
        return this.driver;
    }

    public String getName() {
        return this.name;
    }

    public void getPage(String page){
        this.driver.get(page);
    }

    public void stopDriver(){
        this.driver.quit();
        this.driver = null;
    }

    public void initDriver(){
        if(this.getName().equals(CHROME)){
            this.driver = new ChromeDriver();
        }
        else if(this.getName().equals(FF)){
            this.driver = new FirefoxDriver();
        }
        else if(this.getName().equals(IE)){
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);
            this.driver = new InternetExplorerDriver(caps);
        }
        else
            fail("Browser is not existed");
        this.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS );
    }
}
