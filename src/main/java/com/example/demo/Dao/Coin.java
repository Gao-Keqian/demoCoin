package com.example.demo.Dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("coin")
@EqualsAndHashCode(callSuper=false)
public class Coin {
    @TableId(value = "id", type = IdType.AUTO)
    public int id;

    public String name;

    @TableField(value = "short_name")
    public String shortName;

    public String price;

    @TableField(value = "cmc_rank")
    public String cmcRank;

    @TableField(value = "save_date")
    public String saveDate;

    public Coin(int id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
    }

    public Coin() {
    }
}
