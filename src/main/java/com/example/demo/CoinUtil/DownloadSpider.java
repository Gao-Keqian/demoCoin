package com.example.demo.CoinUtil;

import com.example.demo.Service.IAddressCountService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DownloadSpider {

    @Autowired
    IAddressCountService addressCountService;

    public static boolean download(String address) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("https://app.zerion.io/" + address + "/history");
        Thread.sleep(20000);
        boolean res=true;
        try {
            driver.findElement(By.xpath("//*[@id='PageWrapper']/div/div[5]/div[1]/div[2]/div/button[1]")).click();
        }catch (Exception e){
            res=false;
        }

        Thread.sleep(20000);

        driver.close();
        driver.quit();
        return res;
    }
}
