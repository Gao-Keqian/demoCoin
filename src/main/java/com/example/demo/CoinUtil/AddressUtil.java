package com.example.demo.CoinUtil;

import com.example.demo.Dao.Address;
import com.example.demo.Dao.Coin;
import com.example.demo.Dao.CoinName;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.ArrayList;
import java.util.List;

public class AddressUtil implements PageProcessor {

    private Site site = Site
            .me()
            .setDomain("https://cryptorank.io/")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");

    private static List<String> addressList = new ArrayList<>();
    private static List<String> amountList = new ArrayList<>();

    @Override
    public void process(Page page) {
        String rawText = page.getRawText();
        JsonPathSelector addressSelector = new JsonPathSelector("$.data.holders[*].address");
        JsonPathSelector balanceSelector = new JsonPathSelector("$.data.holders[*].balance");
        List<String> aList = addressSelector.selectList(rawText);
        List<String> bList = balanceSelector.selectList(rawText);
        addressList.addAll(aList);
        amountList.addAll(bList);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void importSinglePost(String url) {
        Spider.create(new AddressUtil())
                .addUrl(url)
                .addPipeline(new ConsolePipeline())
                .run();
    }


    public static List<Address> getAddress(CoinName coin) throws InterruptedException {
        String fName = coin.getName();
        AddressUtil.importSinglePost("https://api.cryptorank.io/v0/coins/" + fName + "/holders");
        List<Address> res = new ArrayList<>();
        for (int i = 0; i < addressList.size(); i++) {
            Address address = new Address();
            address.setCoinName(fName);
            address.setAddress(addressList.get(i));
            address.setOlderAmount(amountList.get(i));
            res.add(address);
        }
        addressList = new ArrayList<>();
        amountList = new ArrayList<>();
        return res;

    }
}
