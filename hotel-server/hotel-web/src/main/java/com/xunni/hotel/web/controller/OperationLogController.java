package com.xunni.hotel.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.entity.OperationLog;
import com.xunni.hotel.web.mapper.OperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/operation-log")
public class OperationLogController {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @GetMapping("/list")
    public Result getLogList(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) String module,
                             @RequestParam(required = false) String type) {
        LambdaQueryWrapper<OperationLog> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(OperationLog::getOperator, keyword)
                    .or()
                    .like(OperationLog::getContent, keyword);
        }
        if (StringUtils.hasText(module)) {
            queryWrapper.eq(OperationLog::getModule, module);
        }
        if (StringUtils.hasText(type)) {
            queryWrapper.eq(OperationLog::getType, type);
        }
        queryWrapper.orderByDesc(OperationLog::getCreateTime);
        List<OperationLog> list = operationLogMapper.selectList(queryWrapper);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result getLogById(@PathVariable Integer id) {
        OperationLog log = operationLogMapper.selectById(id);
        if (log == null) {
            return Result.error("日志不存在");
        }
        return Result.success(log);
    }
}
