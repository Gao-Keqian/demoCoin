package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

}
