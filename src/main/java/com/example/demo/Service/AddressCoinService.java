package com.example.demo.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Dao.AddressCoin;
import com.example.demo.Dao.AddressCount;
import com.example.demo.Dao.Coin;
import com.example.demo.Mapper.AddressCoinMapper;
import com.example.demo.Mapper.CoinMapper;
import okhttp3.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AddressCoinService extends ServiceImpl<AddressCoinMapper, AddressCoin> implements IAddressCoinService {
    @Autowired
    AddressCoinMapper mapper;

    @Override
    public void saveCoin(AddressCoin coin) {
        mapper.insert(coin);
    }

    @Override
    public List<AddressCoin> findAll() {
        return mapper.selectList(null);
    }

    @Override
    public Integer findByAddress(String address) {
        QueryWrapper<AddressCoin> wrapper = new QueryWrapper<>();
        wrapper.ge("address",address);
        return mapper.selectCount(wrapper);
    }
}
