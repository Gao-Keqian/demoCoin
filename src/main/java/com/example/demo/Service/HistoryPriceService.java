package com.example.demo.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Dao.BuyHistory;
import com.example.demo.Dao.HistoryPrice;
import com.example.demo.Mapper.HistoryMapper;
import com.example.demo.Mapper.HistoryPriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class HistoryPriceService extends ServiceImpl<HistoryPriceMapper, HistoryPrice> implements IHistoryPriceService {

    @Autowired
    HistoryPriceMapper historyPriceMapper;

    @Override
    public void saveHistory(HistoryPrice historyPrice) {
        historyPriceMapper.insert(historyPrice);
    }
}
