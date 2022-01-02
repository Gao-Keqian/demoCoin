package com.example.demo.Dao;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("address")
@EqualsAndHashCode(callSuper=false)
public class Address {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    @TableField(value = "address")
    private String address;

    @TableField(value = "coin_name")
    private String coinName;

    @TableField(value = "older_amount")
    private String olderAmount;

    @TableField(value = "new_amount")
    private String newAmount;

    @TableField(value = "change_amount")
    private String changeAmount;
}
