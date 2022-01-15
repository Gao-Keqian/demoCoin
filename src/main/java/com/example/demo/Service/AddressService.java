package com.example.demo.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Dao.Address;
import com.example.demo.Dao.Coin;
import com.example.demo.Mapper.AddressMapper;
import com.example.demo.Mapper.CoinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AddressService extends ServiceImpl<AddressMapper, Address> implements IAddressService {
    @Autowired
    AddressMapper mapper;

    @Override
    public void saveAddress(Address address) {
        mapper.insert(address);
    }

    @Override
    public List<Address> findAll() {
        return mapper.selectList(null);
    }

    @Override
    public List<String> selectCoinName() {
        return mapper.selectCoinName();
    }



}
