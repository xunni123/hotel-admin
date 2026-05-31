USE hotel_management;

-- 会员消费记录表
CREATE TABLE IF NOT EXISTS `member_consume` (
  `consume_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '消费记录ID',
  `member_id` INT(11) DEFAULT NULL COMMENT '会员ID',
  `member_no` VARCHAR(50) DEFAULT NULL COMMENT '会员号',
  `member_name` VARCHAR(50) DEFAULT NULL COMMENT '会员姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `type` VARCHAR(20) DEFAULT NULL COMMENT '消费类型: room-房费, goods-商品, other-其他',
  `amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '消费金额',
  `points_change` INT(11) DEFAULT 0 COMMENT '积分变动',
  `current_points` INT(11) DEFAULT 0 COMMENT '当前积分',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `operator` VARCHAR(50) DEFAULT NULL COMMENT '操作人',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`consume_id`),
  KEY `idx_member_no` (`member_no`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员消费记录表';

-- 插入测试数据
INSERT INTO `member_consume` (`member_id`, `member_no`, `member_name`, `phone`, `type`, `amount`, `points_change`, `current_points`, `remark`, `operator`) VALUES
(1, 'M001', '张三', '13800138001', 'room', 598.00, 598, 1598, '房费消费', 'admin'),
(1, 'M001', '张三', '13800138001', 'goods', 88.00, 88, 1686, '购买饮品', 'admin'),
(2, 'M002', '李四', '13800138002', 'room', 888.00, 888, 1888, '房费消费', 'admin'),
(3, 'M003', '王五', '13800138003', 'room', 299.00, 299, 1299, '房费消费', 'admin'),
(2, 'M002', '李四', '13800138002', 'goods', 50.00, 50, 1938, '购买食品', 'admin');
