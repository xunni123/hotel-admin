package com.xunni.hotel.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.entity.FinancialRecord;
import com.xunni.hotel.web.mapper.FinancialRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/financial-record")
public class FinancialRecordController {

    @Autowired
    private FinancialRecordMapper financialRecordMapper;

    @GetMapping("/list")
    public Result getRecordList(@RequestParam(required = false) String type) {
        LambdaQueryWrapper<FinancialRecord> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(type)) {
            queryWrapper.eq(FinancialRecord::getType, type);
        }
        queryWrapper.orderByDesc(FinancialRecord::getCreateTime);
        List<FinancialRecord> list = financialRecordMapper.selectList(queryWrapper);
        return Result.success(list);
    }

    @GetMapping("/summary")
    public Result getSummary() {
        Map<String, Object> summary = new HashMap<>();
        
        // 总收入
        LambdaQueryWrapper<FinancialRecord> incomeWrapper = new LambdaQueryWrapper<>();
        incomeWrapper.eq(FinancialRecord::getType, "income");
        Double income = financialRecordMapper.selectList(incomeWrapper).stream()
                .mapToDouble(f -> f.getAmount().doubleValue()).sum();
        summary.put("income", income);
        
        // 总支出
        LambdaQueryWrapper<FinancialRecord> expenseWrapper = new LambdaQueryWrapper<>();
        expenseWrapper.eq(FinancialRecord::getType, "expense");
        Double expense = financialRecordMapper.selectList(expenseWrapper).stream()
                .mapToDouble(f -> f.getAmount().doubleValue()).sum();
        summary.put("expense", expense);
        
        // 净利润
        summary.put("profit", income - expense);
        
        // 订单数
        int orderCount = financialRecordMapper.selectList(incomeWrapper).size();
        summary.put("orderCount", orderCount);
        
        // 按支付方式统计
        Map<String, Double> paymentStats = new HashMap<>();
        List<FinancialRecord> records = financialRecordMapper.selectList(null);
        records.forEach(record -> {
            String method = record.getPaymentMethod();
            paymentStats.merge(method, record.getAmount().doubleValue(), Double::sum);
        });
        summary.put("paymentStats", paymentStats);
        
        return Result.success(summary);
    }
}
