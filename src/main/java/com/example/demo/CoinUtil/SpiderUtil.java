package com.example.demo.CoinUtil;

import com.example.demo.Dao.Coin;
import com.example.demo.Service.ICoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

public class SpiderUtil implements PageProcessor {



    public static final String URL_LIST = "https://coinmarketcap.com/";

    public static final String URL_POST = "https://coinmarketcap.com/";

    private Site site = Site
            .me()
            .setDomain("https://coinmarketcap.com/")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");

    private static List<String> list=new ArrayList<>();
    private static List<String> shortList=new ArrayList<>();

    @Override
    public void process(Page page) {
        //full name
        List<String> all = page.getHtml().xpath("//tbody/tr/td/div/a/div/div/p/text()").all();
        List<String> all2 = page.getHtml().xpath("//tbody/tr/td/a/span[2]/text()").all();
        list.addAll(all);
        list.addAll(all2);

        //short name
        List<String> sAll = page.getHtml().xpath("//tbody/tr/td[3]/div/a/div/div/div/p/text()").all();
        List<String> sAll2 = page.getHtml().xpath("//table/tbody/tr/td[3]/a/span[3]/text()").all();
        shortList.addAll(sAll);
        shortList.addAll(sAll2);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void importSinglePost(String url) {
        Spider.create(new SpiderUtil())
                .addUrl(url)
                .addPipeline(new ConsolePipeline())
                .run();
    }

    public static List<List<String>>  getCoinName() throws InterruptedException {
        for (int i = 1; i <=50 ; i++) {
            SpiderUtil.importSinglePost("https://coinmarketcap.com/"+"?page="+i);
        }
        List<List<String>> res=new ArrayList<>();
        res.add(list);
        res.add(shortList);
        return res;
    }
}
