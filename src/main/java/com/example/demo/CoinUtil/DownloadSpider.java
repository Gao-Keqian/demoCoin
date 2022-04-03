package com.example.demo.CoinUtil;

import com.example.demo.Service.IAddressCountService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DownloadSpider {

    @Autowired
    IAddressCountService addressCountService;

    public static boolean download(String address) throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("headless");
//        options.addArguments("disable-gpu");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(20L, TimeUnit.SECONDS);
        driver.get("https://app.zerion.io/" + address + "/history");
        boolean res=true;
        try {
            driver.findElement(By.xpath("//*[@id='PageWrapper']/div/div[3]/div[2]/div[1]/div[2]/div/button[1]")).click();
        }catch (Exception e){
            res=false;
        }
        Thread.sleep(7000);

        driver.close();
        driver.quit();
        return res;
    }
}
