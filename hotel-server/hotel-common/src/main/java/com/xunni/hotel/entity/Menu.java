package com.xunni.hotel.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("menus")
public class Menu {
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menu_id;
    private Long parent_id;
    private String menu_name;
    private String menu_key;
    private String menu_type;
    private String path;
    private String component;
    private String icon;
    private Long sort_order;
    private String status;
    private Date create_time;

    @TableField(exist = false)
    private List<Menu> children;

}
