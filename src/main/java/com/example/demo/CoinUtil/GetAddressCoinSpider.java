package com.example.demo.CoinUtil;

import com.example.demo.Service.IAddressCountService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GetAddressCoinSpider {

    @Autowired
    IAddressCountService addressCountService;

    public static void download(String address, Map<String, List<String>> map) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("https://app.zerion.io/" + address + "/overview");
        Thread.sleep(20000);

        List<WebElement> elements = driver.findElements(By.xpath("//*[@id='PageWrapper']/div/div/div[4]/div[1]/div[2]/div/div/a/div/div[1]/div[2]/div[2]/span/span[2]"));
        for (WebElement element : elements) {
            String coin = element.getText();
            if(map.containsKey(coin)){
                map.get(coin).add(address);
            }else {
                List<String> list= new ArrayList<>();
                list.add(address);
                map.put(coin, list);
            }
        }
        driver.close();
        driver.quit();
    }
}
