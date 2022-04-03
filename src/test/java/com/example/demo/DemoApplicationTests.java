package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void test(){
        BigDecimal increaseRate = new BigDecimal("46727.226837793").divide(new BigDecimal("576976.65613189"), 10, RoundingMode.HALF_UP);
        System.out.println(increaseRate);
    }

    public static Date interceptTime(String timeStr) throws ParseException {

        String strDate = timeStr.substring(0, 10);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.parse(strDate);
    }

    public static void main(String[] args) throws ParseException {
        interceptTime("2021-04-02T00:00:00.000Z");
    }

}
