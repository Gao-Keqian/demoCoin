package com.example.demo.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Dao.Coin;
import com.example.demo.Dao.CoinName;
import com.example.demo.Mapper.CoinMapper;
import com.example.demo.Mapper.CoinNameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CoinNameService extends ServiceImpl<CoinNameMapper, CoinName> implements ICoinNameService {
    @Autowired
    CoinNameMapper mapper;

    @Override
    public List<CoinName> findAll() {
        return mapper.selectList(null);
    }

    @Override
    public void saveCoinName(CoinName coinName) {
        mapper.insert(coinName);
    }


}
