package com.example.demo;


import com.example.demo.CsvUtil.CsvReaderUtil;
import com.example.demo.CsvUtil.DateUtil;
import com.example.demo.CsvUtil.FileReaderUtil;
import com.example.demo.Dao.*;
import com.example.demo.Service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Bidi;
import java.util.*;

@Slf4j
@SpringBootTest
public class ProfitTest {


    @Autowired
    IProfitService profitService;

    @Autowired
    ICoinService coinService;

    @Autowired
    IAddressCount2Service addressCount2Service;

    @Autowired
    IAddressCountService addressCountService;

    @Autowired
    IAddressService addressService;

    @Test
    public void CalculateProfit() throws IOException {
        List<String> readfiles = FileReaderUtil.readfile("F:\\csv folder");
        Set<String> set = new HashSet<>();
        set.add("ETH");
        set.add("USDT");
        set.add("WBTC");
        set.add("USDC");
        for (String file : readfiles) {
            Map<String, Profit> map = CsvReaderUtil.readCsvForProfit(file, set, -15, -2);
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                Profit profit = map.get(key);
                String address = file.split("\\\\")[2].split(" ")[0];
                profit.setAddress(address);
                profitService.saveProfit(profit);
            }
        }
    }

    @Test
    public void ProfitConclusion() throws IOException {
        List<AddressCount> addresses = addressCountService.findAddressByCountAndValid(6);
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> addressCount = new HashMap<>();
        for (AddressCount address : addresses) {
            String add = address.getAddress();
            List<Profit> profitList = profitService.findProfitByAddress(add);
            if (profitList.size() == 0) {
                continue;
            }
            BigDecimal preSum = new BigDecimal(0);
            BigDecimal curSum = new BigDecimal(0);
            int size = profitList.size();

            for (Profit profit : profitList) {
                if("CAVE".equals(profit.getCoin())||"GF".equals(profit.getCoin())||"ENS".equals(profit.getCoin())||"DELTA".equals(profit.getCoin())){
                    size--;
                    continue;
                }
                BigDecimal coinProfit = CalculateCoinProfit(profit);
                if (coinProfit.compareTo(new BigDecimal(-1)) == 0) {
                    size--;
                    continue;
                }

                BigDecimal temp = coinProfit.subtract(BigDecimal.valueOf(profit.getBuyPrice()));
                BigDecimal d = temp.divide(BigDecimal.valueOf(profit.getBuyPrice()), 3, RoundingMode.HALF_UP);

                profit.setTotalProfit(d.toString());
                profitService.updateProfile(profit);
                if(d.compareTo(new BigDecimal(5))>0){
                    size--;
                    continue;
                }
                preSum = preSum.add(BigDecimal.valueOf(profit.getBuyPrice()));
                curSum = curSum.add(coinProfit);
            }
            if (preSum.compareTo(new BigDecimal(0)) == 0) {
                continue;
            }
            BigDecimal sub = curSum.subtract(preSum);
            BigDecimal divide = sub.divide(preSum, 5, RoundingMode.HALF_UP);
            map.put(add, divide.toString());
            addressCount.put(add, size);
        }

        ArrayList<Map.Entry<String, String>> entries = sortMap(map);
        printConclusion(entries, addressCount);
    }


    @Test
    public void ProfitConclusionForRate() throws IOException {
        List<AddressCount> addresses = addressCountService.findAddressByCountAndValid(6);
        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> addressCount = new HashMap<>();

        for (AddressCount address : addresses) {
            String add = address.getAddress();
            List<Profit> profitList = profitService.findProfitByAddress(add);
            if (profitList.size() == 0) {
                continue;
            }
            int count = profitList.size();
            for (Profit profit : profitList) {
                if (profit.getTotalProfit() == null || "".equals(profit.getTotalProfit())) {
                    count--;
                }

                if (profit.getTotalProfit() != null && Float.parseFloat(profit.getTotalProfit()) > 0.3) {
                    map.put(add, map.getOrDefault(add, 0) + 1);
                }
            }
            addressCount.put(address.getAddress(), count);
        }
        printRateConclusion(map, addressCount);
    }


