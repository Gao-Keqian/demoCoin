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

    @RequestMapping("/getCoinName")
    public String getCoinName() throws InterruptedException {
        List<List<String>> coinName = SpiderUtil.getCoinName();
        List<String> list = coinName.get(0);
        List<String> sList = coinName.get(1);

        for (int i = 0; i < list.size(); i++) {
            Coin coin = new Coin();
            coin.setName(list.get(i));
            coin.setShortName(sList.get(i));
            coinService.saveCoin(coin);
        }
        return "Successs";
    }


    @RequestMapping("/getAddress")
    public String getAddress() throws InterruptedException {
        List<CoinName> coins = nameService.findAll();
        List<String> coinNames = addressService.selectCoinName();
        Set<String> set=new HashSet<>(coinNames);


        int i = 0;
        List<String> list=new ArrayList<>();
        for (CoinName coin : coins) {
            if(set.contains(coin.getName())){
                continue;
            }
            i++;

            System.out.println("当前为第"+i+"个");
            List<Address> addresses=new ArrayList<>();
            try {
                addresses = AddressUtil.getAddress(coin);
            }catch (Exception e){
                list.add(coin.getName());
            }
            System.out.println("地址数量为"+addresses.size());
            int j=0;
            for (Address s : addresses) {
                if(j>=100){
                    break;
                }
                addressService.saveAddress(s);
                j++;
            }
            System.out.println("==================================");
        }
        for (String s : list) {
            Address address=new Address();
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
            if(map.get(s)<=5){
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
        List<AddressCount> addresses = addressCountService.findAddressByCount(6);
        for (AddressCount address : addresses) {
            if (address.getValid() != null) {
                continue;
            }
            System.out.println("当前添加地址" + address);
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


    @RequestMapping("/getCoinCount")
    public String getCoinCount() throws InterruptedException {
//        List<AddressCount> addresses = addressCountService.findAddressByCountAndValid(15);
        List<AddressCount> addresses = addressCountService.findAddressByHighLight();
        for (AddressCount address : addresses) {
//            Integer count = addressCoinService.findByAddress(address.getAddress());
//            if(count!=0){
//                continue;
//            }
            List<String> addressCoins = ValidateAddress.getAddressCoin(address.getAddress());
            for (String s : addressCoins) {
                AddressCoin addressCoin = new AddressCoin();
                addressCoin.setAddress(address.getAddress());
                addressCoin.setCoin(s);
                addressCoinService.saveCoin(addressCoin);
            }
        }
        return "Successs";
    }

    @RequestMapping("/getHistory")
    public String getHistory() throws InterruptedException {
        List<AddressCount> addresses = addressCountService.findAddressByCountAndValid(15);
        for (AddressCount address : addresses) {
            List<BuyHistory> historyListh = HistorySpider.getHistory(address.getAddress());
            for (BuyHistory history : historyListh) {
                historyService.saveHistory(history);
            }
        }
        return "Successs";
    }

    @RequestMapping("/downloadCsv")
    public String downloadTransactionHistoryCsv() throws InterruptedException {
        List<AddressCount> addresses = addressCountService.findAddressByCountAndValid(5);
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

    @RequestMapping("/downloadHighLightCsv")
    public String downloadHighLightCsv() throws InterruptedException {
        List<AddressCount2> addressCount2s = addressCount2Service.findAddressByHighLight();

        for (AddressCount2 address : addressCount2s) {

            DownloadSpider.download(address.getAddress());

        }
        return "Successful";
    }


    @RequestMapping("/CalculateCoin")
    public String CalculateCoin() throws InterruptedException, IOException {
        List<AddressCount2> addressCount2s = addressCount2Service.findAddressByHighLight();
        Map<String, List<String>> map = new HashMap<>();
        for (AddressCount2 address : addressCount2s) {

            GetAddressCoinSpider.download(address.getAddress(), map);

        }

        PrintResultUtil.printAddressCoin(map);



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

}
