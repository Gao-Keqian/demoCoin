package com.example.demo.Dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@TableName("history")
@EqualsAndHashCode(callSuper=false)
public class BuyHistory {

    @TableId(value = "id", type = IdType.AUTO)
    public int id;
    public String coin;

    public String address;

    public String action;

    public String num;

    @TableField(value = "buy_date")
    public Date buyDate;


}
