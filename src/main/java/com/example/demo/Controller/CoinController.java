package com.example.demo.Controller;

import com.example.demo.CoinUtil.*;
import com.example.demo.CsvUtil.PrintResultUtil;
import com.example.demo.Dao.*;
import com.example.demo.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;

import java.io.IOException;
import java.util.*;

@RestController
public class CoinController {
    @Autowired
    ICoinService coinService;

    @Autowired
    ICoinNameService nameService;

    @Autowired
    IAddressService addressService;

    @Autowired
    IAddressCountService addressCountService;

    @Autowired
    IAddressCount2Service addressCount2Service;

    @Autowired
    IAddressCoinService addressCoinService;

    @Autowired
    IHistoryService historyService;

    @Autowired
    IHistoryPriceService historyPriceService;

    // 第一步 获取名字 为了得到地址
    @RequestMapping("/getCoinName")
    public String getCoinName() throws InterruptedException {
        List<CoinName> coins = CoinNameSpider.getCoin();

        for (CoinName coin : coins) {
            nameService.saveCoinName(coin);
        }
        return "Successs";
    }

    // 第二步 去拿所有地址
    @RequestMapping("/getAddress")
    public String getAddress() throws InterruptedException {
        List<CoinName> coins = nameService.findAll();
        List<String> coinNames = addressService.selectCoinName();
        Set<String> set = new HashSet<>(coinNames);


        int i = 0;
        List<String> list = new ArrayList<>();
        for (CoinName coin : coins) {
            if (set.contains(coin.getName())) {
                continue;
            }
            i++;

            System.out.println("当前为第" + i + "个");
            List<Address> addresses = new ArrayList<>();
            try {
                addresses = AddressUtil.getAddress(coin);
            } catch (Exception e) {
                list.add(coin.getName());
            }
            System.out.println("地址数量为" + addresses.size());
            int j = 0;
            for (Address s : addresses) {
                if (j >= 100) {
                    break;
                }
                addressService.saveAddress(s);
                j++;
            }
            System.out.println("==================================");
        }
        for (String s : list) {
            Address address = new Address();
            address.setCoinName(s);
            address.setAddress("F");
            addressService.saveAddress(address);
        }


        return "Successs";
    }


    @RequestMapping("/calculate")
    public String calculate() throws InterruptedException {
        List<Address> addresses = addressService.findAll();
        Map<String, Integer> map = new HashMap<>();
        for (Address address : addresses) {
            map.put(address.getAddress(), map.getOrDefault(address.getAddress(), 0) + 1);
        }
        Set<String> keySet = map.keySet();
        for (String s : keySet) {
            if (map.get(s) <= 3) {
                continue;
            }
            AddressCount addressCount = new AddressCount();
            addressCount.setAddress(s);
            addressCount.setCount(map.get(s));
            addressCountService.saveAddressCount(addressCount);
        }
        return "Successs";
    }


    @RequestMapping("/valid")
    public String validateAddress() throws InterruptedException {
        List<AddressCount> addresses = addressCountService.findAddressByCount(0);
        System.out.println("总地址数为：" + addresses.size());
        int i = 1;
        for (AddressCount address : addresses) {
            if (address.getValid() != null) {
                continue;
            }
            System.out.println("当前添加第" + i++ + "个地址" + address);
            String validAddress = ValidateAddress.getValidAddress(address.getAddress());
            if ("地址无法添加".equals(validAddress)) {
                address.setValid("F");
            } else {
                address.setValid("T");
            }
            addressCountService.updateAddressCount(address);
        }
        return "Successs";
    }


    @RequestMapping("/downloadCsv")
    public String downloadTransactionHistoryCsv() throws InterruptedException {
        List<AddressCount> addresses = addressCountService.findAddressByCountAndValid(6);
        for (AddressCount address : addresses) {
            DownloadSpider.download(address.getAddress());
        }
        return "Successful";
    }


    @RequestMapping("/downloadCsv2")
    public String downloadTransactionHistoryCsv2() throws InterruptedException {
        List<AddressCount2> addressCount2s = addressCount2Service.findAddressByCountAndValid(6);

        for (AddressCount2 address : addressCount2s) {
//            AddressCount address1 = addressCountService.findAddressByCountAndValidAndAddress(7, address.getAddress());
//            if (address1 != null) {
//                continue;
//            }
            boolean res = DownloadSpider.download(address.getAddress());
            if (!res) {
                address.setHighLight("F");
                addressCount2Service.updateAddressCount(address);
            }
        }
        return "Successful";
    }


    @RequestMapping("/getCoin")
    public String getCoin() throws InterruptedException {
        List<Coin> coinName = PriceSpider.getCoin();
        for (Coin coin : coinName) {
            coinService.saveCoin(coin);
        }
        return "Successs";
    }


    @RequestMapping("/getCoinForHistoryPrice")
    public String getCoinForHistoryPrice() throws InterruptedException {
        List<CoinName> coinName = CoinNameSpiderForCoinCapIo.getCoin();
        for (CoinName coin : coinName) {
            nameService.saveCoinName(coin);
        }
        return "Successs";
    }


    @RequestMapping("/getHistoryPrice")
    public String getrHistoryPrice() throws InterruptedException {
        List<HistoryPrice> historyPrices = HistoryPriceSpider.getPrice("bitcoin");
        for (HistoryPrice historyPrice : historyPrices) {
            historyPrice.setCoinNameId(1);
            historyPriceService.saveHistory(historyPrice);
        }
        return "Successs";
    }

}
