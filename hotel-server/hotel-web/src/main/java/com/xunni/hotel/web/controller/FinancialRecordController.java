package com.xunni.hotel.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.entity.FinancialRecord;
import com.xunni.hotel.web.mapper.FinancialRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/financial-record")
public class FinancialRecordController {

    @Autowired
    private FinancialRecordMapper financialRecordMapper;

    @GetMapping("/list")
    public Result getRecordList(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String paymentMethod) {
        LambdaQueryWrapper<FinancialRecord> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(type)) {
            queryWrapper.eq(FinancialRecord::getType, type);
        }
        
        if (StringUtils.hasText(paymentMethod)) {
            queryWrapper.eq(FinancialRecord::getPaymentMethod, paymentMethod);
        }
        
        queryWrapper.orderByDesc(FinancialRecord::getCreateTime);
        List<FinancialRecord> list = financialRecordMapper.selectList(queryWrapper);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result getRecordById(@PathVariable Integer id) {
        FinancialRecord record = financialRecordMapper.selectById(id);
        if (record == null) {
            return Result.error("记录不存在");
        }
        return Result.success(record);
    }

    @PostMapping
    public Result addRecord(@RequestBody FinancialRecord record) {
        record.setCreateTime(LocalDateTime.now());
        int rows = financialRecordMapper.insert(record);
        return rows > 0 ? Result.success(record) : Result.error("添加失败");
    }

    @PutMapping("/{id}")
    public Result updateRecord(@PathVariable Integer id, @RequestBody FinancialRecord record) {
        FinancialRecord exist = financialRecordMapper.selectById(id);
        if (exist == null) {
            return Result.error("记录不存在");
        }
        record.setRecordId(id);
        int rows = financialRecordMapper.updateById(record);
        return rows > 0 ? Result.success(record) : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result deleteRecord(@PathVariable Integer id) {
        int rows = financialRecordMapper.deleteById(id);
        return rows > 0 ? Result.success("删除成功") : Result.error("删除失败");
    }

    @GetMapping("/summary")
    public Result getSummary() {
        Map<String, Object> summary = new HashMap<>();

        LambdaQueryWrapper<FinancialRecord> incomeWrapper = new LambdaQueryWrapper<>();
        incomeWrapper.eq(FinancialRecord::getType, "income");
        Double income = financialRecordMapper.selectList(incomeWrapper).stream()
                .mapToDouble(f -> f.getAmount().doubleValue()).sum();
        summary.put("income", income);

        LambdaQueryWrapper<FinancialRecord> expenseWrapper = new LambdaQueryWrapper<>();
        expenseWrapper.eq(FinancialRecord::getType, "expense");
        Double expense = financialRecordMapper.selectList(expenseWrapper).stream()
                .mapToDouble(f -> f.getAmount().doubleValue()).sum();
        summary.put("expense", expense);

        summary.put("profit", income - expense);

        int orderCount = financialRecordMapper.selectList(incomeWrapper).size();
        summary.put("orderCount", orderCount);

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
