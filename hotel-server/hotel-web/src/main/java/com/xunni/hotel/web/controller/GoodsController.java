package com.xunni.hotel.web.controller;

import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.entity.Goods;
import com.xunni.hotel.web.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/list")
    public Result getGoodsList(@RequestParam(required = false) String keyword,
                                @RequestParam(required = false) String category) {
        List<Goods> goodsList = goodsService.lambdaQuery()
                .like(StringUtils.hasText(keyword), Goods::getGoodsName, keyword)
                .or()
                .like(StringUtils.hasText(keyword), Goods::getGoodsCode, keyword)
                .eq(StringUtils.hasText(category), Goods::getCategory, category)
                .orderByDesc(Goods::getCreateTime)
                .list();
        return Result.success(goodsList);
    }

    @GetMapping("/{id}")
    public Result getGoodsById(@PathVariable Integer id) {
        Goods goods = goodsService.getById(id);
        if (goods == null) {
            return Result.error("商品不存在");
        }
        return Result.success(goods);
    }

    @PostMapping
    public Result addGoods(@RequestBody Map<String, Object> params) {
        String code = (String) params.get("code");
        String name = (String) params.get("name");
        String category = (String) params.get("category");
        Object priceObj = params.get("price");
        Object stockObj = params.get("stock");
        String description = (String) params.get("description");
        String status = (String) params.get("status");

        if (!StringUtils.hasText(code) || !StringUtils.hasText(name)) {
            return Result.error("商品编码和名称不能为空");
        }

        Goods goods = new Goods();
        goods.setGoodsCode(code);
        goods.setGoodsName(name);
        goods.setCategory(category);
        
        if (priceObj != null) {
            if (priceObj instanceof Number) {
                goods.setPrice(new java.math.BigDecimal(priceObj.toString()));
            }
        }
        
        if (stockObj != null && stockObj instanceof Number) {
            goods.setStock(((Number) stockObj).intValue());
        }
        
        goods.setDescription(description);
        goods.setStatus(status != null ? status : "on");
        goods.setCreateTime(LocalDateTime.now());
        goods.setUpdateTime(LocalDateTime.now());

        boolean success = goodsService.save(goods);
        return success ? Result.success(goods) : Result.error("添加失败");
    }

    @PutMapping("/{id}")
    public Result updateGoods(@PathVariable Integer id, @RequestBody Map<String, Object> params) {
        Goods goods = goodsService.getById(id);
        if (goods == null) {
            return Result.error("商品不存在");
        }

        if (params.containsKey("code")) {
            goods.setGoodsCode((String) params.get("code"));
        }
        if (params.containsKey("name")) {
            goods.setGoodsName((String) params.get("name"));
        }
        if (params.containsKey("category")) {
            goods.setCategory((String) params.get("category"));
        }
        if (params.containsKey("price")) {
            Object priceObj = params.get("price");
            if (priceObj != null && priceObj instanceof Number) {
                goods.setPrice(new java.math.BigDecimal(priceObj.toString()));
            }
        }
        if (params.containsKey("stock")) {
            Object stockObj = params.get("stock");
            if (stockObj != null && stockObj instanceof Number) {
                goods.setStock(((Number) stockObj).intValue());
            }
        }
        if (params.containsKey("description")) {
            goods.setDescription((String) params.get("description"));
        }
        if (params.containsKey("status")) {
            goods.setStatus((String) params.get("status"));
        }
        
        goods.setUpdateTime(LocalDateTime.now());

        boolean success = goodsService.updateById(goods);
        return success ? Result.success(goods) : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result deleteGoods(@PathVariable Integer id) {
        boolean success = goodsService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    @PutMapping("/toggleStatus/{id}")
    public Result toggleStatus(@PathVariable Integer id) {
        Goods goods = goodsService.getById(id);
        if (goods == null) {
            return Result.error("商品不存在");
        }

        goods.setStatus("on".equals(goods.getStatus()) ? "off" : "on");
        goods.setUpdateTime(LocalDateTime.now());

        boolean success = goodsService.updateById(goods);
        return success ? Result.success(goods) : Result.error("操作失败");
    }
}
