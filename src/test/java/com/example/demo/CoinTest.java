package com.example.demo;

import com.example.demo.CoinUtil.DownloadSpider;
import com.example.demo.CsvUtil.CsvReaderUtil;
import com.example.demo.CsvUtil.DateUtil;
import com.example.demo.CsvUtil.FileReaderUtil;
import com.example.demo.Dao.*;
import com.example.demo.Service.*;
import com.shapesecurity.salvation2.Values.Hash;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map.Entry;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@SpringBootTest
public class CoinTest {

    @Autowired
    IAddressCoinService coinService;

    @Autowired
    ICoinCountService coinCountService;

    @Autowired
    IAddressCount2Service addressCount2Service;

    @Autowired
    IAddressCountService addressCountService;

    @Autowired
    IHistoryService historyService;

    @Autowired
    ICoinService cService;

    @Test
    public void test() {
        List<AddressCoin> coins = coinService.findAll();
        Map<String, Integer> map = new HashMap<>();
        for (AddressCoin coin : coins) {
            map.put(coin.getCoin(), map.getOrDefault(coin.getCoin(), 0) + 1);
        }

        Set<String> keySet = map.keySet();
        for (String s : keySet) {
            CoinCount coinCount = new CoinCount();
            coinCount.setName(s);
            coinCount.setCount(map.get(s));
            coinCountService.saveAddressCount(coinCount);
        }

    }

