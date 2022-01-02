package com.example.demo.Service;

import com.example.demo.Dao.Address;
import com.example.demo.Dao.Coin;

import java.util.List;

public interface IAddressService {
    public void saveAddress(Address address);

    public List<Address> findAll();
}
