package com.xunni.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("operation_log")
public class OperationLog {
    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    @TableField("type")
    private String type;

    @TableField("module")
    private String module;

    @TableField("content")
    private String content;

    @TableField("operator")
    private String operator;

    @TableField("operator_id")
    private Integer operatorId;

    @TableField("ip")
    private String ip;

    @TableField("params")
    private String params;

    @TableField("result")
    private String result;

    @TableField("create_time")
    private LocalDateTime createTime;
}
