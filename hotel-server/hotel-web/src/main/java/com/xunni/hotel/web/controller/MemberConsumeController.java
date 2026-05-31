package com.xunni.hotel.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.entity.MemberConsume;
import com.xunni.hotel.web.mapper.MemberConsumeMapper;
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
@RequestMapping("/member-consume")
public class MemberConsumeController {

    @Autowired
    private MemberConsumeMapper memberConsumeMapper;

    @GetMapping("/list")
    public Result getConsumeList(@RequestParam(required = false) String keyword,
                                 @RequestParam(required = false) String type) {
        LambdaQueryWrapper<MemberConsume> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(MemberConsume::getMemberNo, keyword)
                    .or()
                    .like(MemberConsume::getMemberName, keyword)
                    .or()
                    .like(MemberConsume::getPhone, keyword);
        }
        if (StringUtils.hasText(type)) {
            queryWrapper.eq(MemberConsume::getType, type);
        }
        queryWrapper.orderByDesc(MemberConsume::getCreateTime);
        List<MemberConsume> list = memberConsumeMapper.selectList(queryWrapper);
        return Result.success(list);
    }

    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总消费金额
        Double totalAmount = memberConsumeMapper.selectList(null).stream()
                .mapToDouble(m -> m.getAmount().doubleValue()).sum();
        stats.put("totalAmount", totalAmount);
        
        // 总消费次数
        stats.put("totalCount", memberConsumeMapper.selectCount(null));
        
        // 房费消费金额
        LambdaQueryWrapper<MemberConsume> roomWrapper = new LambdaQueryWrapper<>();
        roomWrapper.eq(MemberConsume::getType, "room");
        Double roomAmount = memberConsumeMapper.selectList(roomWrapper).stream()
                .mapToDouble(m -> m.getAmount().doubleValue()).sum();
        stats.put("roomAmount", roomAmount);
        
        // 商品消费金额
        LambdaQueryWrapper<MemberConsume> goodsWrapper = new LambdaQueryWrapper<>();
        goodsWrapper.eq(MemberConsume::getType, "goods");
        Double goodsAmount = memberConsumeMapper.selectList(goodsWrapper).stream()
                .mapToDouble(m -> m.getAmount().doubleValue()).sum();
        stats.put("goodsAmount", goodsAmount);
        
        return Result.success(stats);
    }
}
