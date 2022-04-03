package com.example.demo.CoinUtil;

import com.example.demo.Dao.CoinName;
import com.example.demo.Dao.HistoryPrice;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryPriceSpider implements PageProcessor {
    //    https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?limit=200&&CMC_PRO_API_KEY=c4f7e968-cfd1-44db-9ff6-d02a13050e00
    private Site site = Site
            .me()
            .setDomain("https://cryptorank.io/")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36")
            .addHeader("Authorization","9c4ddcf7-75cd-42f7-ab14-76c90f2e0b50");


    private static List<HistoryPrice> res = new ArrayList<>();

    @Override
    public void process(Page page) {
        String rawText = page.getRawText();

        JsonPathSelector symbolSelector = new JsonPathSelector("$.data[*].priceUsd");
        JsonPathSelector dateSelector = new JsonPathSelector("$.data[*].date");
        List<String> priceList = symbolSelector.selectList(rawText);
        List<String> dateList = dateSelector.selectList(rawText);
        for (int i = 0; i < priceList.size(); i++) {
            HistoryPrice historyPrice=new HistoryPrice();
            historyPrice.setPrice(priceList.get(i));
            try {
                historyPrice.setCreatedDate(interceptTime(dateList.get(i)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            res.add(historyPrice);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void importSinglePost(String url) {
        Spider.create(new HistoryPriceSpider())
                .addUrl(url)
                .addPipeline(new ConsolePipeline())
                .run();
    }


    public static List<HistoryPrice> getPrice(String coinName) throws InterruptedException {
//
        HistoryPriceSpider.importSinglePost("https://api.coincap.io/v2/assets/"+coinName+"/history?interval=d1");

        return res;
    }


    public static Date interceptTime(String timeStr) throws ParseException {

        String strDate = timeStr.substring(0, 10);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.parse(strDate);
    }
}
