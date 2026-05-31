-- ============================================
-- 查看表结构并修复查询
-- ============================================

SET NAMES utf8mb4;

-- 1. 查看 building 表结构
DESCRIBE building;

-- 2. 查看 building 表数据
SELECT * FROM building;

-- 3. 获取楼栋列表（不使用 status 字段）
SELECT 
    building_id AS value,
    building_name AS label
FROM building 
ORDER BY building_id;

-- 4. 获取楼层列表
SELECT 
    f.floor_id AS value,
    CONCAT(b.building_name, '-', f.floor_name) AS label,
    f.floor_number,
    b.building_id
FROM floor f
JOIN building b ON f.building_id = b.building_id
ORDER BY b.building_id, f.floor_number;

-- 5. 获取房型列表
SELECT 
    type_id AS value,
    type_name AS label
FROM room_type 
ORDER BY sort_order;

-- 6. 获取房间状态列表
SELECT 
    status_code AS value,
    status_name AS label,
    status_color AS color
FROM room_status 
ORDER BY sort_order;

-- 7. 获取入住类型列表
SELECT 
    type_code AS value,
    type_name AS label
FROM check_in_type 
ORDER BY sort_order;

-- 8. 获取渠道列表
SELECT 
    channel_code AS value,
    channel_name AS label
FROM channel 
ORDER BY sort_order;
