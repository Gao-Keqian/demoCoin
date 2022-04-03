package com.example.demo.Dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@TableName("history_price")
@EqualsAndHashCode(callSuper=false)
public class HistoryPrice {
    @TableId(value = "id", type = IdType.AUTO)
    public int id;

    public String price;

    @TableField(value = "created_date")
    public Date createdDate;

    @TableField(value = "coin_name_id")
    public Integer coinNameId;
}
