-- ============================================
-- Hotel Management System - Add New Menus
-- Database: hotel_management
-- ============================================

SET NAMES utf8mb4;

-- Check existing menus first
SELECT menu_id, menu_name FROM menus ORDER BY menu_id;

-- ============================================
-- 1. Finance Module (Parent Menu)
-- ============================================
INSERT INTO menus (parent_id, menu_name, menu_key, menu_type, path, component, icon, sort_order, status, create_time)
VALUES (0, 'Finance', 'finance', 'M', '/finance', NULL, 'Money', 10, '1', NOW());

-- ============================================
-- 2. History Orders (under Finance)
-- ============================================
INSERT INTO menus (parent_id, menu_name, menu_key, menu_type, path, component, icon, sort_order, status, create_time)
VALUES
((SELECT MAX(menu_id) FROM menus), 'History Orders', 'historyOrder', 'C', '/finance/history-order', 'finance/historyOrder/index', 'Document', 1, '1', NOW());

-- ============================================
-- 3. Financial Report (under Finance)
-- ============================================
INSERT INTO menus (parent_id, menu_name, menu_key, menu_type, path, component, icon, sort_order, status, create_time)
VALUES
((SELECT MAX(menu_id) FROM menus WHERE menu_name = 'Finance'), 'Financial Report', 'financialReport', 'C', '/finance/financial-report', 'finance/financialReport/index', 'DataLine', 2, '1', NOW());

-- ============================================
-- 4. Member Consumption Module (Parent Menu)
-- ============================================
INSERT INTO menus (parent_id, menu_name, menu_key, menu_type, path, component, icon, sort_order, status, create_time)
VALUES (0, 'Member Consume', 'memberConsume', 'M', '/member-consume', NULL, 'Wallet', 11, '1', NOW());

-- ============================================
-- 5. Consumption Records (under Member Consume)
-- ============================================
INSERT INTO menus (parent_id, menu_name, menu_key, menu_type, path, component, icon, sort_order, status, create_time)
VALUES
((SELECT MAX(menu_id) FROM menus WHERE menu_name = 'Member Consume'), 'Consume Records', 'consumeRecord', 'C', '/member-consume/consume-record', 'memberConsume/consumeRecord/index', 'List', 1, '1', NOW());

-- ============================================
-- 6. Goods Inventory Module (Parent Menu)
-- ============================================
INSERT INTO menus (parent_id, menu_name, menu_key, menu_type, path, component, icon, sort_order, status, create_time)
VALUES (0, 'Goods Inventory', 'goodsInventory', 'M', '/goods-inventory', NULL, 'Goods', 12, '1', NOW());

-- ============================================
-- 7. Goods Management (under Goods Inventory)
-- ============================================
INSERT INTO menus (parent_id, menu_name, menu_key, menu_type, path, component, icon, sort_order, status, create_time)
VALUES
((SELECT MAX(menu_id) FROM menus WHERE menu_name = 'Goods Inventory'), 'Goods Management', 'goodsManage', 'C', '/goods-inventory/goods-manage', 'goodsInventory/goodsManage/index', 'Box', 1, '1', NOW());

-- ============================================
-- 8. Check System menu exists, if not create it
-- ============================================
INSERT INTO menus (parent_id, menu_name, menu_key, menu_type, path, component, icon, sort_order, status, create_time)
SELECT 0, 'System', 'system', 'M', '/system', NULL, 'Setting', 8, '1', NOW()
WHERE NOT EXISTS (SELECT 1 FROM menus WHERE menu_name = 'System');

-- ============================================
-- 9. Operation Log (under System)
-- ============================================
INSERT INTO menus (parent_id, menu_name, menu_key, menu_type, path, component, icon, sort_order, status, create_time)
VALUES
((SELECT menu_id FROM menus WHERE menu_name = 'System' LIMIT 1), 'Operation Log', 'operationLog', 'C', '/system/operation-log', 'system/operationLog/index', 'DocumentChecked', 4, '1', NOW());

-- ============================================
-- View final menus
-- ============================================
SELECT menu_id, parent_id, menu_name, menu_key, path, component FROM menus ORDER BY parent_id, sort_order;

SELECT 'Menu data added successfully! Please logout and login again to see new menus.' AS message;
