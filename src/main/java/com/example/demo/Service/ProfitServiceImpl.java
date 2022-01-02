package com.example.demo.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Dao.AddressCount;
import com.example.demo.Dao.Profit;
import com.example.demo.Mapper.ProfitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProfitServiceImpl extends ServiceImpl<ProfitMapper, Profit> implements IProfitService {

    @Autowired
    ProfitMapper profitMapper;


    @Override
    public void saveProfit(Profit profit) {
        profitMapper.insert(profit);
    }

    @Override
    public List<Profit> findAll() {
        return profitMapper.selectList(null);
    }

    @Override
    public int findAddressCount(String address) {
        QueryWrapper<Profit> wrapper = new QueryWrapper<>();
        wrapper.eq("address",address);
        return profitMapper.selectCount(wrapper);
    }

    @Override
    public List<Profit> findProfitByAddress(String address) {
        QueryWrapper<Profit> wrapper = new QueryWrapper<>();
        wrapper.eq("address",address);
        return profitMapper.selectList(wrapper);
    }

    @Override
    public void updateProfile(Profit profit) {
        profitMapper.updateById(profit);
    }

}
