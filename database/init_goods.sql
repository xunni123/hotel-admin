-- ============================================
-- Hotel Management System - Goods Table
-- Database: hotel_management
-- ============================================

SET NAMES utf8mb4;

-- Create goods table
CREATE TABLE IF NOT EXISTS goods (
    goods_id INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Goods ID',
    goods_code VARCHAR(50) NOT NULL COMMENT 'Goods Code',
    goods_name VARCHAR(100) NOT NULL COMMENT 'Goods Name',
    category VARCHAR(20) DEFAULT 'other' COMMENT 'Category: drink/food/daily/other',
    price DECIMAL(10, 2) DEFAULT 0.00 COMMENT 'Price',
    stock INT DEFAULT 0 COMMENT 'Stock Quantity',
    description TEXT COMMENT 'Description',
    status VARCHAR(20) DEFAULT 'on' COMMENT 'Status: on/off',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
    UNIQUE KEY uk_goods_code (goods_code),
    KEY idx_category (category),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Goods Table';

-- Insert sample data
INSERT INTO goods (goods_code, goods_name, category, price, stock, description, status) VALUES
('WATER001', 'Mineral Water', 'drink', 5.00, 100, '500ml Bottled Water', 'on'),
('COLA001', 'Cola', 'drink', 8.00, 80, '330ml Canned Cola', 'on'),
('SNACK001', 'Chips', 'food', 12.00, 50, 'Chips Snack Pack', 'on'),
('SHAMPOO001', 'Shampoo', 'daily', 25.00, 30, 'Travel Size Shampoo 50ml', 'on'),
('TOOTH001', 'Toothbrush', 'daily', 10.00, 60, 'Disposable Toothbrush Set', 'on'),
('WATER002', 'Spring Water', 'drink', 3.00, 120, '550ml Spring Water', 'on'),
('JUICE001', 'Orange Juice', 'drink', 15.00, 40, '100% Orange Juice', 'off');

SELECT 'Goods table created successfully!' AS message;
