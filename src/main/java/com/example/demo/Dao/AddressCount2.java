package com.example.demo.Dao;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("address_count2")
@EqualsAndHashCode(callSuper=false)
public class AddressCount2 {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    @TableField(value = "address")
    private String address;

    @TableField(value = "count")
    private int count;

    private String valid;

    @TableField(value = "high_light")
    private String highLight;



}
