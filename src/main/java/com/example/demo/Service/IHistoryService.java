package com.example.demo.Service;

import com.example.demo.Dao.BuyHistory;
import com.example.demo.Dao.CoinCount;

import java.util.List;

public interface IHistoryService {
    public void saveHistory(BuyHistory buyHistory);


    public List<BuyHistory> findByAction();



}
