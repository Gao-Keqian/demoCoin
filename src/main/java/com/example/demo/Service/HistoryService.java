package com.example.demo.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Dao.AddressCount;
import com.example.demo.Dao.BuyHistory;
import com.example.demo.Dao.CoinCount;
import com.example.demo.Mapper.CoinCountMapper;
import com.example.demo.Mapper.HistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class HistoryService extends ServiceImpl<HistoryMapper, BuyHistory> implements IHistoryService {
    @Autowired
    HistoryMapper historyMapper;


    @Override
    public void saveHistory(BuyHistory buyHistory) {
        historyMapper.insert(buyHistory);
    }

    @Override
    public List<BuyHistory> findByAction() {
        QueryWrapper<BuyHistory> wrapper = new QueryWrapper<>();
        wrapper.ge("action","buy");
        return historyMapper.selectList(wrapper);
    }
}
