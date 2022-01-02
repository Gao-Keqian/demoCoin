package com.example.demo.CoinUtil;

import com.example.demo.Dao.BuyHistory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.*;

public class HistorySpider {


    public static List<BuyHistory> getHistory(String address) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("https://app.zerion.io/" + address + "/history");
        Thread.sleep(20000);

        List<WebElement> elements = driver.findElements(By.xpath("//*[@id='PageWrapper']/div/div[4]/div[2]/div[1]/div[2]/div/div/div[2]/div[1]/div[2]/div[1]/div"));

        List<BuyHistory> res = new ArrayList<>();
        Set<String> set=new HashSet<>();
        for (WebElement webElement : elements) {
            String text = webElement.getText();
            text = text.replace(" ", "");
            String[] s = text.split("\n");
            if(set.contains(s[2])){
                continue;
            }
            BuyHistory buyHistory = new BuyHistory();
            buyHistory.setAddress(address);
            buyHistory.setCoin(s[2]);
            buyHistory.setNum(s[1]);
            buyHistory.setAction("+".equals(s[0]) ? "buy" : "sell");
            buyHistory.setBuyDate(new Date());
            res.add(buyHistory);
            set.add(s[2]);
        }
        driver.close();
        driver.quit();
        return res;
    }


}
