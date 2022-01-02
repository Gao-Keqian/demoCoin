package com.example.demo.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Dao.BuyHistory;
import com.example.demo.Dao.Coin;
import com.example.demo.Mapper.CoinMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CoinService extends ServiceImpl<CoinMapper, Coin> implements ICoinService {
    @Autowired
    CoinMapper mapper;

    @Override
    public void saveCoin(Coin coin) {
        mapper.insert(coin);
    }


    @Override
    public List<Coin> findAll() {
        return mapper.selectList(null);
    }

    @Override
    public Coin findCoinByShortName(String name) {
        QueryWrapper<Coin> wrapper = new QueryWrapper<>();
        wrapper.eq("short_name",name);

        Integer count = mapper.selectCount(wrapper);
        if(count>1){
            return mapper.selectList(wrapper).get(0);
        }else if(count==0) {
            return null;
        }
        return mapper.selectOne(wrapper);
    }
}
