package com.example.demo.CoinUtil;

import com.example.demo.CsvUtil.DateUtil;
import com.example.demo.Dao.Coin;
import com.example.demo.Dao.CoinName;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CoinNameSpider implements PageProcessor {
    //    https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?limit=200&&CMC_PRO_API_KEY=c4f7e968-cfd1-44db-9ff6-d02a13050e00
    private Site site = Site
            .me()
            .setDomain("https://cryptorank.io/")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");

    private static List<CoinName> res = new ArrayList<>();

    @Override
    public void process(Page page) {
        String rawText = page.getRawText();
//        JsonPathSelector nameSelector = new JsonPathSelector("$.data[*].name");
        JsonPathSelector symbolSelector = new JsonPathSelector("$.data[*].slug");
//        JsonPathSelector rankSelector = new JsonPathSelector("$.data[*].rank");
//        List<String> nameList = nameSelector.selectList(rawText);
        List<String> symbolList = symbolSelector.selectList(rawText);
//        List<String> rankList = rankSelector.selectList(rawText);
        for (int i = 0; i < symbolList.size(); i++) {
            CoinName coin = new CoinName();
            coin.setName(symbolList.get(i));
//            coin.setShortName(symbolList.get(i));
//            coin.setCmcRank(rankList.get(i));
//            coin.setSaveDate(DateUtil.DateToString(new Date()));
            res.add(coin);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void importSinglePost(String url) {
        Spider.create(new CoinNameSpider())
                .addUrl(url)
                .addPipeline(new ConsolePipeline())
                .run();
    }


    public static List<CoinName> getCoin() throws InterruptedException {
//
        CoinNameSpider.importSinglePost("https://api.cryptorank.io/v1/currencies?api_key=7fdbf815ac3f5de9d3d02c5584c53b4f71cbd03e20c7195025e7bbe8f29c&&limit=2500");

        return res;
    }
}
