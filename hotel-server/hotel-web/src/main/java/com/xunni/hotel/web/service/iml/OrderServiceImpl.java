package com.xunni.hotel.web.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunni.hotel.entity.Order;
import com.xunni.hotel.web.mapper.OrderMapper;
import com.xunni.hotel.web.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
}