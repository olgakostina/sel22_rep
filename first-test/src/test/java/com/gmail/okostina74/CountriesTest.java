package com.gmail.okostina74;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.*;
import static junit.framework.TestCase.fail;


public class CountriesTest {
    private WebDriver chromeDriver;
    private WebDriver firefoxDriver;

    //this method check sorted or not rows in column
    public void isSort(List<WebElement> webElements, String table, String driverName){
        List <String> columnNameValues = new ArrayList<String>();
        int i = webElements.size();
        if (table.equals("Zones")) i--;
        for (int j=0; j<i; j++){
            String str = webElements.get(j).getAttribute("textContent");
            columnNameValues.add(str);
        }
        List <String> columnNameValuesSort = new ArrayList(columnNameValues);
        Collections.sort(columnNameValuesSort);
        if (!columnNameValuesSort.equals(columnNameValues)){
            fail("Table: " + table + " has incorrect sort order in Name column. Driver: " + driverName);
        }
    }

    //this method finds number of column with specific name
    public int columnNumber(List<WebElement> webElements, String column){
        int number = 0;
        for (WebElement columnHeader : webElements){
            String str = columnHeader.getAttribute("textContent");
            if (str.equals(column)) {
                number = webElements.indexOf(columnHeader) + 1;
                break;
            }
        }
    return number;
    }

    //this method checks order of Name column in Country table
    public void countriesTable(WebDriver driver, String driverName){
        int name=0, zone=0;
        driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        if(!driver.findElement(By.cssSelector("[name=countries_form]")).isDisplayed())
            fail("No Countries page. Driver: " + driverName);
        //find number of Name and Zone columns
        List<WebElement> tableHeader = driver.findElements(By.cssSelector("th"));
        for (WebElement columnHeader : tableHeader){
            String str = columnHeader.getAttribute("textContent");
            if (str.equals("Name")) {
                name = tableHeader.indexOf(columnHeader) + 1;
            }
            if (str.equals("Zones")) {
                zone = tableHeader.indexOf(columnHeader) + 1;
            }
        }

        //check that Table "Countries" has correct alphabet order in column "Name"
        List <WebElement> columnName = driver.findElements(By.cssSelector(".dataTable td:nth-child(" + name +")"));
        isSort(columnName, "Countries", driverName);

        List <WebElement> columnZones = driver.findElements(By.cssSelector(".dataTable td:nth-child(" + zone +")"));
        List <Integer> columnZonesValuesMoreThanZero = new ArrayList<Integer>();
        for (int i = 0; i<columnZones.size(); i++){
            Integer zones = Integer.parseInt(columnZones.get(i).getAttribute("textContent"));
            if (zones>0) {
                columnZonesValuesMoreThanZero.add(i);
            }
        }
        name+=2;
        columnName = driver.findElements(By.cssSelector(".dataTable td:nth-child(" + name +")"));

        for (Integer zones : columnZonesValuesMoreThanZero){
           columnName.get(zones).click();
           if (!driver.findElement(By.cssSelector("h1")).getText().equals("Edit Country"))
               fail("Not a page 'Edit Country' Driver: " + driverName);
           int nameZones = columnNumber(driver.findElements(By.cssSelector("#table-zones th")), "Name");
           columnName = driver.findElements(By.cssSelector("#table-zones td:nth-child(" + nameZones +")"));
           isSort(columnName, "Zones", driverName);
           driver.navigate().back();
           columnName = driver.findElements(By.cssSelector(".dataTable td:nth-child(" + name +")"));
      }
    }

    //method for test2: check order of geo-zones for each country on http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones.

    public void geozonesTable(WebDriver driver, String driverName){
        driver.get(" http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones");
        if (!driver.findElement(By.cssSelector("h1")).getText().equals("Geo Zones"))
            fail("Not a Geo Zones page. Driver: " + driverName);
        int nameZones = columnNumber(driver.findElements(By.cssSelector("th")), "Name") + 1;
        List<WebElement> geoZones = driver.findElements(By.cssSelector("tr .row td"));
        for (int i = nameZones; i<geoZones.size(); i+=5) {
            geoZones.get(i).click();
            if (!driver.findElement(By.cssSelector("h1")).getText().equals("Edit Geo Zone"))
                fail("Not an Edit Geo Zone page Driver: " + driverName);
            List<WebElement> zones = driver.findElements(By.cssSelector("select:not(.select2-hidden-accessible) [selected=selected]"));
            isSort(zones, "Geo-zones", driverName);
            driver.navigate().back();
            geoZones = driver.findElements(By.cssSelector("tr .row td"));
        }
    }

    @Before
    public void start(){
        chromeDriver = new ChromeDriver();
        chromeDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        firefoxDriver = new FirefoxDriver();
        firefoxDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    public void countriesTest(){
        countriesTable(chromeDriver,"Chrome");
        geozonesTable(chromeDriver, "Chrome");
        countriesTable(firefoxDriver,"Firefox");
        geozonesTable(firefoxDriver, "FireFox");
    }

    @After
    public void stop(){
        chromeDriver.quit();
        chromeDriver=null;
        firefoxDriver.quit();
        firefoxDriver=null;
    }

}