    @Test
    public void getHistory() throws IOException {
        List<BuyHistory> histories = historyService.findByAction();
        Map<String, Integer> map = new HashMap<>();
        for (BuyHistory history : histories) {
            map.put(history.getCoin(), map.getOrDefault(history.getCoin(), 0) + 1);
        }
        String path = "C:\\Users\\Lenovo\\Desktop\\coin2.txt";
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));

        Set<String> keySet = map.keySet();
        for (String key : keySet) {
//            if (map.get(key) < 2) {
//                continue;
//            }
            writer.write(key + "    " + map.get(key) + "\r\n");
        }
        writer.close();
    }


    @Test
    public void getHistoryByTransaction() throws IOException {
        List<String> readfiles = FileReaderUtil.readfile("F:\\csv folder");
        for (String file : readfiles) {
            List<BuyHistory> historyList = CsvReaderUtil.readCsv(file);
            for (BuyHistory history : historyList) {
                historyService.saveHistory(history);
            }
        }
    }


    @Test
    public void findMissedFiles() throws IOException, InterruptedException {
        Set<String> readfiles = FileReaderUtil.readfileName("F:\\csv folder");
        List<AddressCount2> addressCount2s = addressCount2Service.findAddressByCountAndValid(9);

        for (AddressCount2 address : addressCount2s) {
            if (readfiles.contains(address.getAddress())) {
                continue;
            }
            boolean res = DownloadSpider.download(address.getAddress());
            if (!res) {
                address.setHighLight("F");
                addressCount2Service.updateAddressCount(address);
            }
        }
    }


    @Test
    public void findCoin() throws IOException {
        List<String> readfiles = FileReaderUtil.readfile("F:\\csv folder");
        Set<String> set= new HashSet<>();
        for (int i = -30; i <=-3 ; i++) {
            for (String file : readfiles) {
                if (CsvReaderUtil.findCsv(file, i)) {
                    if(!set.contains(file)){
                        System.out.println(file);

                    }

                    set.add(file);
                }
                ;
            }
        }
    }


    @Test
    public void calculateProfile() throws IOException {
        List<String> readfiles = FileReaderUtil.readfile("F:\\csv folder");
        Set<String> set = new HashSet<>();
        set.add("ETH");
        set.add("USDT");
        set.add("WBTC");
        set.add("USDC");
        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> sumMap = new HashMap<>();
        for (int i = -7; i <= -1; i++) {
            Date previousDate = DateUtil.getPreviousDate(i);

//            String filePath = creatDoc("profile", DateUtil.DateToString(previousDate));

            for (String file : readfiles) {
                List<CoinInfo> csvForProfile = CsvReaderUtil.readCsvForProfile(file, set, i);
                List<CoinInfo> res = new ArrayList<>();
                int increase = 0;
                int decrease = 0;
                for (CoinInfo coinInfo : csvForProfile) {
                    Coin coin = cService.findCoinByShortName(coinInfo.getCoin());
                    if (coin == null) {
                        continue;
                    }
                    String price = coin.getPrice();
                    BigDecimal increaseRate = new BigDecimal(price).subtract(new BigDecimal(coinInfo.getPrice())).divide(new BigDecimal(coinInfo.getPrice()), 2, RoundingMode.HALF_UP);
                    if (increaseRate.compareTo(new BigDecimal(0.3)) > 0) {
                        map.put(file, map.getOrDefault(file, 0) + 1);
                    }
                    sumMap.put(file, sumMap.getOrDefault(file, 0) + 1);

                    if (increaseRate.compareTo(new BigDecimal(0)) > 0) {
                        increase++;
                    } else {
                        decrease++;
                    }
                    coinInfo.setRank(coin.getCmcRank());
                    coinInfo.setProfile(increaseRate.toString());
                    res.add(coinInfo);
                }
//                printInfo(filePath, file, res, increase, decrease);
            }
        }

        printConclusion(map, sumMap);
    }


    @Test
    public void calculateCoinAdded() throws IOException {
        List<String> readfiles = FileReaderUtil.readfile("F:\\csv folder");
        Set<String> set = new HashSet<>();
        set.add("ETH");
        set.add("USDT");
        set.add("WBTC");
        set.add("USDC");
        for (String file : readfiles) {
            Map<String, Integer> map = new HashMap<>();
            for (int i = -30; i <= -25; i++) {
                List<String> csvBasedOnAddress = CsvReaderUtil.readCsvBasedOnAddress(file, set, i);
                for (String coin : csvBasedOnAddress) {
                    map.put(coin, map.getOrDefault(coin, 0) + 1);
                }
            }
            String filePath = creatDoc("add", file.substring(14, 20));
            printAddedCoin(filePath, file, map);

        }

    }


    @Test
    public void compareAddress1AndAddress2() {
        List<AddressCount> addressByCount = addressCountService.findAddressByCount(6);
        for (AddressCount addressCount : addressByCount) {
            AddressCount2 address = addressCount2Service.findAddress(addressCount.getAddress());
            if (address != null && address.getValid() != null) {
                addressCount.setValid(address.getValid());
                addressCountService.updateAddressCount(addressCount);
            }
        }

    }


    public static void printAddedCoin(String filePath, String address, Map<String, Integer> map) throws IOException {
        String path = filePath + "\\" + address.substring(14, 20) + ".txt";
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));

        Set<String> set = map.keySet();
        for (String s : set) {
            writer.write("Coin：" + s + "总数：" + map.get(s) + "\r\n");
        }
        writer.close();
    }


    public static void printConclusion(Map<String, Integer> map, Map<String, Integer> sumMap) throws IOException {

        String path = "C:\\Users\\Lenovo\\Desktop\\coin.txt";
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));

        Set<String> set = map.keySet();

        for (String s : set) {
            double temp = (double) map.get(s) / (double) sumMap.get(s);
//            if(sumMap.get(s)>5&&temp>0.15)
                 writer.write("地址：" + s + "总数：" + map.get(s) + "  占比：" + temp + "  总购买数" + sumMap.get(s) + "\r\n");
        }
        writer.close();
    }

    public static String creatDoc(String filepath, String fileName) {

        File file = new File("F:\\" + filepath + "\\" + fileName);
        if (!file.exists()) {//如果文件夹不存在
            file.mkdir();//创建文件夹
        }
        return file.getPath();
    }

    public static void printInfo(String filePath, String address, List<CoinInfo> coinInfos, int increase, int decrease) throws IOException {

        Set<String> set = new HashSet<>();
        set.add("CTR");
        set.add("ROYA");
        set.add("RFOX");
        set.add("DERC");
        String path = filePath + "\\" + address.substring(14, 20) + ".txt";
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));

        for (CoinInfo coinInfo : coinInfos) {
            if (set.contains(coinInfo.getCoin())) {
                System.out.println(path);
            }
            writer.write(coinInfo.getCoin() + "    Profile: " + coinInfo.getProfile() + "   Rank:  " + coinInfo.getRank() + "\r\n");
        }
        writer.write("增长数：" + increase + "下跌数：" + decrease);

        writer.close();
    }

    public static void main(String[] args) {
        System.out.println("134159923108436880000000134159.92310843688".length());
    }


}
