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
    public void findMissedFiles() throws IOException, InterruptedException {
        Set<String> readfiles = FileReaderUtil.readfileName("F:\\csv folder");
        System.out.println(readfiles.size());
        List<AddressCount> addressCounts = addressCountService.findAddressByCountAndValid(6);

        for (AddressCount address : addressCounts) {
            if (readfiles.contains(address.getAddress())) {
                continue;
            }
            DownloadSpider.download(address.getAddress());
        }
    }



}