//    @Test
//    public void calculateCoins() throws IOException {
//        List<String> readfiles = FileReaderUtil.readfile("F:\\csv folder");
//        Set<String> set = new HashSet<>();
//        set.add("ETH");
//        set.add("USDT");
//        set.add("WBTC");
//        set.add("USDC");
//        Map<String, Integer> map = new HashMap<>();
//        Map<String, Integer> sumMap = new HashMap<>();
//        for (String file : readfiles) {
//            Set<String> csvForProfile = CsvReaderUtil.findCoins(file, set, -1, 0);
//            for (String coin : csvForProfile) {
//                map.put(coin, map.getOrDefault(coin, 0) + 1);
//            }
//        }
//        printCoinConclusion(map);
//    }


    public BigDecimal CalculateCoinProfit(Profit profit) {
        BigDecimal buyAmount = BigDecimal.valueOf(profit.getBuyAmount());
        BigDecimal sellAmount = BigDecimal.valueOf(profit.getSellAmount());
        String coinName = profit.getCoin();
        BigDecimal buyPrice = BigDecimal.valueOf(profit.getBuyPrice());
        BigDecimal sellPrice = BigDecimal.valueOf(profit.getSellPrice());
        BigDecimal total;
        if (buyAmount.compareTo(sellAmount) == 0) {
            total = sellPrice;
        } else if (buyAmount.compareTo(sellAmount) < 0) {
            if (buyAmount.compareTo(new BigDecimal(0)) == 0) {
                return new BigDecimal(-1);
            } else {
                total = sellPrice.divide(sellAmount, 8, RoundingMode.HALF_UP).multiply(buyAmount);
            }
        } else {
            BigDecimal leave = buyAmount.subtract(sellAmount);
            Coin coin = coinService.findCoinByShortName(coinName);
            if (coin == null) {
                return new BigDecimal(-1);
            }
            String unitPrice = coin.getPrice();
            BigDecimal leavePrice = leave.multiply(new BigDecimal(unitPrice));
            total = leavePrice.add(sellPrice);
        }

        return total;
    }

    public static void printConclusion(ArrayList<Map.Entry<String, String>>  map, Map<String, Integer> addressCount) throws IOException {

        String path = "C:\\Users\\Lenovo\\Desktop\\profit.txt";
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));

        for (Map.Entry<String, String> entry : map) {
            String val = entry.getValue();
            String key = entry.getKey();
            Integer count = addressCount.get(key);
            if (!"".equals(val) && val != null && count > 5  && Float.parseFloat(val)>0) {
                writer.write("地址：" + key + "  利润率:  " + val + "  总购买数为： " + count + "\r\n");
            }
        }
        writer.close();
    }


    public static void printRateConclusion(Map<String, Integer> map, Map<String, Integer> addressCount) throws IOException {

        String path = "C:\\Users\\Lenovo\\Desktop\\coin_sum.txt";
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));

        Set<String> keySet = map.keySet();

        for (String key : keySet) {
            Integer count = addressCount.get(key);
            if (count > 2 ) {

                writer.write("地址：" + key + "  利润率:  " + (float)map.get(key)/(float)count + "  总购买数为： " + count + "\r\n");

            }
        }
        writer.close();
    }


    public static ArrayList<Map.Entry<String, String>> sortMap(Map<String, String> map){
        Set<Map.Entry<String,String>> entrySet = map.entrySet();
        ArrayList<Map.Entry<String, String>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String, String>>() {

            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return new BigDecimal(o2.getValue()).compareTo(new BigDecimal(o1.getValue()));
            }

        });


        return list;
    }

    public static void main(String[] args) {
        sortMap(new HashMap<>());
    }
}



