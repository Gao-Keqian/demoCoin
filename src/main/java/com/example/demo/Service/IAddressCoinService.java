package com.example.demo.Service;

import com.example.demo.Dao.AddressCoin;
import com.example.demo.Dao.Coin;

import java.util.List;

public interface IAddressCoinService {
    public void saveCoin(AddressCoin coin);

    public List<AddressCoin> findAll();

    public Integer findByAddress(String address);
}
