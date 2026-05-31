-- ============================================
-- 简化版查询语句（不依赖可能不存在的字段）
-- ============================================

SET NAMES utf8mb4;

-- 1. 获取楼栋列表
SELECT building_id AS value, building_name AS label FROM building ORDER BY building_id;

-- 2. 获取楼层列表
SELECT f.floor_id AS value, CONCAT(b.building_name, '-', f.floor_name) AS label FROM floor f JOIN building b ON f.building_id = b.building_id ORDER BY b.building_id, f.floor_number;

-- 3. 获取房型列表（使用 type_id 排序）
SELECT type_id AS value, type_name AS label FROM room_type ORDER BY type_id;

-- 4. 获取房间状态列表
SELECT status_code AS value, status_name AS label, status_color AS color FROM room_status ORDER BY status_id;

-- 5. 获取入住类型列表
SELECT type_code AS value, type_name AS label FROM check_in_type ORDER BY type_id;

-- 6. 获取渠道列表
SELECT channel_code AS value, channel_name AS label FROM channel ORDER BY channel_id;

-- 7. 获取房间完整信息
SELECT r.room_id, r.room_number, b.building_name AS building, f.floor_name AS floor, rt.type_name AS room_type, r.status, rs.status_name AS status_name, rs.status_color AS status_color, o.guest_name, o.check_in_date AS check_in_time, o.check_out_date AS check_out_time FROM room r LEFT JOIN building b ON r.building_id = b.building_id LEFT JOIN floor f ON r.floor_id = f.floor_id LEFT JOIN room_type rt ON r.type_id = rt.type_id LEFT JOIN room_status rs ON r.status = rs.status_code LEFT JOIN orders o ON r.room_id = o.room_id AND o.order_status IN ('checked_in', 'reserved') ORDER BY b.building_id, f.floor_number, r.room_number;
