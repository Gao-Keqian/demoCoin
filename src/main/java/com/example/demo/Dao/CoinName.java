package com.example.demo.Dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("coin_name")
@EqualsAndHashCode(callSuper=false)
public class CoinName {
    @TableId(value = "id", type = IdType.AUTO)
    public int id;

    public String name;
}
