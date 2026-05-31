package com.xunni.hotel.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.entity.MemberConsume;
import com.xunni.hotel.web.mapper.MemberConsumeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
            queryWrapper.and(w -> w
                    .like(MemberConsume::getMemberNo, keyword)
                    .or()
                    .like(MemberConsume::getMemberName, keyword)
                    .or()
                    .like(MemberConsume::getPhone, keyword));
        }
        if (StringUtils.hasText(type)) {
            queryWrapper.eq(MemberConsume::getType, type);
        }
        queryWrapper.orderByDesc(MemberConsume::getCreateTime);
        List<MemberConsume> list = memberConsumeMapper.selectList(queryWrapper);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result getConsumeById(@PathVariable Integer id) {
        MemberConsume consume = memberConsumeMapper.selectById(id);
        if (consume == null) {
            return Result.error("记录不存在");
        }
        return Result.success(consume);
    }

    @PostMapping
    public Result addConsume(@RequestBody MemberConsume consume) {
        consume.setCreateTime(LocalDateTime.now());
        int rows = memberConsumeMapper.insert(consume);
        return rows > 0 ? Result.success(consume) : Result.error("添加失败");
    }

    @PutMapping("/{id}")
    public Result updateConsume(@PathVariable Integer id, @RequestBody MemberConsume consume) {
        MemberConsume exist = memberConsumeMapper.selectById(id);
        if (exist == null) {
            return Result.error("记录不存在");
        }
        consume.setConsumeId(id);
        int rows = memberConsumeMapper.updateById(consume);
        return rows > 0 ? Result.success(consume) : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result deleteConsume(@PathVariable Integer id) {
        int rows = memberConsumeMapper.deleteById(id);
        return rows > 0 ? Result.success("删除成功") : Result.error("删除失败");
    }

    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> stats = new HashMap<>();

        Double totalAmount = memberConsumeMapper.selectList(null).stream()
                .mapToDouble(m -> m.getAmount().doubleValue()).sum();
        stats.put("totalAmount", totalAmount);

        stats.put("totalCount", memberConsumeMapper.selectCount(null));

        LambdaQueryWrapper<MemberConsume> roomWrapper = new LambdaQueryWrapper<>();
        roomWrapper.eq(MemberConsume::getType, "room");
        Double roomAmount = memberConsumeMapper.selectList(roomWrapper).stream()
                .mapToDouble(m -> m.getAmount().doubleValue()).sum();
        stats.put("roomAmount", roomAmount);

        LambdaQueryWrapper<MemberConsume> goodsWrapper = new LambdaQueryWrapper<>();
        goodsWrapper.eq(MemberConsume::getType, "goods");
        Double goodsAmount = memberConsumeMapper.selectList(goodsWrapper).stream()
                .mapToDouble(m -> m.getAmount().doubleValue()).sum();
        stats.put("goodsAmount", goodsAmount);

        return Result.success(stats);
    }
}
