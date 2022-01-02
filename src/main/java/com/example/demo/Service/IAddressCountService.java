package com.example.demo.Service;

import com.example.demo.Dao.Address;
import com.example.demo.Dao.AddressCount;

import java.util.List;

public interface IAddressCountService {
    public void saveAddressCount(AddressCount addressCount);

    public List<AddressCount> findAddressByCount(int num);

    public void  updateAddressCount(AddressCount addressCount);

    public List<AddressCount> findAddressByCountAndValid(int num);

    public List<AddressCount> findAddressByHighLight();

    public AddressCount findAddress(String address);

    public AddressCount findAddressByCountAndValidAndAddress(int num, String address);
}
