package com.example.demo.CoinUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class ValidateAddress {

    public static String getValidAddress(String address) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("https://app.zerion.io/connect-wallet");
        Thread.sleep(7000);
        driver.findElement(By.id("track-asset-input")).sendKeys(address);
        Thread.sleep(2000);
        driver.findElement(By.className("InlineForm__CenterContent-sc-1b3rprz-1")).click();
        Thread.sleep(9000);
        String text=null;
        try {
            text = driver.findElement(By.className("Modal__BodyText-sc-1eqklat-4")).getText();
        }catch (Exception e){
             text="T";
        }
        driver.close();
        driver.quit();
        return text;
    }

    public static List<String> getAddressCoin(String address) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("https://app.zerion.io/"+address+"/overview");
        Thread.sleep(15000);

        List<WebElement> elements = driver.findElements(By.xpath("//*[@id='PageWrapper']/div/div/div[4]/div[1]/div[2]/div/div/a/div/div[1]/div[2]/div[1]/span"));
        List<String> res=new ArrayList<>();
        for (WebElement element : elements) {
            String text = element.getText();
            text=text.replaceAll("[^a-zA-Z]","");
            res.add(text);
        }
        driver.close();
        driver.quit();

        return res;
    }
}
