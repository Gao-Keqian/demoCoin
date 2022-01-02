package com.example.demo.Dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.format.DecimalStyle;

@Data
@TableName("profit")
@EqualsAndHashCode(callSuper=false)
public class Profit {
    @TableId(value = "id", type = IdType.AUTO)
    public int id;

    public String address;

    public String coin;

    @TableField(value = "buy_amount")
    public float buyAmount;

    @TableField(value = "buy_price")
    public float buyPrice;

    @TableField(value = "sell_amount")
    public float sellAmount;

    @TableField(value = "sell_price")
    public float sellPrice;

    @TableField(value = "total_profit")
    public String totalProfit;

}
