package com.gmail.okostina74;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

import java.util.concurrent.TimeUnit;


public class LoginAdminTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start(){
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS );
        wait = new WebDriverWait(driver, 10);

    }

    @Test
    public void LoginAdminTest() {
        try {
            driver.get("http://localhost/litecart/admin/");
            driver.findElement(By.name("username")).sendKeys("admin");
            driver.findElement(By.name("password")).sendKeys("admin");
            driver.findElement(By.name("login")).click();
            wait.until(titleIs("My Store"));
        }
        catch (Exception e){
            driver.quit();
            driver = null;
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
