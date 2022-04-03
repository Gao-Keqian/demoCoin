package com.example.demo.Dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("profit_info")
@EqualsAndHashCode(callSuper=false)
public class ProfitInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    private String address;

    private Double profit;

    @TableField(value = "profit_rate")
    private Double profitRate;

    private int count;

    private String flag;
}
