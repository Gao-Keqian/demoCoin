package com.example.demo.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Dao.AddressCount;
import com.example.demo.Dao.CoinCount;
import com.example.demo.Mapper.AddressCountMapper;
import com.example.demo.Mapper.CoinCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CoinCountService extends ServiceImpl<CoinCountMapper, CoinCount> implements ICoinCountService {
    @Autowired
    CoinCountMapper coinCountMapper;

    @Override
    public void saveAddressCount(CoinCount coinCount) {
        coinCountMapper.insert(coinCount);
    }


}
