package com.example.demo.CoinUtil;

import com.example.demo.CsvUtil.DateUtil;
import com.example.demo.Dao.Coin;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PriceSpider implements PageProcessor {
//    https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?limit=200&&CMC_PRO_API_KEY=c4f7e968-cfd1-44db-9ff6-d02a13050e00
    private Site site = Site
            .me()
            .setDomain("https://pro-api.coinmarketcap.com/")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");

    private static List<Coin> res=new ArrayList<>();

    @Override
    public void process(Page page) {
        String rawText = page.getRawText();
        JsonPathSelector nameSelector=new JsonPathSelector("$.data[*].name");
        JsonPathSelector symbolSelector=new JsonPathSelector("$.data[*].symbol");
        JsonPathSelector priceSelector=new JsonPathSelector("$.data[*].quote.USD.price");
        JsonPathSelector rankSelector=new JsonPathSelector("$.data[*].cmc_rank");
        List<String> nameList = nameSelector.selectList(rawText);
        List<String> symbolList = symbolSelector.selectList(rawText);
        List<String> priceList = priceSelector.selectList(rawText);
        List<String> rankList = rankSelector.selectList(rawText);
        for (int i = 0; i < nameList.size(); i++) {
            Coin coin=new Coin();
            coin.setName(nameList.get(i));
            coin.setShortName(symbolList.get(i));
            coin.setPrice(priceList.get(i));
            coin.setCmcRank(rankList.get(i));
            coin.setSaveDate(DateUtil.DateToString(new Date()));
            res.add(coin);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void importSinglePost(String url) {
        Spider.create(new PriceSpider())
                .addUrl(url)
                .addPipeline(new ConsolePipeline())
                .run();
    }


    public static List<Coin>  getCoin() throws InterruptedException {
        int limit=3000;
        int start=1;
        for (int i = 0; i < 2; i++) {
            start=start+limit*i;
            PriceSpider.importSinglePost("https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start="+start+"&&limit="+limit+"&&CMC_PRO_API_KEY=c4f7e968-cfd1-44db-9ff6-d02a13050e00");
        }
        return res;
    }
}
