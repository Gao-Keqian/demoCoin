package com.example.demo.Service;

import com.example.demo.Dao.Coin;

import java.util.List;

public interface ICoinService  {
    public void saveCoin(Coin coin);

    public List<Coin> findAll();

    public Coin findCoinByShortName(String name);

}
