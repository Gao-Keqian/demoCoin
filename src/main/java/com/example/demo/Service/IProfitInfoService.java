package com.example.demo.Service;

import com.example.demo.Dao.Profit;
import com.example.demo.Dao.ProfitInfo;

import java.util.List;

public interface IProfitInfoService {

    public void saveProfitInfo(ProfitInfo profit);

    public ProfitInfo findProfitInfoByAddress(String address);

    public void updateProfitInfo(ProfitInfo profit);

    public List<ProfitInfo> findProfitInfo(int count, double rate, double profit);

    public List<ProfitInfo> findAll();

    public List<ProfitInfo> findProfitInfoByFlag();

}
