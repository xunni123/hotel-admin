package com.xunni.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("cleaning_tasks")
public class CleaningTask {
    @TableId(value = "task_id", type = IdType.AUTO)
    private Integer taskId;

    @TableField("cleaner_id")
    private Integer cleanerId;

    @TableField("room_id")
    private Integer roomId;

    @TableField("room_number")
    private String roomNumber;

    @TableField("task_type")
    private String taskType;

    @TableField("priority")
    private String priority;

    @TableField("status")
    private String status;

    @TableField("assigned_by")
    private String assignedBy;

    @TableField("assigned_time")
    private LocalDateTime assignedTime;

    @TableField("start_time")
    private LocalDateTime startTime;

    @TableField("completed_time")
    private LocalDateTime completedTime;

    @TableField("remark")
    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
