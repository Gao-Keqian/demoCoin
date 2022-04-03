package com.example.demo.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Dao.Profit;
import com.example.demo.Dao.ProfitInfo;
import com.example.demo.Mapper.ProfitInfoMapper;
import com.example.demo.Mapper.ProfitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProfitInfoServiceImpl extends ServiceImpl<ProfitInfoMapper, ProfitInfo> implements IProfitInfoService {

    @Autowired
    ProfitInfoMapper profitInfoMapper;


    @Override
    public void saveProfitInfo(ProfitInfo profit) {
        profitInfoMapper.insert(profit);
    }

    @Override
    public ProfitInfo findProfitInfoByAddress(String address) {
            QueryWrapper<ProfitInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("address",address);
            return profitInfoMapper.selectOne(wrapper);
    }


    @Override
    public void updateProfitInfo(ProfitInfo profit) {
        profitInfoMapper.updateById(profit);
    }

    @Override
    public List<ProfitInfo> findProfitInfo(int count, double rate, double profit) {
        QueryWrapper<ProfitInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("count",count);
        queryWrapper.and(wrapper->wrapper.ge("profit_rate",rate).or().ge("profit",profit));


        return profitInfoMapper.selectList(queryWrapper);
    }

    @Override
    public List<ProfitInfo> findAll() {
        return profitInfoMapper.selectList(null);
    }

    @Override
    public List<ProfitInfo> findProfitInfoByFlag() {
        QueryWrapper<ProfitInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("flag","T");
        return profitInfoMapper.selectList(queryWrapper);
    }
}
