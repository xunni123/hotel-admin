-- 保洁员表
CREATE TABLE IF NOT EXISTS `cleaners` (
  `cleaner_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '保洁员ID',
  `cleaner_name` VARCHAR(50) NOT NULL COMMENT '保洁员姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `status` VARCHAR(20) DEFAULT 'available' COMMENT '状态: available-空闲, busy-忙碌, offline-离线',
  `join_date` DATE DEFAULT NULL COMMENT '入职日期',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`cleaner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='保洁员表';

-- 清洁任务表
CREATE TABLE IF NOT EXISTS `cleaning_tasks` (
  `task_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `cleaner_id` INT(11) DEFAULT NULL COMMENT '保洁员ID',
  `room_id` INT(11) DEFAULT NULL COMMENT '房间ID',
  `room_number` VARCHAR(20) DEFAULT NULL COMMENT '房间号',
  `task_type` VARCHAR(20) DEFAULT 'cleaning' COMMENT '任务类型: cleaning-打扫, inspection-检查',
  `priority` VARCHAR(20) DEFAULT 'normal' COMMENT '优先级: urgent-紧急, normal-普通, low-低',
  `status` VARCHAR(20) DEFAULT 'pending' COMMENT '状态: pending-待处理, in_progress-进行中, completed-已完成',
  `assigned_by` VARCHAR(50) DEFAULT NULL COMMENT '派单人',
  `assigned_time` DATETIME DEFAULT NULL COMMENT '派单时间',
  `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
  `completed_time` DATETIME DEFAULT NULL COMMENT '完成时间',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`task_id`),
  KEY `idx_cleaner_id` (`cleaner_id`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='清洁任务表';

-- 插入保洁员测试数据
INSERT INTO `cleaners` (`cleaner_name`, `phone`, `status`, `join_date`, `remark`) VALUES
('张秀英', '13800138001', 'available', '2023-01-15', '资深保洁员'),
('李桂芳', '13800138002', 'busy', '2023-03-20', '手脚麻利'),
('王丽华', '13800138003', 'available', '2023-06-01', '新入职'),
('赵红梅', '13800138004', 'offline', '2023-02-10', '请假中'),
('陈美玲', '13800138005', 'available', '2023-04-15', 'VIP房专属');

-- 插入清洁任务测试数据
INSERT INTO `cleaning_tasks` (`cleaner_id`, `room_number`, `task_type`, `priority`, `status`, `assigned_by`, `assigned_time`) VALUES
(1, '101', 'cleaning', 'normal', 'pending', 'admin', NOW()),
(1, '102', 'cleaning', 'urgent', 'in_progress', 'admin', NOW()),
(2, '201', 'cleaning', 'normal', 'pending', 'admin', NOW()),
(3, '301', 'inspection', 'low', 'completed', 'admin', NOW()),
(5, '501', 'cleaning', 'urgent', 'in_progress', 'admin', NOW());
