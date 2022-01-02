package com.example.demo.Service;

import com.example.demo.Dao.AddressCount;
import com.example.demo.Dao.AddressCount2;

import java.util.List;

public interface IAddressCount2Service {
    public void saveAddressCount(AddressCount2 addressCount);

    public List<AddressCount2> findAddressByCount(int num);

    public void  updateAddressCount(AddressCount2 addressCount);

    public List<AddressCount2> findAddressByCountAndValid(int num);

    public List<AddressCount2> findAddressByHighLight();

    public AddressCount2 findAddress(String address);
}
