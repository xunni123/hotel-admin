USE hotel_management;

-- 订单表
CREATE TABLE IF NOT EXISTS `orders` (
  `order_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
  `room_number` VARCHAR(20) DEFAULT NULL COMMENT '房间号',
  `room_id` INT(11) DEFAULT NULL COMMENT '房间ID',
  `guest_name` VARCHAR(50) DEFAULT NULL COMMENT '客人姓名',
  `guest_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `check_in_date` VARCHAR(20) DEFAULT NULL COMMENT '入住日期',
  `check_out_date` VARCHAR(20) DEFAULT NULL COMMENT '退房日期',
  `nights` INT(11) DEFAULT 0 COMMENT '入住天数',
  `total_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '订单金额',
  `payment_method` VARCHAR(20) DEFAULT NULL COMMENT '支付方式: wechat/alipay/cash/bank',
  `order_status` VARCHAR(20) DEFAULT 'completed' COMMENT '订单状态: completed/cancelled',
  `payment_status` VARCHAR(20) DEFAULT 'paid' COMMENT '支付状态: paid/unpaid',
  `book_channel` VARCHAR(50) DEFAULT NULL COMMENT '预订渠道',
  `remarks` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 插入测试数据
INSERT INTO `orders` (`order_no`, `room_number`, `room_id`, `guest_name`, `guest_phone`, `check_in_date`, `check_out_date`, `nights`, `total_amount`, `payment_method`, `order_status`, `payment_status`, `book_channel`, `remarks`) VALUES
('ORD20250501001', '101', 1, '张三', '13800138001', '2025-05-01', '2025-05-03', 2, 598.00, 'wechat', 'completed', 'paid', '前台', ''),
('ORD20250502002', '205', 2, '李四', '13800138002', '2025-05-02', '2025-05-05', 3, 888.00, 'alipay', 'completed', 'paid', '线上', ''),
('ORD20250510003', '302', 3, '王五', '13800138003', '2025-05-10', '2025-05-11', 1, 299.00, 'cash', 'completed', 'paid', '电话', ''),
('ORD20250515004', '108', 1, '赵六', '13800138004', '2025-05-15', '2025-05-18', 3, 897.00, 'bank', 'cancelled', 'unpaid', '线上', '客人取消');
