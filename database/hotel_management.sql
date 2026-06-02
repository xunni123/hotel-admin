/*
 Navicat Premium Data Transfer

 Source Server         : nihao
 Source Server Type    : MySQL
 Source Server Version : 50743 (5.7.43-log)
 Source Host           : localhost:3306
 Source Schema         : hotel_management

 Target Server Type    : MySQL
 Target Server Version : 50743 (5.7.43-log)
 File Encoding         : 65001

 Date: 01/06/2026 14:19:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for announcements
-- ----------------------------
DROP TABLE IF EXISTS `announcements`;
CREATE TABLE `announcements`  (
  `announcement_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告内容',
  `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作者',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'active' COMMENT '状态 active/inactive',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`announcement_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of announcements
-- ----------------------------
INSERT INTO `announcements` VALUES (1, '系统上线通知', '酒店管理系统正式上线！欢迎使用！', '系统管理员', 'active', '2026-05-31 12:53:06', '2026-05-31 12:53:06');
INSERT INTO `announcements` VALUES (2, '保洁工作规范', '请各位保洁员按照最新规范进行清洁工作', '行政部', 'active', '2026-05-31 12:53:06', '2026-05-31 12:53:06');
INSERT INTO `announcements` VALUES (3, '关于严格执行[客房/前台/餐饮]部最新操作规范的通知', '致全体[部门名称]员工：\n为进一步提升服务质量，加强日常管理，现发布 《[具体制度名称，如：夜间客房巡查流程]》 。请各位同事自[日期]起严格执行。\n重点要求列项如下：\n\n仪容仪表： 上班必须佩戴工牌，着装整洁。\n\n交接班： 完善《交接记录本》，未完成事项必须口头+文字双重交接。\n\n时效性： 接到客房服务需求，必须在5分钟内响应。\n违规处理： 质检小组将每日抽查，违规者按《员工手册》第X条处理。\n\n发布部门： 酒店行政办\n日期： 2026年5月31日', 'admin', 'active', '2026-05-31 13:05:41', '2026-05-31 13:05:41');
INSERT INTO `announcements` VALUES (4, '酒店内部通知（放置于客房/电梯）', '尊敬的宾客：\n为了给您提供更安全舒适的居住环境，本酒店将于 202X年X月X日（周一）14:00-16:00 进行以下维护工作：\n\n消防系统检测（届时会有短暂警报声，请勿惊慌）；\n\n大堂地板打蜡（请移步至侧门通行）。', 'admin', 'active', '2026-05-31 13:18:12', '2026-05-31 13:18:12');

-- ----------------------------
-- Table structure for building
-- ----------------------------
DROP TABLE IF EXISTS `building`;
CREATE TABLE `building`  (
  `building_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '楼栋ID',
  `building_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '楼栋名称',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`building_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '楼栋表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of building
-- ----------------------------
INSERT INTO `building` VALUES (1, 'A栋', '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `building` VALUES (2, 'B栋', '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `building` VALUES (3, 'C栋', '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `building` VALUES (4, 'D栋', '2026-05-06 23:03:17', '2026-05-06 23:03:17');

-- ----------------------------
-- Table structure for channel
-- ----------------------------
DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel`  (
  `channel_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '娓犻亾ID',
  `channel_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '娓犻亾浠ｇ爜',
  `channel_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '娓犻亾鍚嶇О',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '娓犻亾鎻忚堪',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '鎺掑簭鍙',
  PRIMARY KEY (`channel_id`) USING BTREE,
  UNIQUE INDEX `channel_code`(`channel_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '娓犻亾瀛楀吀琛' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of channel
-- ----------------------------
INSERT INTO `channel` VALUES (1, 'official', '官网', '官方网站预订', 0);
INSERT INTO `channel` VALUES (2, 'ctrip', '携程', '携程旅行', 0);
INSERT INTO `channel` VALUES (3, 'meituan', '美团', '美团酒店', 0);
INSERT INTO `channel` VALUES (4, 'phone', '电话', '电话预订', 0);
INSERT INTO `channel` VALUES (5, 'walkin', '上门', '前台直接入住', 0);
INSERT INTO `channel` VALUES (6, 'elong', '艺龙', '艺龙旅行', 0);

-- ----------------------------
-- Table structure for check_in_type
-- ----------------------------
DROP TABLE IF EXISTS `check_in_type`;
CREATE TABLE `check_in_type`  (
  `type_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '绫诲瀷ID',
  `type_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '绫诲瀷浠ｇ爜',
  `type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '绫诲瀷鍚嶇О',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '绫诲瀷鎻忚堪',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '鎺掑簭鍙',
  PRIMARY KEY (`type_id`) USING BTREE,
  UNIQUE INDEX `type_code`(`type_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '鍏ヤ綇绫诲瀷瀛楀吀琛' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of check_in_type
-- ----------------------------
INSERT INTO `check_in_type` VALUES (1, 'individual', '散客', '个人入住', 0);
INSERT INTO `check_in_type` VALUES (2, 'group', '团体', '团队入住', 0);
INSERT INTO `check_in_type` VALUES (3, 'member', '会员', '会员入住', 0);
INSERT INTO `check_in_type` VALUES (4, 'vip', 'VIP', 'VIP客户', 0);
INSERT INTO `check_in_type` VALUES (5, 'corporate', '企业', '企业协议客户', 0);

-- ----------------------------
-- Table structure for cleaners
-- ----------------------------
DROP TABLE IF EXISTS `cleaners`;
CREATE TABLE `cleaners`  (
  `cleaner_id` int(11) NOT NULL AUTO_INCREMENT,
  `cleaner_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'available',
  `join_date` date NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`cleaner_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cleaners
-- ----------------------------
INSERT INTO `cleaners` VALUES (1, 'Zhang Xiuying', '13800138001', NULL, 'available', '2023-01-15', 'Senior cleaner', '2026-05-30 20:52:28', '2026-05-30 20:52:28');
INSERT INTO `cleaners` VALUES (2, 'Li Guifang', '13800138002', NULL, 'busy', '2023-03-20', 'Fast worker', '2026-05-30 20:52:28', '2026-05-30 20:52:28');
INSERT INTO `cleaners` VALUES (3, 'Wang Lihua', '13800138003', NULL, 'available', '2023-06-01', 'New employee', '2026-05-30 20:52:28', '2026-05-30 20:52:28');
INSERT INTO `cleaners` VALUES (4, 'Zhao Hongmei', '13800138004', NULL, 'offline', '2023-02-10', 'On leave', '2026-05-30 20:52:28', '2026-05-30 20:52:28');
INSERT INTO `cleaners` VALUES (5, 'Chen Meiling', '13800138005', NULL, 'available', '2023-04-15', 'VIP room specialist', '2026-05-30 20:52:28', '2026-05-30 20:52:28');

-- ----------------------------
-- Table structure for cleaning_tasks
-- ----------------------------
DROP TABLE IF EXISTS `cleaning_tasks`;
CREATE TABLE `cleaning_tasks`  (
  `task_id` int(11) NOT NULL AUTO_INCREMENT,
  `cleaner_id` int(11) NULL DEFAULT NULL,
  `room_id` int(11) NULL DEFAULT NULL,
  `room_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `task_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'cleaning',
  `priority` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'normal',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'pending',
  `assigned_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `assigned_time` datetime NULL DEFAULT NULL,
  `start_time` datetime NULL DEFAULT NULL,
  `completed_time` datetime NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`task_id`) USING BTREE,
  INDEX `idx_cleaner_id`(`cleaner_id`) USING BTREE,
  INDEX `idx_room_id`(`room_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cleaning_tasks
-- ----------------------------
INSERT INTO `cleaning_tasks` VALUES (1, 1, NULL, '101', 'cleaning', 'normal', 'pending', 'admin', '2026-05-30 20:52:28', NULL, NULL, NULL, '2026-05-30 20:52:28', '2026-05-30 20:52:28');
INSERT INTO `cleaning_tasks` VALUES (2, 1, NULL, '102', 'cleaning', 'urgent', 'in_progress', 'admin', '2026-05-30 20:52:28', NULL, NULL, NULL, '2026-05-30 20:52:28', '2026-05-30 20:52:28');
INSERT INTO `cleaning_tasks` VALUES (3, 2, NULL, '201', 'cleaning', 'normal', 'pending', 'admin', '2026-05-30 20:52:28', NULL, NULL, NULL, '2026-05-30 20:52:28', '2026-05-30 20:52:28');
INSERT INTO `cleaning_tasks` VALUES (4, 3, NULL, '301', 'inspection', 'low', 'completed', 'admin', '2026-05-30 20:52:28', NULL, NULL, NULL, '2026-05-30 20:52:28', '2026-05-30 20:52:28');
INSERT INTO `cleaning_tasks` VALUES (5, 5, NULL, '501', 'cleaning', 'urgent', 'in_progress', 'admin', '2026-05-30 20:52:28', NULL, NULL, NULL, '2026-05-30 20:52:28', '2026-05-30 20:52:28');
INSERT INTO `cleaning_tasks` VALUES (35, 1, 11, NULL, 'cleaning', 'normal', 'pending', 'admin', '2026-05-31 23:39:53', NULL, NULL, NULL, '2026-05-31 23:39:53', '2026-05-31 23:39:53');

-- ----------------------------
-- Table structure for financial_record
-- ----------------------------
DROP TABLE IF EXISTS `financial_record`;
CREATE TABLE `financial_record`  (
  `record_id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'income',
  `amount` decimal(10, 2) NULL DEFAULT 0.00,
  `payment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`record_id`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE,
  INDEX `idx_order_no`(`order_no`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of financial_record
-- ----------------------------
INSERT INTO `financial_record` VALUES (1, 'income', 299.00, 'wechat', 'ORD20240501001', 'Room charge', 'Admin', '2026-05-31 15:01:12');
INSERT INTO `financial_record` VALUES (2, 'income', 598.00, 'alipay', 'ORD20240501002', 'Room charge', 'Admin', '2026-05-31 15:01:12');
INSERT INTO `financial_record` VALUES (3, 'income', 888.00, 'wechat', 'ORD20240502001', 'Room charge', 'Admin', '2026-05-31 15:01:12');
INSERT INTO `financial_record` VALUES (4, 'expense', 5000.00, 'bank', '', 'Monthly rent', 'Admin', '2026-05-31 15:01:12');
INSERT INTO `financial_record` VALUES (5, 'income', 1200.00, 'cash', 'ORD20240503001', 'Room charge', 'Zhang San', '2026-05-31 15:01:12');
INSERT INTO `financial_record` VALUES (6, 'income', 350.00, 'alipay', 'ORD20240503002', 'Room charge', 'Zhang San', '2026-05-31 15:01:12');
INSERT INTO `financial_record` VALUES (7, 'expense', 500.00, 'cash', '', 'Supplies', 'Admin', '2026-05-31 15:01:12');
INSERT INTO `financial_record` VALUES (13, 'income', 598.00, 'wechat', 'ORD20250501001', '房费收入', 'admin', '2026-06-01 00:00:52');
INSERT INTO `financial_record` VALUES (14, 'income', 888.00, 'alipay', 'ORD20250502002', '房费收入', 'admin', '2026-06-01 00:00:52');
INSERT INTO `financial_record` VALUES (15, 'income', 299.00, 'cash', 'ORD20250510003', '房费收入', 'admin', '2026-06-01 00:00:52');
INSERT INTO `financial_record` VALUES (16, 'expense', 200.00, 'cash', NULL, '购买清洁用品', 'admin', '2026-06-01 00:00:52');
INSERT INTO `financial_record` VALUES (17, 'expense', 150.00, 'wechat', NULL, '日常维护费', 'admin', '2026-06-01 00:00:52');

-- ----------------------------
-- Table structure for floor
-- ----------------------------
DROP TABLE IF EXISTS `floor`;
CREATE TABLE `floor`  (
  `floor_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '楼层ID',
  `building_id` int(11) NOT NULL COMMENT '楼栋ID',
  `floor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '楼层名称',
  `floor_number` int(11) NOT NULL COMMENT '楼层号',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`floor_id`) USING BTREE,
  INDEX `building_id`(`building_id`) USING BTREE,
  CONSTRAINT `floor_ibfk_1` FOREIGN KEY (`building_id`) REFERENCES `building` (`building_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '楼层表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of floor
-- ----------------------------
INSERT INTO `floor` VALUES (1, 1, '1层', 1, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `floor` VALUES (2, 1, '2层', 2, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `floor` VALUES (3, 1, '3层', 3, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `floor` VALUES (4, 1, '4层', 4, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `floor` VALUES (5, 1, '5层', 5, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `floor` VALUES (6, 2, '1层', 1, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `floor` VALUES (7, 2, '2层', 2, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `floor` VALUES (8, 2, '3层', 3, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `floor` VALUES (9, 2, '4层', 4, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `floor` VALUES (10, 2, '5层', 5, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `floor` VALUES (11, 3, '1层', 1, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `floor` VALUES (12, 3, '2层', 2, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `floor` VALUES (13, 3, '3层', 3, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `floor` VALUES (14, 4, '1层', 1, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `floor` VALUES (15, 4, '2层', 2, '2026-05-06 23:03:17', '2026-05-06 23:03:17');

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `goods_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Goods ID',
  `goods_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Goods Code',
  `goods_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Goods Name',
  `category` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'other' COMMENT 'Category: drink/food/daily/other',
  `price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT 'Price',
  `stock` int(11) NULL DEFAULT 0 COMMENT 'Stock Quantity',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Description',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'on' COMMENT 'Status: on/off',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  PRIMARY KEY (`goods_id`) USING BTREE,
  UNIQUE INDEX `uk_goods_code`(`goods_code`) USING BTREE,
  INDEX `idx_category`(`category`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Goods Table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (1, 'WATER001', '冰露', '饮品', 2.00, 100, '250ml Bottled Water', 'on', '2026-05-31 14:47:47', '2026-05-31 19:05:59');
INSERT INTO `goods` VALUES (2, 'COLA001', 'Cola', 'drink', 8.00, 80, '330ml Canned Cola', 'on', '2026-05-31 14:47:47', '2026-05-31 14:47:47');
INSERT INTO `goods` VALUES (3, 'SNACK001', 'Chips', 'food', 12.00, 50, 'Chips Snack Pack', 'on', '2026-05-31 14:47:47', '2026-05-31 14:47:47');
INSERT INTO `goods` VALUES (4, 'SHAMPOO001', 'Shampoo', 'daily', 25.00, 30, 'Travel Size Shampoo 50ml', 'on', '2026-05-31 14:47:47', '2026-05-31 15:14:41');
INSERT INTO `goods` VALUES (5, 'TOOTH001', 'Toothbrush', 'daily', 10.00, 60, 'Disposable Toothbrush Set', 'on', '2026-05-31 14:47:47', '2026-05-31 14:47:47');
INSERT INTO `goods` VALUES (6, 'WATER002', 'Spring Water', 'drink', 3.00, 120, '550ml Spring Water', 'on', '2026-05-31 14:47:47', '2026-05-31 14:47:47');
INSERT INTO `goods` VALUES (7, 'JUICE001', 'Orange Juice', 'drink', 15.00, 40, '100% Orange Juice', 'on', '2026-05-31 14:47:47', '2026-05-31 15:14:43');
INSERT INTO `goods` VALUES (8, 'G001', '床单', 'daily', 150.00, 10, '纯棉被', 'off', '2026-05-31 19:08:32', '2026-06-01 09:43:31');

-- ----------------------------
-- Table structure for member_consume
-- ----------------------------
DROP TABLE IF EXISTS `member_consume`;
CREATE TABLE `member_consume`  (
  `consume_id` int(11) NOT NULL AUTO_INCREMENT,
  `member_id` int(11) NULL DEFAULT NULL,
  `member_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `member_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'room',
  `amount` decimal(10, 2) NULL DEFAULT 0.00,
  `points_change` int(11) NULL DEFAULT 0,
  `current_points` int(11) NULL DEFAULT 0,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`consume_id`) USING BTREE,
  INDEX `idx_member_id`(`member_id`) USING BTREE,
  INDEX `idx_member_no`(`member_no`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of member_consume
-- ----------------------------
INSERT INTO `member_consume` VALUES (1, 1, 'M001', 'Zhang San', '13800138001', 'room', 299.00, 30, 1280, 'Room charge', 'Admin', '2026-05-31 15:01:12');
INSERT INTO `member_consume` VALUES (2, 2, 'M002', 'Li Si', '13800138002', 'goods', 50.00, 5, 650, 'Mineral water', 'Admin', '2026-05-31 15:01:12');
INSERT INTO `member_consume` VALUES (3, 1, 'M001', 'Zhang San', '13800138001', 'room', 399.00, 40, 1320, 'Room charge', 'Admin', '2026-05-31 15:01:12');
INSERT INTO `member_consume` VALUES (4, 3, 'M003', 'Wang Wu', '13800138003', 'other', 100.00, 10, 210, 'SPA service', 'Admin', '2026-05-31 15:01:12');
INSERT INTO `member_consume` VALUES (5, 2, 'M002', 'Li Si', '13800138002', 'room', 499.00, 50, 700, 'Room charge', 'Admin', '2026-05-31 15:01:12');
INSERT INTO `member_consume` VALUES (11, 1, 'M001', '张三', '13800138001', 'room', 598.00, 598, 1598, '房费消费', 'admin', '2026-06-01 00:00:52');
INSERT INTO `member_consume` VALUES (12, 1, 'M001', '张三', '13800138001', 'goods', 88.00, 88, 1686, '购买饮品', 'admin', '2026-06-01 00:00:52');
INSERT INTO `member_consume` VALUES (13, 2, 'M002', '李四', '13800138002', 'room', 888.00, 888, 1888, '房费消费', 'admin', '2026-06-01 00:00:52');
INSERT INTO `member_consume` VALUES (14, 3, 'M003', '王五', '13800138003', 'room', 299.00, 299, 1299, '房费消费', 'admin', '2026-06-01 00:00:52');
INSERT INTO `member_consume` VALUES (15, 2, 'M002', '李四', '13800138002', 'goods', 50.00, 50, 1938, '购买食品', 'admin', '2026-06-01 00:00:52');

-- ----------------------------
-- Table structure for members
-- ----------------------------
DROP TABLE IF EXISTS `members`;
CREATE TABLE `members`  (
  `member_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '会员ID',
  `member_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会员编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'bronze' COMMENT '会员等级(vip/gold/silver/bronze/diamond)',
  `points` int(11) NULL DEFAULT 0 COMMENT '积分',
  `register_date` date NULL DEFAULT NULL COMMENT '注册日期',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'active' COMMENT '状态(active/inactive/expired)',
  `total_consumption` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '累计消费',
  `last_consumption_date` date NULL DEFAULT NULL COMMENT '最后消费日期',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`member_id`) USING BTREE,
  UNIQUE INDEX `member_no`(`member_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '会员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of members
-- ----------------------------
INSERT INTO `members` VALUES (1, 'VIP001', '张三', '13800138001', 'zhangsan@example.com', 'vip', 5000, '2024-01-15', 'active', 12500.00, '2026-05-28', '2026-05-30 19:00:27', '2026-05-30 19:00:27');
INSERT INTO `members` VALUES (2, 'VIP002', '李四', '13800138002', 'lisi@example.com', 'gold', 3200, '2024-03-20', 'active', 8600.00, '2026-05-25', '2026-05-30 19:00:27', '2026-05-30 19:00:27');
INSERT INTO `members` VALUES (3, 'VIP003', '王五', '13800138003', 'wangwu@example.com', 'silver', 1500, '2024-06-10', 'active', 4200.00, '2026-05-20', '2026-05-30 19:00:27', '2026-05-30 19:00:27');
INSERT INTO `members` VALUES (4, 'VIP004', '赵六', '13800138004', 'zhaoliu@example.com', 'bronze', 500, '2024-09-05', 'active', 1800.00, '2026-05-15', '2026-05-30 19:00:27', '2026-05-30 19:00:27');
INSERT INTO `members` VALUES (5, 'VIP005', '钱七', '13800138005', 'qianqi@example.com', 'diamond', 8800, '2023-11-20', 'active', 25000.00, '2026-05-29', '2026-05-30 19:00:27', '2026-05-30 19:00:27');
INSERT INTO `members` VALUES (6, 'VIP006', '孙八', '13800138006', 'sunba@example.com', 'gold', 2800, '2024-04-18', 'inactive', 6500.00, '2025-12-10', '2026-05-30 19:00:27', '2026-05-30 19:00:27');
INSERT INTO `members` VALUES (7, 'VIP007', '周九', '13800138007', 'zhoujiu@example.com', 'silver', 1200, '2024-07-22', 'active', 3500.00, '2026-05-18', '2026-05-30 19:00:27', '2026-05-30 19:00:27');
INSERT INTO `members` VALUES (8, 'VIP008', '吴十', '13800138008', 'wushi@example.com', 'vip', 4500, '2024-02-28', 'expired', 9800.00, '2025-08-15', '2026-05-30 19:00:27', '2026-05-30 19:00:27');

-- ----------------------------
-- Table structure for menus
-- ----------------------------
DROP TABLE IF EXISTS `menus`;
CREATE TABLE `menus`  (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` int(11) NULL DEFAULT 0 COMMENT '父菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `menu_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识（如：room:view）',
  `menu_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单类型：M-目录，C-菜单，F-按钮',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由路径',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`menu_id`) USING BTREE,
  UNIQUE INDEX `menu_key`(`menu_key`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menus
-- ----------------------------
INSERT INTO `menus` VALUES (1, 0, '首页', '/', 'C', '/', '', 'HomeFilled', 0, 1, '2026-06-01 09:06:09');
INSERT INTO `menus` VALUES (2, 0, '预订管理', 'booking:view', 'C', '/booking', NULL, 'Calendar', 2, 1, '2026-03-11 20:39:18');
INSERT INTO `menus` VALUES (3, 0, '入住管理', 'checkin:view', 'C', '/checkin', NULL, 'Discount', 3, 1, '2026-03-11 20:39:18');
INSERT INTO `menus` VALUES (4, 0, '订单管理', 'order:view', 'C', '/order', NULL, 'Document', 4, 1, '2026-03-11 20:39:18');
INSERT INTO `menus` VALUES (5, 0, '会员管理', 'member:view', 'C', '/member', NULL, 'User', 5, 1, '2026-03-11 20:39:18');
INSERT INTO `menus` VALUES (6, 0, '客房管理', 'room:view', 'C', '/room', NULL, 'HomeFilled', 6, 1, '2026-03-11 20:39:18');
INSERT INTO `menus` VALUES (7, 0, '房态面板', 'analytics:view', 'C', '/roomstatus', NULL, 'DataLine', 7, 1, '2026-03-11 20:39:18');
INSERT INTO `menus` VALUES (8, 0, '系统设置', 'system:view', 'C', '/system', NULL, 'Setting', 8, 1, '2026-03-11 20:39:18');
INSERT INTO `menus` VALUES (9, 0, '公告管理', 'notice:view', 'C', '/notice', NULL, 'BellFilled', 9, 1, '2026-05-31 12:43:02');
INSERT INTO `menus` VALUES (12, 2, '预订列表', 'booking:list', 'C', '/booking/list', NULL, 'List', 2, 1, '2026-03-11 20:39:21');
INSERT INTO `menus` VALUES (15, 3, '入住列表', 'checkin:list', 'C', '/check/list', NULL, 'List', 2, 1, '2026-03-11 20:39:21');
INSERT INTO `menus` VALUES (17, 4, '订单列表', 'order:list', 'C', '/order/list', NULL, 'List', 1, 1, '2026-03-11 20:39:21');
INSERT INTO `menus` VALUES (28, 6, '清洁管理', 'room:cleaning', 'C', '/room/cleaning', NULL, 'Brush', 3, 1, '2026-03-11 20:39:21');
INSERT INTO `menus` VALUES (33, 8, '用户管理', 'system:user', 'C', '/system/user', NULL, 'User', 1, 1, '2026-03-11 20:39:21');
INSERT INTO `menus` VALUES (34, 8, '角色管理', 'system:role', 'C', '/system/role', NULL, 'UserFilled', 2, 1, '2026-03-11 20:39:21');
INSERT INTO `menus` VALUES (35, 8, '菜单管理', 'system:menu', 'C', '/system/menu', NULL, 'Menu', 3, 1, '2026-03-11 20:39:21');
INSERT INTO `menus` VALUES (36, 9, '新增公告', 'notice:create', 'C', '/notice/create', NULL, 'Bell', 9, 1, '2026-05-31 12:43:44');
INSERT INTO `menus` VALUES (37, 9, '公告列表', 'notice:list', 'C', '/notice/list', NULL, 'Bell', 0, 1, '2026-05-31 13:19:50');
INSERT INTO `menus` VALUES (46, 0, '财务管理', 'finance', 'M', '/finance', NULL, 'Money', 10, 1, '2026-05-31 14:24:38');
INSERT INTO `menus` VALUES (47, 46, '历史订单', 'historyOrder', 'C', '/finance/history-order', 'finance/historyOrder/index', 'Document', 1, 1, '2026-05-31 14:24:38');
INSERT INTO `menus` VALUES (48, 46, '财务报表', 'financialReport', 'C', '/finance/financial-report', 'finance/financialReport/index', 'DataLine', 2, 1, '2026-05-31 14:24:38');
INSERT INTO `menus` VALUES (49, 0, '会员消费', 'memberConsume', 'M', '/member-consume', NULL, 'Wallet', 11, 1, '2026-05-31 14:24:38');
INSERT INTO `menus` VALUES (50, 49, '消费记录', 'consumeRecord', 'C', '/member-consume/consume-record', 'memberConsume/consumeRecord/index', 'List', 1, 1, '2026-05-31 14:24:38');
INSERT INTO `menus` VALUES (51, 0, '商品库存', 'goodsInventory', 'M', '/goods-inventory', NULL, 'Goods', 12, 1, '2026-05-31 14:24:38');
INSERT INTO `menus` VALUES (52, 51, '商品管理', 'goodsManage', 'C', '/goods-inventory/goods-manage', 'goodsInventory/goodsManage/index', 'Box', 1, 1, '2026-05-31 14:24:38');
INSERT INTO `menus` VALUES (54, 8, '操作日志', 'system:operationLog', 'C', '/system/operation-log', 'system/operationLog/index', 'DocumentChecked', 4, 1, '2026-05-31 14:26:27');

-- ----------------------------
-- Table structure for numbers
-- ----------------------------
DROP TABLE IF EXISTS `numbers`;
CREATE TABLE `numbers`  (
  `n` int(11) NOT NULL,
  PRIMARY KEY (`n`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of numbers
-- ----------------------------
INSERT INTO `numbers` VALUES (0);
INSERT INTO `numbers` VALUES (1);
INSERT INTO `numbers` VALUES (2);
INSERT INTO `numbers` VALUES (3);
INSERT INTO `numbers` VALUES (4);
INSERT INTO `numbers` VALUES (5);
INSERT INTO `numbers` VALUES (6);
INSERT INTO `numbers` VALUES (7);
INSERT INTO `numbers` VALUES (8);
INSERT INTO `numbers` VALUES (9);
INSERT INTO `numbers` VALUES (10);
INSERT INTO `numbers` VALUES (11);
INSERT INTO `numbers` VALUES (12);
INSERT INTO `numbers` VALUES (13);
INSERT INTO `numbers` VALUES (14);
INSERT INTO `numbers` VALUES (15);
INSERT INTO `numbers` VALUES (16);
INSERT INTO `numbers` VALUES (17);
INSERT INTO `numbers` VALUES (18);
INSERT INTO `numbers` VALUES (19);
INSERT INTO `numbers` VALUES (20);
INSERT INTO `numbers` VALUES (21);
INSERT INTO `numbers` VALUES (22);
INSERT INTO `numbers` VALUES (23);
INSERT INTO `numbers` VALUES (24);
INSERT INTO `numbers` VALUES (25);
INSERT INTO `numbers` VALUES (26);
INSERT INTO `numbers` VALUES (27);
INSERT INTO `numbers` VALUES (28);
INSERT INTO `numbers` VALUES (29);
INSERT INTO `numbers` VALUES (30);
INSERT INTO `numbers` VALUES (31);
INSERT INTO `numbers` VALUES (32);
INSERT INTO `numbers` VALUES (33);
INSERT INTO `numbers` VALUES (34);
INSERT INTO `numbers` VALUES (35);
INSERT INTO `numbers` VALUES (36);
INSERT INTO `numbers` VALUES (37);
INSERT INTO `numbers` VALUES (38);
INSERT INTO `numbers` VALUES (39);
INSERT INTO `numbers` VALUES (40);
INSERT INTO `numbers` VALUES (41);
INSERT INTO `numbers` VALUES (42);
INSERT INTO `numbers` VALUES (43);
INSERT INTO `numbers` VALUES (44);
INSERT INTO `numbers` VALUES (45);
INSERT INTO `numbers` VALUES (46);
INSERT INTO `numbers` VALUES (47);
INSERT INTO `numbers` VALUES (48);
INSERT INTO `numbers` VALUES (49);
INSERT INTO `numbers` VALUES (50);
INSERT INTO `numbers` VALUES (51);
INSERT INTO `numbers` VALUES (52);
INSERT INTO `numbers` VALUES (53);
INSERT INTO `numbers` VALUES (54);
INSERT INTO `numbers` VALUES (55);
INSERT INTO `numbers` VALUES (56);
INSERT INTO `numbers` VALUES (57);
INSERT INTO `numbers` VALUES (58);
INSERT INTO `numbers` VALUES (59);
INSERT INTO `numbers` VALUES (60);
INSERT INTO `numbers` VALUES (61);
INSERT INTO `numbers` VALUES (62);
INSERT INTO `numbers` VALUES (63);
INSERT INTO `numbers` VALUES (64);
INSERT INTO `numbers` VALUES (65);
INSERT INTO `numbers` VALUES (66);
INSERT INTO `numbers` VALUES (67);
INSERT INTO `numbers` VALUES (68);
INSERT INTO `numbers` VALUES (69);
INSERT INTO `numbers` VALUES (70);
INSERT INTO `numbers` VALUES (71);
INSERT INTO `numbers` VALUES (72);
INSERT INTO `numbers` VALUES (73);
INSERT INTO `numbers` VALUES (74);
INSERT INTO `numbers` VALUES (75);
INSERT INTO `numbers` VALUES (76);
INSERT INTO `numbers` VALUES (77);
INSERT INTO `numbers` VALUES (78);
INSERT INTO `numbers` VALUES (79);
INSERT INTO `numbers` VALUES (80);
INSERT INTO `numbers` VALUES (81);
INSERT INTO `numbers` VALUES (82);
INSERT INTO `numbers` VALUES (83);
INSERT INTO `numbers` VALUES (84);
INSERT INTO `numbers` VALUES (85);
INSERT INTO `numbers` VALUES (86);
INSERT INTO `numbers` VALUES (87);
INSERT INTO `numbers` VALUES (88);
INSERT INTO `numbers` VALUES (89);
INSERT INTO `numbers` VALUES (90);
INSERT INTO `numbers` VALUES (91);
INSERT INTO `numbers` VALUES (92);
INSERT INTO `numbers` VALUES (93);
INSERT INTO `numbers` VALUES (94);
INSERT INTO `numbers` VALUES (95);
INSERT INTO `numbers` VALUES (96);
INSERT INTO `numbers` VALUES (97);
INSERT INTO `numbers` VALUES (98);
INSERT INTO `numbers` VALUES (99);
INSERT INTO `numbers` VALUES (100);
INSERT INTO `numbers` VALUES (101);
INSERT INTO `numbers` VALUES (102);
INSERT INTO `numbers` VALUES (103);
INSERT INTO `numbers` VALUES (104);
INSERT INTO `numbers` VALUES (105);
INSERT INTO `numbers` VALUES (106);
INSERT INTO `numbers` VALUES (107);
INSERT INTO `numbers` VALUES (108);
INSERT INTO `numbers` VALUES (109);
INSERT INTO `numbers` VALUES (110);
INSERT INTO `numbers` VALUES (111);
INSERT INTO `numbers` VALUES (112);
INSERT INTO `numbers` VALUES (113);
INSERT INTO `numbers` VALUES (114);
INSERT INTO `numbers` VALUES (115);
INSERT INTO `numbers` VALUES (116);
INSERT INTO `numbers` VALUES (117);
INSERT INTO `numbers` VALUES (118);
INSERT INTO `numbers` VALUES (119);
INSERT INTO `numbers` VALUES (120);
INSERT INTO `numbers` VALUES (121);
INSERT INTO `numbers` VALUES (122);
INSERT INTO `numbers` VALUES (123);
INSERT INTO `numbers` VALUES (124);
INSERT INTO `numbers` VALUES (125);
INSERT INTO `numbers` VALUES (126);
INSERT INTO `numbers` VALUES (127);
INSERT INTO `numbers` VALUES (128);
INSERT INTO `numbers` VALUES (129);
INSERT INTO `numbers` VALUES (130);
INSERT INTO `numbers` VALUES (131);
INSERT INTO `numbers` VALUES (132);
INSERT INTO `numbers` VALUES (133);
INSERT INTO `numbers` VALUES (134);
INSERT INTO `numbers` VALUES (135);
INSERT INTO `numbers` VALUES (136);
INSERT INTO `numbers` VALUES (137);
INSERT INTO `numbers` VALUES (138);
INSERT INTO `numbers` VALUES (139);
INSERT INTO `numbers` VALUES (140);
INSERT INTO `numbers` VALUES (141);
INSERT INTO `numbers` VALUES (142);
INSERT INTO `numbers` VALUES (143);
INSERT INTO `numbers` VALUES (144);
INSERT INTO `numbers` VALUES (145);
INSERT INTO `numbers` VALUES (146);
INSERT INTO `numbers` VALUES (147);
INSERT INTO `numbers` VALUES (148);
INSERT INTO `numbers` VALUES (149);
INSERT INTO `numbers` VALUES (150);
INSERT INTO `numbers` VALUES (151);
INSERT INTO `numbers` VALUES (152);
INSERT INTO `numbers` VALUES (153);
INSERT INTO `numbers` VALUES (154);
INSERT INTO `numbers` VALUES (155);
INSERT INTO `numbers` VALUES (156);
INSERT INTO `numbers` VALUES (157);
INSERT INTO `numbers` VALUES (158);
INSERT INTO `numbers` VALUES (159);
INSERT INTO `numbers` VALUES (160);
INSERT INTO `numbers` VALUES (161);
INSERT INTO `numbers` VALUES (162);
INSERT INTO `numbers` VALUES (163);
INSERT INTO `numbers` VALUES (164);
INSERT INTO `numbers` VALUES (165);
INSERT INTO `numbers` VALUES (166);
INSERT INTO `numbers` VALUES (167);
INSERT INTO `numbers` VALUES (168);
INSERT INTO `numbers` VALUES (169);
INSERT INTO `numbers` VALUES (170);
INSERT INTO `numbers` VALUES (171);
INSERT INTO `numbers` VALUES (172);
INSERT INTO `numbers` VALUES (173);
INSERT INTO `numbers` VALUES (174);
INSERT INTO `numbers` VALUES (175);
INSERT INTO `numbers` VALUES (176);
INSERT INTO `numbers` VALUES (177);
INSERT INTO `numbers` VALUES (178);
INSERT INTO `numbers` VALUES (179);
INSERT INTO `numbers` VALUES (180);
INSERT INTO `numbers` VALUES (181);
INSERT INTO `numbers` VALUES (182);
INSERT INTO `numbers` VALUES (183);
INSERT INTO `numbers` VALUES (184);
INSERT INTO `numbers` VALUES (185);
INSERT INTO `numbers` VALUES (186);
INSERT INTO `numbers` VALUES (187);
INSERT INTO `numbers` VALUES (188);
INSERT INTO `numbers` VALUES (189);
INSERT INTO `numbers` VALUES (190);
INSERT INTO `numbers` VALUES (191);
INSERT INTO `numbers` VALUES (192);
INSERT INTO `numbers` VALUES (193);
INSERT INTO `numbers` VALUES (194);
INSERT INTO `numbers` VALUES (195);
INSERT INTO `numbers` VALUES (196);
INSERT INTO `numbers` VALUES (197);
INSERT INTO `numbers` VALUES (198);
INSERT INTO `numbers` VALUES (199);
INSERT INTO `numbers` VALUES (200);

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'other',
  `module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operator_id` int(11) NULL DEFAULT NULL,
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE,
  INDEX `idx_module`(`module`) USING BTREE,
  INDEX `idx_operator`(`operator`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operation_log
-- ----------------------------
INSERT INTO `operation_log` VALUES (1, 'login', 'system', 'User login', 'Admin', 1, '192.168.1.100', '{\"username\":\"admin\"}', 'success', '2026-05-31 15:01:12');
INSERT INTO `operation_log` VALUES (2, 'add', 'room', 'Add room', 'Admin', 1, '192.168.1.100', '{\"roomNo\":\"101\",\"roomType\":\"Standard\"}', 'success', '2026-05-31 15:01:12');
INSERT INTO `operation_log` VALUES (3, 'edit', 'member', 'Edit member info', 'Admin', 1, '192.168.1.100', '{\"memberId\":1,\"phone\":\"13800138001\"}', 'success', '2026-05-31 15:01:12');
INSERT INTO `operation_log` VALUES (4, 'delete', 'notice', 'Delete notice', 'Admin', 1, '192.168.1.101', '{\"noticeId\":5}', 'success', '2026-05-31 15:01:12');
INSERT INTO `operation_log` VALUES (5, 'login', 'system', 'User login', 'Zhang San', 2, '192.168.1.102', '{\"username\":\"zhangsan\"}', 'success', '2026-05-31 15:01:12');
INSERT INTO `operation_log` VALUES (6, 'add', 'order', 'Create order', 'Zhang San', 2, '192.168.1.102', '{\"roomId\":1,\"guestName\":\"Li Si\"}', 'success', '2026-05-31 15:01:12');
INSERT INTO `operation_log` VALUES (7, 'logout', 'system', 'User logout', 'Zhang San', 2, '192.168.1.102', '', 'success', '2026-05-31 15:01:12');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `order_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单号',
  `member_id` int(11) NULL DEFAULT NULL COMMENT '会员ID',
  `room_id` int(11) NOT NULL COMMENT '房间ID',
  `room_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '房间号',
  `guest_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客人姓名',
  `guest_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客人电话',
  `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `check_in_date` date NOT NULL COMMENT '入住日期',
  `check_out_date` date NOT NULL COMMENT '离店日期',
  `nights` int(11) NOT NULL COMMENT '入住晚数',
  `adults` int(11) NULL DEFAULT 2 COMMENT '成人数量',
  `children` int(11) NULL DEFAULT 0 COMMENT '儿童数量',
  `total_amount` decimal(12, 2) NOT NULL COMMENT '订单总金额',
  `paid_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '已付金额',
  `deposit_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '押金金额',
  `order_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'pending' COMMENT '订单状态：pending-待确认，confirmed-已确认，checked_in-已入住，checked_out-已离店，cancelled-已取消，no_show-未到店',
  `payment_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'unpaid' COMMENT '支付状态：unpaid-未支付，partial-部分支付，paid-已支付，refunded-已退款',
  `payment_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式：cash-现金，card-银行卡，wechat-微信，alipay-支付宝',
  `source` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'frontdesk' COMMENT '订单来源：frontdesk-前台，online-在线，phone-电话',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人（用户ID）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `region` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '瀹㈡簮鍦板尯锛屽?鍖椾含銆佷笂娴枫?骞夸笢绛',
  `book_channel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '棰勮?娓犻亾锛屽?瀹樼綉銆佹惡绋嬨?缇庡洟銆佺數璇濈瓑',
  `customer_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '瀹㈡埛绫诲瀷锛屽?鏁ｅ?銆佸洟浣撱?浼氬憳绛',
  `check_in_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '鍏ヤ綇绫诲瀷锛屽?鏁ｅ?銆佸洟浣撱?浼氬憳绛',
  PRIMARY KEY (`order_id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no`) USING BTREE,
  INDEX `member_id`(`member_id`) USING BTREE,
  INDEX `room_id`(`room_id`) USING BTREE,
  INDEX `create_by`(`create_by`) USING BTREE,
  INDEX `idx_order_no`(`order_no`) USING BTREE,
  INDEX `idx_guest_phone`(`guest_phone`) USING BTREE,
  INDEX `idx_check_in_date`(`check_in_date`) USING BTREE,
  INDEX `idx_region`(`region`) USING BTREE,
  INDEX `idx_book_channel`(`book_channel`) USING BTREE,
  INDEX `idx_customer_type`(`customer_type`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_orders_region`(`region`) USING BTREE,
  INDEX `idx_orders_book_channel`(`book_channel`) USING BTREE,
  INDEX `idx_orders_customer_type`(`customer_type`) USING BTREE,
  INDEX `idx_orders_create_time`(`create_time`) USING BTREE,
  INDEX `idx_orders_check_in_type`(`check_in_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, 'ORD20240101001', 1, 4, 'A104', '张三', '13900000001', NULL, '2024-01-01', '2026-05-06', 2, 2, 0, 998.00, 998.00, 0.00, 'checked_in', 'unpaid', NULL, 'frontdesk', NULL, 3, '2024-01-01 00:00:00', '2026-05-22 10:55:38', '北京', 'official', '散客', 'individual');
INSERT INTO `orders` VALUES (2, 'ORD20240102002', 2, 5, 'A201', '李四', '13900000002', NULL, '2024-01-02', '2024-01-05', 3, 2, 0, 1797.00, 500.00, 0.00, 'reserved', 'unpaid', NULL, 'frontdesk', NULL, 3, '2024-01-02 00:00:00', '2026-05-30 20:51:20', '上海', 'ctrip', '会员', NULL);
INSERT INTO `orders` VALUES (3, 'ORD20240103003', NULL, 1, 'A101', '王小明', '13912000005', NULL, '2024-01-03', '2024-01-04', 1, 2, 0, 399.00, 399.00, 0.00, 'confirmed', 'unpaid', NULL, 'frontdesk', NULL, 3, '2024-01-03 00:00:00', '2026-05-26 21:40:34', '广东', 'meituan', '散客', NULL);
INSERT INTO `orders` VALUES (4, 'ORD20240104004', 3, 9, 'A302', '王五', '13900000003', NULL, '2024-01-04', '2024-01-07', 3, 2, 0, 2697.00, 2697.00, 0.00, 'checked_out', 'unpaid', NULL, 'frontdesk', NULL, 3, '2024-01-04 00:00:00', '2026-05-22 10:55:38', '浙江', 'phone', '团体', 'group');
INSERT INTO `orders` VALUES (5, 'ORD20240105005', NULL, 7, 'A203', '赵丽颖', '13900000006', NULL, '2024-01-05', '2024-01-06', 1, 2, 0, 429.00, 0.00, 0.00, 'cancelled', 'unpaid', NULL, 'frontdesk', NULL, 3, '2024-01-05 00:00:00', '2026-05-22 10:55:38', '江苏', 'official', '会员', 'vip');
INSERT INTO `orders` VALUES (6, 'ORD20260405001', NULL, 1, 'A101', '张三', '13900000001', NULL, '2026-04-05', '2026-05-06', 2, 2, 0, 800.00, 800.00, 0.00, 'checked_in', 'unpaid', NULL, 'frontdesk', NULL, NULL, '2026-04-05 00:00:00', '2026-05-22 10:55:38', '北京', 'official', '散客', 'individual');
INSERT INTO `orders` VALUES (7, 'ORD20260405002', NULL, 2, 'A102', '李四', '13900000002', NULL, '2026-04-03', '2026-04-05', 2, 2, 0, 600.00, 600.00, 0.00, 'checked_out', 'unpaid', NULL, 'frontdesk', NULL, NULL, '2026-04-03 00:00:00', '2026-05-22 10:55:38', '上海', 'ctrip', '会员', 'member');
INSERT INTO `orders` VALUES (8, 'ORD20260405003', NULL, 3, 'A103', '王五', '13900000003', NULL, '2026-04-04', '2026-05-06', 2, 2, 0, 700.00, 700.00, 0.00, 'checked_in', 'unpaid', NULL, 'frontdesk', NULL, NULL, '2026-04-04 00:00:00', '2026-05-22 10:55:38', '广东', 'meituan', '散客', 'individual');
INSERT INTO `orders` VALUES (35, 'ORD20260401001', 1, 1, 'A101', '张三', '13800138001', '110101199001011234', '2026-04-01', '2026-04-03', 2, 2, 0, 1200.00, 1200.00, 200.00, 'checked_out', 'paid', 'online', 'frontdesk', NULL, NULL, '2026-04-08 23:36:19', '2026-05-22 10:55:38', '北京', 'official', '散客', 'vip');
INSERT INTO `orders` VALUES (36, 'ORD20260401002', 2, 2, 'A102', '李四', '13900139002', '310101199001012345', '2026-04-02', '2026-04-05', 3, 1, 1, 1800.00, 1800.00, 300.00, 'checked_out', 'paid', 'online', 'frontdesk', NULL, NULL, '2026-04-08 23:36:19', '2026-05-22 10:55:38', '上海', 'official', '散客', 'individual');
INSERT INTO `orders` VALUES (37, 'ORD20260401003', 3, 3, 'A103', '王五', '13700137003', '440101199001013456', '2026-04-03', '2026-04-06', 3, 3, 0, 2700.00, 2700.00, 500.00, 'checked_out', 'paid', 'online', 'frontdesk', NULL, NULL, '2026-04-08 23:36:19', '2026-05-22 10:55:38', '广东', 'ctrip', '团体', 'member');
INSERT INTO `orders` VALUES (38, 'ORD20260401004', 4, 4, 'A104', '赵六', '13600136004', '510101199001014567', '2026-04-04', '2026-04-07', 3, 2, 1, 2100.00, 2100.00, 400.00, 'checked_out', 'paid', 'online', 'frontdesk', NULL, NULL, '2026-04-08 23:36:19', '2026-05-22 10:55:38', '北京', 'meituan', '会员', 'individual');
INSERT INTO `orders` VALUES (46, 'ORD20260408001', NULL, 1, 'A101', '张三', '13800138001', NULL, '2026-04-07', '2026-04-08', 1, 2, 0, 300.00, 300.00, 0.00, 'checked_out', 'unpaid', NULL, 'frontdesk', NULL, NULL, '2026-04-10 00:42:09', '2026-05-22 10:55:38', '北京', 'official', NULL, 'individual');
INSERT INTO `orders` VALUES (47, 'ORD20260409001', NULL, 2, 'A102', '李四', '13800138002', NULL, '2026-04-08', '2026-04-09', 1, 2, 0, 350.00, 350.00, 0.00, 'checked_out', 'unpaid', NULL, 'frontdesk', NULL, NULL, '2026-04-10 00:42:09', '2026-05-22 10:55:38', '上海', 'ctrip', NULL, 'member');
INSERT INTO `orders` VALUES (57, 'ORD20260410001231', NULL, 12, 'A101', 'guest', '17266222222', '500292844194848812', '2026-05-25', '2026-05-26', 0, 1, 0, 0.00, 0.00, 0.00, '', '', '', 'frontdesk', '', NULL, '2026-05-26 17:56:15', '2026-05-26 17:56:15', NULL, '', '', '');
INSERT INTO `orders` VALUES (58, 'ORD20260530001', NULL, 5, 'A201', '测试客人A', '13800138001', '110101199001011234', '2026-05-28', '2026-05-31', 3, 2, 0, 897.00, 0.00, 0.00, 'checked_in', 'paid', NULL, 'frontdesk', NULL, NULL, '2026-05-30 18:19:09', '2026-05-30 18:19:09', NULL, 'frontdesk', NULL, NULL);
INSERT INTO `orders` VALUES (59, 'ORD20260530002', NULL, 8, 'A301', '测试客人B', '13800138002', '110101199002021234', '2026-05-29', '2026-05-31', 2, 1, 1, 598.00, 0.00, 0.00, 'checked_in', 'unpaid', NULL, 'frontdesk', NULL, NULL, '2026-05-30 18:19:09', '2026-05-30 18:19:09', NULL, 'meituan', NULL, NULL);
INSERT INTO `orders` VALUES (60, 'OD20260531001', NULL, 1, 'A101', '张三', '13800000001', NULL, '2026-05-29', '2026-05-31', 2, 2, 0, 398.00, 398.00, 0.00, 'checked_out', 'paid', NULL, 'frontdesk', NULL, NULL, '2026-05-31 23:31:48', '2026-05-31 23:31:48', '北京', NULL, NULL, NULL);
INSERT INTO `orders` VALUES (61, 'OD20260531002', NULL, 2, 'A102', '李四', '13800000002', NULL, '2026-05-30', '2026-05-31', 1, 2, 0, 258.00, 258.00, 0.00, 'checked_out', 'paid', NULL, 'frontdesk', NULL, NULL, '2026-05-31 23:31:48', '2026-05-31 23:31:48', '上海', NULL, NULL, NULL);
INSERT INTO `orders` VALUES (62, 'OD20260531003', NULL, 3, 'A103', '王五', '13800000003', NULL, '2026-05-28', '2026-05-31', 3, 2, 0, 897.00, 897.00, 0.00, 'checked_out', 'paid', NULL, 'frontdesk', NULL, NULL, '2026-05-31 23:31:48', '2026-05-31 23:31:48', '广州', NULL, NULL, NULL);
INSERT INTO `orders` VALUES (63, 'OD20260531004', NULL, 4, 'A104', '赵六', '13800000004', NULL, '2026-05-31', '2026-05-31', 1, 2, 0, 188.00, 188.00, 0.00, 'checked_out', 'paid', NULL, 'frontdesk', NULL, NULL, '2026-05-31 23:31:48', '2026-05-31 23:31:48', '深圳', NULL, NULL, NULL);
INSERT INTO `orders` VALUES (68, 'ORD20250501001', NULL, 1, '101', '张三', '13800138001', NULL, '2025-05-01', '2025-05-03', 2, 2, 0, 598.00, 0.00, 0.00, 'completed', 'paid', 'wechat', 'frontdesk', '', NULL, '2026-06-01 00:00:51', '2026-06-01 00:00:51', NULL, '前台', NULL, NULL);
INSERT INTO `orders` VALUES (69, 'ORD20250502002', NULL, 2, '205', '李四', '13800138002', NULL, '2025-05-02', '2025-05-05', 3, 2, 0, 888.00, 0.00, 0.00, 'completed', 'paid', 'alipay', 'frontdesk', '', NULL, '2026-06-01 00:00:51', '2026-06-01 00:00:51', NULL, '线上', NULL, NULL);
INSERT INTO `orders` VALUES (70, 'ORD20250510003', NULL, 3, '302', '王五', '13800138003', NULL, '2025-05-10', '2025-05-11', 1, 2, 0, 299.00, 0.00, 0.00, 'completed', 'paid', 'cash', 'frontdesk', '', NULL, '2026-06-01 00:00:51', '2026-06-01 00:00:51', NULL, '电话', NULL, NULL);
INSERT INTO `orders` VALUES (71, 'ORD20250515004', NULL, 1, '108', '赵六', '13800138004', NULL, '2025-05-15', '2025-05-18', 3, 2, 0, 897.00, 0.00, 0.00, 'cancelled', 'unpaid', 'bank', 'frontdesk', '客人取消', NULL, '2026-06-01 00:00:51', '2026-06-01 00:00:51', NULL, '线上', NULL, NULL);

-- ----------------------------
-- Table structure for role_menus
-- ----------------------------
DROP TABLE IF EXISTS `role_menus`;
CREATE TABLE `role_menus`  (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE,
  INDEX `menu_id`(`menu_id`) USING BTREE,
  CONSTRAINT `role_menus_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `role_menus_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `menus` (`menu_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_menus
-- ----------------------------
INSERT INTO `role_menus` VALUES (1, 1);
INSERT INTO `role_menus` VALUES (127, 1);
INSERT INTO `role_menus` VALUES (1, 2);
INSERT INTO `role_menus` VALUES (2, 2);
INSERT INTO `role_menus` VALUES (4, 2);
INSERT INTO `role_menus` VALUES (8, 2);
INSERT INTO `role_menus` VALUES (1, 3);
INSERT INTO `role_menus` VALUES (2, 3);
INSERT INTO `role_menus` VALUES (4, 3);
INSERT INTO `role_menus` VALUES (1, 4);
INSERT INTO `role_menus` VALUES (2, 4);
INSERT INTO `role_menus` VALUES (4, 4);
INSERT INTO `role_menus` VALUES (1, 5);
INSERT INTO `role_menus` VALUES (1, 6);
INSERT INTO `role_menus` VALUES (2, 6);
INSERT INTO `role_menus` VALUES (6, 6);
INSERT INTO `role_menus` VALUES (1, 7);
INSERT INTO `role_menus` VALUES (2, 7);
INSERT INTO `role_menus` VALUES (8, 7);
INSERT INTO `role_menus` VALUES (127, 7);
INSERT INTO `role_menus` VALUES (1, 8);
INSERT INTO `role_menus` VALUES (8, 8);
INSERT INTO `role_menus` VALUES (1, 9);
INSERT INTO `role_menus` VALUES (1, 12);
INSERT INTO `role_menus` VALUES (2, 12);
INSERT INTO `role_menus` VALUES (4, 12);
INSERT INTO `role_menus` VALUES (8, 12);
INSERT INTO `role_menus` VALUES (1, 15);
INSERT INTO `role_menus` VALUES (2, 15);
INSERT INTO `role_menus` VALUES (4, 15);
INSERT INTO `role_menus` VALUES (1, 17);
INSERT INTO `role_menus` VALUES (2, 17);
INSERT INTO `role_menus` VALUES (4, 17);
INSERT INTO `role_menus` VALUES (1, 28);
INSERT INTO `role_menus` VALUES (6, 28);
INSERT INTO `role_menus` VALUES (1, 33);
INSERT INTO `role_menus` VALUES (8, 33);
INSERT INTO `role_menus` VALUES (1, 34);
INSERT INTO `role_menus` VALUES (8, 34);
INSERT INTO `role_menus` VALUES (1, 35);
INSERT INTO `role_menus` VALUES (8, 35);
INSERT INTO `role_menus` VALUES (1, 36);
INSERT INTO `role_menus` VALUES (1, 37);
INSERT INTO `role_menus` VALUES (1, 46);
INSERT INTO `role_menus` VALUES (1, 47);
INSERT INTO `role_menus` VALUES (1, 48);
INSERT INTO `role_menus` VALUES (1, 49);
INSERT INTO `role_menus` VALUES (1, 50);
INSERT INTO `role_menus` VALUES (1, 51);
INSERT INTO `role_menus` VALUES (1, 52);
INSERT INTO `role_menus` VALUES (1, 54);

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色权限字符串',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `role_name`(`role_name`) USING BTREE,
  UNIQUE INDEX `role_key`(`role_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 129 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES (1, 'admin', 'admin', '超级管理员', 0, 1, '2026-04-18 21:37:44');
INSERT INTO `roles` VALUES (2, '酒店经理', 'manager', '酒店总经理/店长', 2, 1, '2026-03-11 20:34:42');
INSERT INTO `roles` VALUES (3, '前台主管', 'frontdesk_supervisor', '前台主管', 3, 1, '2026-03-11 20:34:42');
INSERT INTO `roles` VALUES (4, '前台员工', 'frontdesk', '前台接待人员', 4, 1, '2026-03-11 20:34:42');
INSERT INTO `roles` VALUES (5, '客房主管', 'housekeeping_supervisor', '客房部主管', 5, 1, '2026-03-11 20:34:42');
INSERT INTO `roles` VALUES (6, '客房服务员', 'housekeeping', '客房保洁人员', 6, 1, '2026-03-11 20:34:42');
INSERT INTO `roles` VALUES (7, '财务人员', 'finance', '财务人员', 7, 1, '2026-03-11 20:34:42');
INSERT INTO `roles` VALUES (8, '只读用户', 'readonly', '仅可查看数据', 8, 1, '2026-03-11 20:34:42');
INSERT INTO `roles` VALUES (9, 'xunni', 'guest', '新员工', 0, 1, '2026-04-18 21:40:08');
INSERT INTO `roles` VALUES (127, 'test', 'test', 'test', 0, 1, '2026-04-19 00:13:04');
INSERT INTO `roles` VALUES (128, 'test2', 'test2', 'test', 0, 1, '2026-04-27 21:43:41');

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `room_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '房间ID',
  `room_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '房间号',
  `building` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '妤兼爧鍚嶇О锛屽?A鏍嬨?B鏍嬬瓑',
  `floor` int(11) NULL DEFAULT NULL COMMENT '妤煎眰鍙',
  `building_id` int(11) NOT NULL COMMENT '楼栋ID',
  `floor_id` int(11) NOT NULL COMMENT '楼层ID',
  `type_id` int(11) NOT NULL COMMENT '房型ID',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '鎴块棿鐘舵?锛歛vailable(绌洪棽)/occupied(宸插叆浣?/dirty(鑴忔埧)/maintenance(缁翠慨)/booked(宸查?璁?/locked(閿佹埧)',
  `guest_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客人姓名',
  `guest_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '房卡号',
  `check_in_time` datetime NULL DEFAULT NULL COMMENT '入住时间',
  `check_out_time` datetime NULL DEFAULT NULL COMMENT '预计退房时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`room_id`) USING BTREE,
  UNIQUE INDEX `room_number`(`room_number`) USING BTREE,
  INDEX `building_id`(`building_id`) USING BTREE,
  INDEX `floor_id`(`floor_id`) USING BTREE,
  INDEX `type_id`(`type_id`) USING BTREE,
  INDEX `idx_room_building`(`building`) USING BTREE,
  INDEX `idx_room_floor`(`floor`) USING BTREE,
  INDEX `idx_room_status`(`status`) USING BTREE,
  CONSTRAINT `room_ibfk_1` FOREIGN KEY (`building_id`) REFERENCES `building` (`building_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `room_ibfk_2` FOREIGN KEY (`floor_id`) REFERENCES `floor` (`floor_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `room_ibfk_3` FOREIGN KEY (`type_id`) REFERENCES `room_type` (`type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '房间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room
-- ----------------------------
INSERT INTO `room` VALUES (1, 'A101', 'A', NULL, 1, 1, 1, 'empty_clean', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-15 10:14:15');
INSERT INTO `room` VALUES (2, 'A102', 'A', NULL, 1, 1, 1, 'empty_clean', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-15 10:14:15');
INSERT INTO `room` VALUES (3, 'A103', 'A', NULL, 1, 1, 2, 'occupied_clean', '张三', 'F001', '2026-04-04 00:00:00', '2024-01-18 12:00:00', NULL, '2026-05-06 23:03:17', '2026-05-21 09:35:50');
INSERT INTO `room` VALUES (4, 'A104', 'A', NULL, 1, 1, 2, 'occupied_dirty', '李四', 'F002', '2024-01-01 00:00:00', '2024-01-16 12:00:00', NULL, '2026-05-06 23:03:17', '2026-05-21 09:35:50');
INSERT INTO `room` VALUES (5, 'A201', 'A', NULL, 1, 2, 3, 'empty_clean', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-30 18:35:42');
INSERT INTO `room` VALUES (6, 'A202', 'A', NULL, 1, 2, 3, 'maintenance', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-15 10:14:15');
INSERT INTO `room` VALUES (7, 'A203', 'A', NULL, 1, 2, 4, 'occupied_clean', '王五', 'F003', '2024-01-15 16:00:00', '2024-01-17 12:00:00', NULL, '2026-05-06 23:03:17', '2026-05-15 10:14:15');
INSERT INTO `room` VALUES (8, 'A301', 'A', NULL, 1, 3, 5, 'empty_clean', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-30 18:38:39');
INSERT INTO `room` VALUES (9, 'A302', 'A', NULL, 1, 3, 5, 'empty_clean', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-15 10:14:15');
INSERT INTO `room` VALUES (10, 'A401', 'A', NULL, 1, 4, 6, 'empty_clean', '赵六', 'F004', '2024-01-15 09:00:00', '2024-01-19 12:00:00', NULL, '2026-05-06 23:03:17', '2026-05-30 18:36:15');
INSERT INTO `room` VALUES (11, 'A402', 'A', NULL, 1, 4, 6, 'pending_cleaning', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-31 23:39:53');
INSERT INTO `room` VALUES (12, 'A501', 'A', NULL, 1, 5, 7, 'free', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-15 10:14:15');
INSERT INTO `room` VALUES (13, 'B101', 'B', NULL, 2, 6, 1, 'empty_clean', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-15 10:34:37');
INSERT INTO `room` VALUES (14, 'B102', 'B', NULL, 2, 6, 1, 'occupied_clean', '孙七', 'F005', '2024-01-15 11:00:00', '2024-01-17 12:00:00', NULL, '2026-05-06 23:03:17', '2026-05-15 10:34:38');
INSERT INTO `room` VALUES (15, 'B201', 'B', NULL, 2, 7, 2, 'empty_clean', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-15 10:34:39');
INSERT INTO `room` VALUES (16, 'B202', 'B', NULL, 2, 7, 2, 'occupied_dirty', '周八', 'F006', '2024-01-13 14:00:00', '2024-01-15 12:00:00', NULL, '2026-05-06 23:03:17', '2026-05-15 10:34:51');
INSERT INTO `room` VALUES (17, 'B301', 'B', NULL, 2, 8, 3, 'maintenance', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-15 10:34:52');
INSERT INTO `room` VALUES (18, 'B302', 'B', NULL, 2, 8, 3, 'empty_clean', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-15 10:34:53');
INSERT INTO `room` VALUES (19, 'B401', 'B', NULL, 2, 9, 4, 'occupied_clean', '吴九', 'F007', '2024-01-15 13:00:00', '2024-01-20 12:00:00', NULL, '2026-05-06 23:03:17', '2026-05-15 10:34:54');
INSERT INTO `room` VALUES (20, 'B501', 'B', NULL, 2, 10, 5, 'self_use', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-15 10:34:55');
INSERT INTO `room` VALUES (21, 'C101', 'C', NULL, 3, 11, 1, 'empty_clean', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-15 10:34:57');
INSERT INTO `room` VALUES (22, 'C102', 'C', NULL, 3, 11, 1, 'empty_clean', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-15 10:34:57');
INSERT INTO `room` VALUES (23, 'C201', 'C', NULL, 3, 12, 3, 'occupied_clean', '郑十', 'F008', '2024-01-15 15:00:00', '2024-01-18 12:00:00', NULL, '2026-05-06 23:03:17', '2026-05-15 10:34:59');
INSERT INTO `room` VALUES (24, 'C301', 'C', NULL, 3, 13, 4, 'dirty', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-31 19:09:26');
INSERT INTO `room` VALUES (25, 'D101', 'D', NULL, 4, 14, 6, 'empty_clean', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-15 10:35:02');
INSERT INTO `room` VALUES (26, 'D102', 'D', NULL, 4, 14, 6, 'occupied_clean', '钱十一', 'F009', '2024-01-15 10:00:00', '2024-01-16 12:00:00', NULL, '2026-05-06 23:03:17', '2026-05-15 10:35:03');
INSERT INTO `room` VALUES (27, 'D201', 'D', NULL, 4, 15, 7, 'empty_clean', NULL, NULL, NULL, NULL, NULL, '2026-05-06 23:03:17', '2026-05-15 10:35:04');

-- ----------------------------
-- Table structure for room_status
-- ----------------------------
DROP TABLE IF EXISTS `room_status`;
CREATE TABLE `room_status`  (
  `status_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '鐘舵?ID',
  `status_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '鐘舵?浠ｇ爜',
  `status_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '鐘舵?鍚嶇О',
  `status_color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '鐘舵?棰滆壊',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '鐘舵?鎻忚堪',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '鎺掑簭鍙',
  PRIMARY KEY (`status_id`) USING BTREE,
  UNIQUE INDEX `status_code`(`status_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '鎴块棿鐘舵?瀛楀吀琛' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room_status
-- ----------------------------
INSERT INTO `room_status` VALUES (1, 'available', '空闲', '#48bb78', '房间干净可入住', 0);
INSERT INTO `room_status` VALUES (2, 'occupied', '已入住', '#ed8936', '客人正在入住', 0);
INSERT INTO `room_status` VALUES (3, 'dirty', '脏房', '#fc8181', '待打扫', 0);
INSERT INTO `room_status` VALUES (4, 'maintenance', '维修', '#718096', '维修中', 0);
INSERT INTO `room_status` VALUES (5, 'booked', '已预订', '#9f7aea', '已被预订', 0);
INSERT INTO `room_status` VALUES (6, 'locked', '锁房', '#4a5568', '锁定状态', 0);
INSERT INTO `room_status` VALUES (7, 'empty_clean', '可入住', '#48bb78', NULL, 0);
INSERT INTO `room_status` VALUES (8, 'empty_dirty', '待打扫', '#fc8181', NULL, 0);
INSERT INTO `room_status` VALUES (9, 'free', '可入住', '#48bb78', NULL, 0);
INSERT INTO `room_status` VALUES (10, 'occupied_clean', '入住中', '#ed8936', NULL, 0);
INSERT INTO `room_status` VALUES (11, 'occupied_dirty', '入住中', '#ed8936', NULL, 0);
INSERT INTO `room_status` VALUES (12, 'self_use', '自用', '#805ad5', NULL, 0);

-- ----------------------------
-- Table structure for room_type
-- ----------------------------
DROP TABLE IF EXISTS `room_type`;
CREATE TABLE `room_type`  (
  `type_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '房型ID',
  `type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '房型名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '房型描述',
  `price` decimal(10, 2) NOT NULL COMMENT '房价',
  `bed_count` int(11) NULL DEFAULT 1 COMMENT '床位数',
  `max_guests` int(11) NULL DEFAULT 2 COMMENT '最大入住人数',
  `area` decimal(8, 2) NULL DEFAULT NULL COMMENT '面积',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '房型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room_type
-- ----------------------------
INSERT INTO `room_type` VALUES (1, '艺程大床房', '标准大床房，配备舒适大床', 299.00, 1, 2, 35.00, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `room_type` VALUES (2, '行政套房', '豪华套房，独立客厅', 599.00, 1, 2, 65.00, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `room_type` VALUES (3, '美程大床房', '高级大床房', 399.00, 1, 2, 40.00, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `room_type` VALUES (4, '美豪行政套房', '行政级套房', 799.00, 1, 3, 80.00, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `room_type` VALUES (5, '尊优行政房', '尊贵行政客房', 699.00, 1, 2, 55.00, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `room_type` VALUES (6, '亲子套房', '适合家庭入住', 499.00, 2, 4, 50.00, '2026-05-06 23:03:17', '2026-05-06 23:03:17');
INSERT INTO `room_type` VALUES (7, '优享亲子房', '高级亲子房', 599.00, 2, 4, 60.00, '2026-05-06 23:03:17', '2026-05-06 23:03:17');

-- ----------------------------
-- Table structure for user_roles
-- ----------------------------
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles`  (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  CONSTRAINT `user_roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `user_roles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_roles
-- ----------------------------
INSERT INTO `user_roles` VALUES (1, 1);
INSERT INTO `user_roles` VALUES (2, 2);
INSERT INTO `user_roles` VALUES (3, 4);
INSERT INTO `user_roles` VALUES (4, 6);
INSERT INTO `user_roles` VALUES (127, 8);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码（加密存储）',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 128 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin', '123456', '超级管理员', '13800000000', 'kasjdfkjsdkf@qsf.com', 'https://tse4-mm.cn.bing.net/th/id/OIP-C.VyPItnrS3EOJdvxfxkjDAAAAAA?w=224&h=220&c=7&r=0&o=7&dpr=2&pid=1.7&rm=3', 1, '2026-03-19 23:55:24', '::1', '2026-03-11 20:34:42', '2026-05-03 17:10:59', NULL);
INSERT INTO `users` VALUES (2, 'manager', '888888', '张经理', '13800000001', NULL, NULL, 1, '2026-03-19 22:38:12', '::1', '2026-03-11 20:34:42', '2026-03-19 22:38:12', NULL);
INSERT INTO `users` VALUES (3, 'frontdesk1', '$2a$10$YourHashedPasswordHere', '李前台', '13800000002', NULL, NULL, 1, NULL, NULL, '2026-03-11 20:34:42', '2026-03-11 20:34:42', NULL);
INSERT INTO `users` VALUES (4, 'housekeeping1', '$2a$10$YourHashedPasswordHere', '王保洁', '13800000003', NULL, NULL, 1, NULL, NULL, '2026-03-11 20:34:42', '2026-03-11 20:34:42', NULL);
INSERT INTO `users` VALUES (127, 'test', '123456', '', '123124123421', 'kqjwkejiwj@qq', '', 1, '2026-03-19 23:55:54', '::1', '2026-03-14 22:32:40', '2026-05-26 21:51:12', NULL);

-- ----------------------------
-- Procedure structure for GetMenuTree
-- ----------------------------
DROP PROCEDURE IF EXISTS `GetMenuTree`;
delimiter ;;
CREATE PROCEDURE `GetMenuTree`(IN parentId INT)
BEGIN
    CREATE TEMPORARY TABLE IF NOT EXISTS temp_menu (
        menu_id INT,
        parent_id INT,
        menu_name VARCHAR(100),
        path VARCHAR(255),
        component VARCHAR(255),
        icon VARCHAR(50),
        sort_order INT,
        status TINYINT,
        level INT
    );
    
    INSERT INTO temp_menu
    SELECT menu_id, parent_id, menu_name, path, component, icon, sort_order, status, 1
    FROM menus WHERE parent_id = parentId;
    
    SET @level = 1;
    WHILE ROW_COUNT() > 0 DO
        SET @level = @level + 1;
        INSERT INTO temp_menu
        SELECT m.menu_id, m.parent_id, m.menu_name, m.path, m.component, m.icon, m.sort_order, m.status, @level
        FROM menus m
        JOIN temp_menu t ON m.parent_id = t.menu_id
        WHERE t.level = @level - 1 AND m.menu_id NOT IN (SELECT menu_id FROM temp_menu);
    END WHILE;
    
    SELECT * FROM temp_menu ORDER BY level, parent_id, sort_order;
    DROP TEMPORARY TABLE IF EXISTS temp_menu;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
