package com.example.demo.CsvUtil;

import com.csvreader.CsvReader;
import com.example.demo.Dao.BuyHistory;
import com.example.demo.Dao.CoinInfo;
import com.example.demo.Dao.Profit;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CsvReaderUtil {

    public static List<BuyHistory> readCsv(String filePath) {
        List<BuyHistory> res = new ArrayList<>();
        Set<String> set = new HashSet<>();
        try {
            CsvReader csvReader = new CsvReader(filePath);
            int i = 0;
            while (csvReader.readRecord()) {
                if (i == 0) {
                    i++;
                    continue;
                }
                // 读一整行
                String rawRecord = csvReader.getRawRecord();
                rawRecord = rawRecord.replaceAll("\"", "");
                String[] split = rawRecord.split(",");
                // 读这行的某一列
                String transactionType = split[2];
                if ("Send".equals(transactionType)) {
                    continue;
                }
                String buyCurrency = split[7];
                if (set.contains(buyCurrency)) {
                    continue;
                }
                Date date = DateUtil.StringToDate(split[0]);
                Date previousDate = DateUtil.getPreviousDate(-5);
                Date lastFiveDate = DateUtil.getPreviousDate(0);
//                if (date.compareTo(lastFiveDate) <= 0 && date.compareTo(previousDate) >= 0) {
                if (DateUtils.isSameDay(date, lastFiveDate)) {
                    BuyHistory buyHistory = new BuyHistory();
                    buyHistory.setBuyDate(date);
                    buyHistory.setCoin(buyCurrency);
                    buyHistory.setNum(split[9]);
                    buyHistory.setAction("buy");
                    res.add(buyHistory);
                    set.add(buyCurrency);
//                } else if (date.compareTo(previousDate) < 0) {
                } else if (lastFiveDate.compareTo(date) > 0) {
                    break;
                }
            }
            csvReader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static List<String> readCsvBasedOnAddress(String filePath, Set<String> coinSet, int num) {
        List<String> res = new ArrayList<>();
        Set<String> set = new HashSet<>();
        try {
            CsvReader csvReader = new CsvReader(filePath);
            int i = 0;
            while (csvReader.readRecord()) {
                if (i == 0) {
                    i++;
                    continue;
                }
                // 读一整行
                String rawRecord = csvReader.getRawRecord();
                rawRecord = rawRecord.replaceAll("\"", "");
                String[] split = rawRecord.split(",");
                // 读这行的某一列
                String transactionType = split[2];
//
                if (!"Receive".equals(transactionType) || "".equals(split[9]) || split[9] == null) {
                    continue;
                }
                String buyCurrency = split[7];
                if (set.contains(buyCurrency) || coinSet.contains(buyCurrency)) {
                    continue;
                }
                Date date = DateUtil.StringToDate(split[0]);
                Date previousDate = DateUtil.getPreviousDate(num);
                if (DateUtils.isSameDay(date, previousDate)) {
                    res.add(buyCurrency);
                    set.add(buyCurrency);
                } else if (date.compareTo(previousDate) < 0) {
                    break;
                }
            }
            csvReader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return res;
    }


    public static boolean findCsv(String filePath, int num) {

        try {
            CsvReader csvReader = new CsvReader(filePath);
            int i = 0;
            while (csvReader.readRecord()) {
                if (i == 0) {
                    i++;
                    continue;
                }
                // 读一整行
                String rawRecord = csvReader.getRawRecord();
                rawRecord = rawRecord.replaceAll("\"", "");
                String[] split = rawRecord.split(",");
                // 读这行的某一列
                String transactionType = split[2];
                if (!"Receive".equals(transactionType)) {
                    continue;
                }
                String currency = split[7];
                Date date = DateUtil.StringToDate(split[0]);
                Date previousDate = DateUtil.getPreviousDate(num);
                if (DateUtils.isSameDay(date, previousDate)) {
                    if ("ROUTE".equals(currency) ) {
                        return true;
                    }
                } else if (date.compareTo(previousDate) < 0) {
                    break;
                }
            }
            csvReader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static List<CoinInfo> readCsvForProfile(String filePath, Set<String> coinSet, int num) {
        List<CoinInfo> res = new ArrayList<>();
        try {
            CsvReader csvReader = new CsvReader(filePath);
            int i = 0;
            while (csvReader.readRecord()) {
                if (i == 0) {
                    i++;
                    continue;
                }
                String rawRecord = csvReader.getRawRecord();
                rawRecord = rawRecord.replaceAll("\"", "");
                String[] split = rawRecord.split(",");
                if (split.length < 9) {
                    continue;
                }
                String transactionType = split[2];
                if (!"Receive".equals(transactionType) || "".equals(split[9]) || split[9] == null || split[6].length() > 20) {
                    continue;
                }

                String buyCurrency = split[7];
                if (coinSet.contains(buyCurrency)) {
                    continue;
                }

                Date date = DateUtil.StringToDate(split[0]);
                Date previousDate = DateUtil.getPreviousDate(num);
//                if(lastFiveDate.compareTo(parse) >= 0&&previousDate.compareTo(parse)<=0){
                String totalPrice = split[9].replace("\n", "");
                String amount = split[6].replace("\n", "");
                if (DateUtils.isSameDay(date, previousDate)) {
                    BigDecimal price = null;

                    try {
                        price = new BigDecimal(totalPrice).divide(new BigDecimal(amount), 5, RoundingMode.HALF_UP);
                    } catch (Exception e) {
                        System.out.println(1);
                    }
                    if (price == null) {
                        continue;
                    }
                    CoinInfo coinInfo = new CoinInfo();
                    coinInfo.setCoin(buyCurrency);
                    coinInfo.setPrice(price.toString());
                    res.add(coinInfo);
                } else if (previousDate.compareTo(date) > 0) {
//                }else if(lastFiveDate.compareTo(parse)>0){
                    break;
                }
            }
            csvReader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return res;
    }


    public static Map<String, Profit> readCsvForProfit(String filePath, Set<String> coinSet, int start, int end) {
        Map<String, Profit> map = new HashMap<>();
        try {
            CsvReader csvReader = new CsvReader(filePath);
            int i = 0;
            while (csvReader.readRecord()) {
                if (i == 0) {
                    i++;
                    continue;
                }
                String rawRecord = csvReader.getRawRecord();
                rawRecord = rawRecord.replaceAll("\"", "");
                String[] split = rawRecord.split(",");
                if (split.length < 16) {
                    break;
                }
                String transactionType = split[2];

                if (!"Receive".equals(transactionType) && !"Send".equals(transactionType)) {
                    continue;
                }

                Date date = DateUtil.StringToDate(split[0]);
                Date startDate = DateUtil.getPreviousDate(start);
                Date endDate = DateUtil.getPreviousDate(end);
                String currency = null;
                Profit profit = null;
                if (startDate.compareTo(date) <= 0 && endDate.compareTo(date) >= 0) {
                    if ("Send".equals(transactionType)) {
                        currency = split[12];
                        if ("".equals(split[14]) || split[14] == null || coinSet.contains(currency)) {
                            continue;
                        }

                        String amount = split[11];
                        String price = split[14];
                        float sellPrice = 0;
                        float sellAmount = 0;
                        try {
                            sellPrice = Float.parseFloat(price);
                            sellAmount = Float.parseFloat(amount);
                        } catch (Exception e) {
                            continue;
                        }
                        if (map.containsKey(currency)) {
                            profit = map.get(currency);
                            sellPrice += profit.getSellPrice();
                            sellAmount += profit.getSellAmount();
                        } else {
                            profit = new Profit();
                        }
                        profit.setSellAmount(sellAmount);
                        profit.setSellPrice(sellPrice);

                    } else {
                        currency = split[7];

                        if ("".equals(split[9]) || split[9] == null || coinSet.contains(currency)||currency.contains(" ")) {
                            continue;
                        }

                        String price = split[9].replace("\n", "");
                        String amount = split[6].replace("\n", "");
                        float buyPrice = 0;
                        float buyAmount = 0;
                        try {
                            buyPrice = Float.parseFloat(price);
                            buyAmount = Float.parseFloat(amount);
                        } catch (Exception e) {
                            continue;
                        }
                        if (map.containsKey(currency)) {
                            profit = map.get(currency);
                            buyPrice += profit.getBuyPrice();
                            buyAmount += profit.getBuyAmount();
                        } else {
                            profit = new Profit();
                        }
                        profit.setBuyAmount(buyAmount);
                        profit.setBuyPrice(buyPrice);
                    }
                    profit.setCoin(currency);
                    map.put(currency, profit);

                } else if (startDate.compareTo(date) > 0) {
                    break;
                }
            }
            csvReader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return map;
    }


    public static Set<String> findCoins(String filePath, Set<String> coinSet, int start, int end) {
        Set<String> set = new HashSet<>();
        try {
            CsvReader csvReader = new CsvReader(filePath);
            int i = 0;
            while (csvReader.readRecord()) {
                if (i == 0) {
                    i++;
                    continue;
                }
                String rawRecord = csvReader.getRawRecord();
                rawRecord = rawRecord.replaceAll("\"", "");
                String[] split = rawRecord.split(",");
                String transactionType = split[2];
                if (!"Receive".equals(transactionType)) {
                    continue;
                }
                String buyCurrency = split[7];
                if (set.contains(buyCurrency) || coinSet.contains(buyCurrency)) {
                    continue;
                }
                Date date = DateUtil.StringToDate(split[0]);
                Date startDate = DateUtil.getPreviousDate(start);
                Date endDate = DateUtil.getPreviousDate(end);
                if (startDate.compareTo(date) <= 0 && endDate.compareTo(date) >= 0) {
                    set.add(buyCurrency);
                } else if (startDate.compareTo(date) > 0) {
                    break;
                }
            }
            csvReader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return set;
    }
}
