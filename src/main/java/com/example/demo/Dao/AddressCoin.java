package com.example.demo.Dao;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("address_coin")
@EqualsAndHashCode(callSuper=false)
public class AddressCoin {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    @TableField(value = "address")
    private String address;

    @TableField(value = "coin")
    private String coin;
}
