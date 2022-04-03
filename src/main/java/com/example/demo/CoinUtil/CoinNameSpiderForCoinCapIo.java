package com.example.demo.CoinUtil;

import com.example.demo.Dao.CoinName;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.ArrayList;
import java.util.List;

public class CoinNameSpiderForCoinCapIo implements PageProcessor {
    //    https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?limit=200&&CMC_PRO_API_KEY=c4f7e968-cfd1-44db-9ff6-d02a13050e00
    private Site site = Site
            .me()
            .setDomain("https://cryptorank.io/")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36")
            .addHeader("Authorization","9c4ddcf7-75cd-42f7-ab14-76c90f2e0b50");

    private static List<CoinName> res = new ArrayList<>();

    @Override
    public void process(Page page) {
        String rawText = page.getRawText();
//        JsonPathSelector nameSelector = new JsonPathSelector("$.data[*].name");
        JsonPathSelector symbolSelector = new JsonPathSelector("$.data[*].id");
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
        Spider.create(new CoinNameSpiderForCoinCapIo())
                .addUrl(url)
                .addPipeline(new ConsolePipeline())
                .run();
    }


    public static List<CoinName> getCoin() throws InterruptedException {
        int val=0;
        for (int i = 0; i < 2; i++) {
            CoinNameSpiderForCoinCapIo.importSinglePost("https://api.coincap.io/v2/assets?limit=1500&&offset="+val);
            val+=1501;
        }
        return res;
    }
}
