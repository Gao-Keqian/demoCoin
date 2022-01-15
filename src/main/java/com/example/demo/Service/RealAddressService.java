package com.example.demo.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Dao.AddressCount;
import com.example.demo.Dao.CoinName;
import com.example.demo.Dao.RealAddress;
import com.example.demo.Mapper.CoinNameMapper;
import com.example.demo.Mapper.RealAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class RealAddressService extends ServiceImpl<RealAddressMapper, RealAddress> implements IRealAddressService {
    @Autowired
    RealAddressMapper mapper;

    @Override
    public List<RealAddress> findAll() {
        return mapper.selectList(null);
    }

    @Override
    public RealAddress findAddress(String address) {
        QueryWrapper<RealAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("address",address);
        return mapper.selectOne(wrapper);
    }

    @Override
    public void saveAddress(RealAddress realAddress) {
        mapper.insert(realAddress);
    }


}
