package com.example.demo.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Dao.AddressCount;
import com.example.demo.Dao.AddressCount2;
import com.example.demo.Mapper.AddressCount2Mapper;
import com.example.demo.Mapper.AddressCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AddressCount2Service extends ServiceImpl<AddressCount2Mapper, AddressCount2> implements IAddressCount2Service {
    @Autowired
    AddressCount2Mapper mapper;


    @Override
    public void saveAddressCount(AddressCount2 addressCount) {
        mapper.insert(addressCount);
    }

    @Override
    public List<AddressCount2> findAddressByCount(int num) {
        QueryWrapper<AddressCount2> wrapper = new QueryWrapper<>();
        wrapper.ge("count",num);
        return mapper.selectList(wrapper);
    }

    @Override
    public void updateAddressCount(AddressCount2 addressCount) {
        mapper.updateById(addressCount);
    }


    @Override
    public List<AddressCount2> findAddressByCountAndValid(int num) {
        QueryWrapper<AddressCount2> wrapper = new QueryWrapper<>();
        wrapper.ge("count",num);
        wrapper.eq("valid","T");
        return mapper.selectList(wrapper);
    }

    @Override
    public List<AddressCount2> findAddressByHighLight() {
        QueryWrapper<AddressCount2> wrapper = new QueryWrapper<>();
        wrapper.eq("high_light","TD");
        return mapper.selectList(wrapper);
    }

    @Override
    public AddressCount2 findAddress(String address) {
        QueryWrapper<AddressCount2> wrapper = new QueryWrapper<>();
        wrapper.eq("address",address);
        return mapper.selectOne(wrapper);
    }
}
