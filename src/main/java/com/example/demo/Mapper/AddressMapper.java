package com.example.demo.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.Dao.Address;
import com.example.demo.Dao.Coin;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface AddressMapper extends BaseMapper<Address> {

    @Select("SELECT distinct coin_name FROM coin.address")
    public List<String> selectCoinName();

}
