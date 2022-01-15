package com.example.demo.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.Dao.CoinName;
import com.example.demo.Dao.RealAddress;

import java.util.List;

public interface IRealAddressService {


    public List<RealAddress> findAll();


    public RealAddress findAddress(String address);

    public void saveAddress(RealAddress realAddress);

}
