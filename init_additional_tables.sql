-- ============================================
-- Hotel Management System - Additional Tables
-- Database: hotel_management
-- ============================================

SET NAMES utf8mb4;

-- ============================================
-- 1. Member Consume Records Table
-- ============================================
CREATE TABLE IF NOT EXISTS member_consume (
    consume_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT,
    member_no VARCHAR(50),
    member_name VARCHAR(100),
    phone VARCHAR(20),
    type VARCHAR(20) DEFAULT 'room',
    amount DECIMAL(10, 2) DEFAULT 0.00,
    points_change INT DEFAULT 0,
    current_points INT DEFAULT 0,
    remark VARCHAR(500),
    operator VARCHAR(50),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_member_id (member_id),
    KEY idx_member_no (member_no),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO member_consume (member_id, member_no, member_name, phone, type, amount, points_change, current_points, remark, operator) VALUES
(1, 'M001', 'Zhang San', '13800138001', 'room', 299.00, 30, 1280, 'Room charge', 'Admin'),
(2, 'M002', 'Li Si', '13800138002', 'goods', 50.00, 5, 650, 'Mineral water', 'Admin'),
(1, 'M001', 'Zhang San', '13800138001', 'room', 399.00, 40, 1320, 'Room charge', 'Admin'),
(3, 'M003', 'Wang Wu', '13800138003', 'other', 100.00, 10, 210, 'SPA service', 'Admin'),
(2, 'M002', 'Li Si', '13800138002', 'room', 499.00, 50, 700, 'Room charge', 'Admin');

-- ============================================
-- 2. Operation Log Table
-- ============================================
CREATE TABLE IF NOT EXISTS operation_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(20) DEFAULT 'other',
    module VARCHAR(50),
    content VARCHAR(500),
    operator VARCHAR(50),
    operator_id INT,
    ip VARCHAR(50),
    params TEXT,
    result TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_type (type),
    KEY idx_module (module),
    KEY idx_operator (operator),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO operation_log (type, module, content, operator, operator_id, ip, params, result) VALUES
('login', 'system', 'User login', 'Admin', 1, '192.168.1.100', '{"username":"admin"}', 'success'),
('add', 'room', 'Add room', 'Admin', 1, '192.168.1.100', '{"roomNo":"101","roomType":"Standard"}', 'success'),
('edit', 'member', 'Edit member info', 'Admin', 1, '192.168.1.100', '{"memberId":1,"phone":"13800138001"}', 'success'),
('delete', 'notice', 'Delete notice', 'Admin', 1, '192.168.1.101', '{"noticeId":5}', 'success'),
('login', 'system', 'User login', 'Zhang San', 2, '192.168.1.102', '{"username":"zhangsan"}', 'success'),
('add', 'order', 'Create order', 'Zhang San', 2, '192.168.1.102', '{"roomId":1,"guestName":"Li Si"}', 'success'),
('logout', 'system', 'User logout', 'Zhang San', 2, '192.168.1.102', '', 'success');

-- ============================================
-- 3. Financial Record Table
-- ============================================
CREATE TABLE IF NOT EXISTS financial_record (
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(20) DEFAULT 'income',
    amount DECIMAL(10, 2) DEFAULT 0.00,
    payment_method VARCHAR(50),
    order_no VARCHAR(50),
    remark VARCHAR(500),
    operator VARCHAR(50),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_type (type),
    KEY idx_order_no (order_no),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO financial_record (type, amount, payment_method, order_no, remark, operator) VALUES
('income', 299.00, 'wechat', 'ORD20240501001', 'Room charge', 'Admin'),
('income', 598.00, 'alipay', 'ORD20240501002', 'Room charge', 'Admin'),
('income', 888.00, 'wechat', 'ORD20240502001', 'Room charge', 'Admin'),
('expense', 5000.00, 'bank', '', 'Monthly rent', 'Admin'),
('income', 1200.00, 'cash', 'ORD20240503001', 'Room charge', 'Zhang San'),
('income', 350.00, 'alipay', 'ORD20240503002', 'Room charge', 'Zhang San'),
('expense', 500.00, 'cash', '', 'Supplies', 'Admin');

-- Verification
SELECT 'Tables created!' AS message;
SELECT 'member_consume:', COUNT(*) FROM member_consume;
SELECT 'operation_log:', COUNT(*) FROM operation_log;
SELECT 'financial_record:', COUNT(*) FROM financial_record;
