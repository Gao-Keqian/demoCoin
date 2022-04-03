package com.example.demo;


import com.example.demo.CoinUtil.DownloadSpider;
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
import java.text.DecimalFormat;
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

    @Autowired
    IProfitInfoService profitInfoService;

    @Test
    public void CalculateProfit() throws IOException {
        List<String> readfiles = FileReaderUtil.readfile("F:\\csv folder");
        Set<String> set = new HashSet<>();
        set.add("ETH");
        set.add("USDT");
        set.add("WBTC");
        set.add("USDC");
        for (String file : readfiles) {
            Map<String, Profit> map = CsvReaderUtil.readCsvForProfit(file, set, -10, -5);
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                Profit profit = map.get(key);
                String address = file.split("\\\\")[2].split(" ")[0];
                profit.setAddress(address);
                try {
                    profitService.saveProfit(profit);
                } catch (Exception e) {
                    System.out.println(1);
                }
            }
        }
    }

    @Test
    public void ProfitConclusion() throws IOException {
        List<AddressCount> addresses = addressCountService.findAddressByCountAndValid(5);
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
                if ("CAVE".equals(profit.getCoin()) ||
                        "GF".equals(profit.getCoin()) || "ENS".equals(profit.getCoin()) ||
                        "DELTA".equals(profit.getCoin()) || "AUDIO".equals(profit.getCoin()) ||
                        "CERE".equals(profit.getCoin()) ||
                        "JASMY".equals(profit.getCoin())
                ) {
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
                if (d.compareTo(new BigDecimal(2.5)) > 0) {
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
        for (Map.Entry<String, String> entry : entries) {
            String val = entry.getValue();
            String key = entry.getKey();
            ProfitInfo profitInfo = new ProfitInfo();
            profitInfo.setAddress(key);
            profitInfo.setProfit(Double.parseDouble(val));
            profitInfoService.saveProfitInfo(profitInfo);
        }
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
                if (profit.getTotalProfit() == null || "".equals(profit.getTotalProfit()) || Float.parseFloat(profit.getTotalProfit()) > 2.5) {
                    count--;
                    continue;
                }
                if ("CAVE".equals(profit.getCoin()) ||
                        "GF".equals(profit.getCoin()) || "ENS".equals(profit.getCoin()) ||
                        "DELTA".equals(profit.getCoin()) || "AUDIO".equals(profit.getCoin()) ||
                        "CERE".equals(profit.getCoin()) ||
                        "JASMY".equals(profit.getCoin())
                ) {
                    count--;
                    continue;
                }
                if (Float.parseFloat(profit.getTotalProfit()) > 0.1) {
                    map.put(add, map.getOrDefault(add, 0) + 1);
                }
            }
            addressCount.put(address.getAddress(), count);
        }
        Set<String> set = map.keySet();
        DecimalFormat df = new DecimalFormat("0.00");
        for (String key : set) {
            int val = map.get(key);
            int count = addressCount.get(key);

            String res = df.format((double) val / count);
            ProfitInfo profitInfo = profitInfoService.findProfitInfoByAddress(key);
            profitInfo.setProfitRate(Double.parseDouble(res));
            profitInfo.setCount(count);
            profitInfoService.updateProfitInfo(profitInfo);
        }
    }

    @Test
    public void printConclusion() throws IOException {

        String path = "C:\\Users\\Lenovo\\Desktop\\profit_info.txt";
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        List<ProfitInfo> profitInfos = profitInfoService.findProfitInfo(7, 0.4, 0.15);
        int count = 0;
        for (ProfitInfo profitInfo : profitInfos) {
            if (profitInfo.getProfit() < 0.1 || (profitInfo.getProfit() < 0.15 && profitInfo.getProfitRate() < 0.5) || profitInfo.getProfitRate() < 0.4) {
                continue;
            }
            count++;
            writer.write("地址：" + profitInfo.getAddress() + "  利润率:  " + profitInfo.getProfit() +
                    "  获利比率： " + profitInfo.getProfitRate() +
                    "  总购买数为： " + profitInfo.getCount() +
                    "\r\n");
        }
        writer.write("总计： " + count);
        writer.close();
    }


    @Test
    public void downloadProfitAddress() throws InterruptedException {
        List<ProfitInfo> profitInfos = profitInfoService.findProfitInfoByFlag();
        for (ProfitInfo profitInfo : profitInfos) {
            DownloadSpider.download(profitInfo.getAddress());
        }
    }


    @Test
    public void getCoins() throws IOException {

        String path = "C:\\Users\\Lenovo\\Desktop\\profit_info.txt";
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        List<ProfitInfo> profitInfos = profitInfoService.findProfitInfo(7, 0.4, 0.15);
        int count = 0;
        for (ProfitInfo profitInfo : profitInfos) {
            if (profitInfo.getProfit() < 0.1 || (profitInfo.getProfit() < 0.15 && profitInfo.getProfitRate() < 0.5) || profitInfo.getProfitRate() < 0.4) {
                continue;
            }
            count++;
            writer.write("地址：" + profitInfo.getAddress() + "  利润率:  " + profitInfo.getProfit() +
                    "  获利比率： " + profitInfo.getProfitRate() +
                    "  总购买数为： " + profitInfo.getCount() +
                    "\r\n");
        }
        writer.write("总计： " + count);
        writer.close();
    }


    @Test
    public void setFlagNull(){
        List<ProfitInfo> profitInfos = profitInfoService.findAll();
        for (ProfitInfo profitInfo : profitInfos) {
            if("T".equals(profitInfo.getFlag())){
                profitInfo.setFlag(null);
                profitInfoService.updateProfitInfo(profitInfo);
            }
        }
    }


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

    public static ArrayList<Map.Entry<String, String>> sortMap(Map<String, String> map) {
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        ArrayList<Map.Entry<String, String>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {

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


//    private void moveTotherFolders(String pathName,String fileName,String ansPath){
//        String startPath = this.path + pathName + File.separator + fileName;
//        String endPath = ansPath + File.separator + currentDate + File.separator;
//        try {
//            File startFile = new File(startPath);
//            File tmpFile = new File(endPath);//获取文件夹路径
//            if(!tmpFile.exists()){//判断文件夹是否创建，没有创建则创建新文件夹
//                tmpFile.mkdirs();
//            }
//            System.out.println(endPath + startFile.getName());
//            if (startFile.renameTo(new File(endPath + startFile.getName()))) {
//                System.out.println("File is moved successful!");
//                log.info("文件移动成功！文件名：《{}》 目标路径：{}",fileName,endPath);
//            } else {
//                System.out.println("File is failed to move!");
//                log.info("文件移动失败！文件名：《{}》 起始路径：{}",fileName,startPath);
//            }
//        } catch (Exception e) {
//            log.info("文件移动异常！文件名：《{}》 起始路径：{}",fileName,startPath);
//
//        }
//    }
}



