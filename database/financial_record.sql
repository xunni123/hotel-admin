USE hotel_management;

-- 财务记录表
CREATE TABLE IF NOT EXISTS `financial_record` (
  `record_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `type` VARCHAR(20) NOT NULL COMMENT '类型: income-收入, expense-支出',
  `amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  `payment_method` VARCHAR(20) DEFAULT NULL COMMENT '支付方式: wechat/alipay/cash/bank',
  `order_no` VARCHAR(50) DEFAULT NULL COMMENT '关联订单号',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `operator` VARCHAR(50) DEFAULT NULL COMMENT '操作人',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`record_id`),
  KEY `idx_type` (`type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务记录表';

-- 插入测试数据
INSERT INTO `financial_record` (`type`, `amount`, `payment_method`, `order_no`, `remark`, `operator`) VALUES
('income', 598.00, 'wechat', 'ORD20250501001', '房费收入', 'admin'),
('income', 888.00, 'alipay', 'ORD20250502002', '房费收入', 'admin'),
('income', 299.00, 'cash', 'ORD20250510003', '房费收入', 'admin'),
('expense', 200.00, 'cash', NULL, '购买清洁用品', 'admin'),
('expense', 150.00, 'wechat', NULL, '日常维护费', 'admin');
