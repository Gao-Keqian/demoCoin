package com.example.demo.Service;

import com.example.demo.Dao.Coin;
import com.example.demo.Dao.Profit;

import java.util.List;

public interface IProfitService {
    public void saveProfit(Profit profit);

    public List<Profit> findAll();

    public int findAddressCount(String address);

    public List<Profit> findProfitByAddress(String address);

    public void updateProfile(Profit profit);

}
