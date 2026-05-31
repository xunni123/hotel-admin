package com.xunni.hotel.web.controller;


import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.entity.Order;
import com.xunni.hotel.web.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public Result getOrderList() {
        List<Order> orders = orderService.list();
        return Result.success(orders);
    }

    @PostMapping("/add")
    public Result addOrder(@RequestBody Order order) {
        boolean success = orderService.save(order);
        return success ? Result.success(order) : Result.error("添加订单失败");
    }

    @PutMapping("/update/{orderId}")
    public Result updateOrder(@PathVariable Integer orderId, @RequestBody Order order) {
        order.setOrderId(orderId);
        boolean success = orderService.updateById(order);
        return success ? Result.success(order) : Result.error("更新订单失败");
    }
}