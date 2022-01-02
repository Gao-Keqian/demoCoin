package com.example.demo.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Dao.Address;
import com.example.demo.Dao.AddressCount;
import com.example.demo.Mapper.AddressCountMapper;
import com.example.demo.Mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AddressCountService extends ServiceImpl<AddressCountMapper, AddressCount> implements IAddressCountService {
    @Autowired
    AddressCountMapper mapper;


    @Override
    public void saveAddressCount(AddressCount addressCount) {
        mapper.insert(addressCount);
    }

    @Override
    public List<AddressCount> findAddressByCount(int num) {
        QueryWrapper<AddressCount> wrapper = new QueryWrapper<>();
        wrapper.ge("count",num);
        return mapper.selectList(wrapper);
    }

    @Override
    public void updateAddressCount(AddressCount addressCount) {
        mapper.updateById(addressCount);
    }


    @Override
    public List<AddressCount> findAddressByCountAndValid(int num) {
        QueryWrapper<AddressCount> wrapper = new QueryWrapper<>();
        wrapper.ge("count",num);
        wrapper.eq("valid","T");
        return mapper.selectList(wrapper);
    }

    @Override
    public AddressCount  findAddressByCountAndValidAndAddress(int num, String address) {
        QueryWrapper<AddressCount> wrapper = new QueryWrapper<>();
        wrapper.ge("count",num);
        wrapper.eq("address",address);
        wrapper.eq("valid","T");
        return mapper.selectOne(wrapper);
    }


    @Override
    public List<AddressCount> findAddressByHighLight() {
        QueryWrapper<AddressCount> wrapper = new QueryWrapper<>();
        wrapper.eq("high_light","TD");
        return mapper.selectList(wrapper);
    }


    @Override
    public AddressCount findAddress(String address) {
        QueryWrapper<AddressCount> wrapper = new QueryWrapper<>();
        wrapper.eq("address",address);
        return mapper.selectOne(wrapper);
    }
}
