-- 创建公告表
CREATE TABLE IF NOT EXISTS announcements (
    announcement_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '公告ID',
    title VARCHAR(100) NOT NULL COMMENT '公告标题',
    content TEXT NOT NULL COMMENT '公告内容',
    author VARCHAR(50) COMMENT '作者',
    status VARCHAR(20) DEFAULT 'active' COMMENT '状态 active/inactive',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- 插入示例数据
INSERT INTO announcements (title, content, author, status) VALUES
('系统上线通知', '酒店管理系统正式上线！欢迎使用！', '系统管理员', 'active'),
('保洁工作规范', '请各位保洁员按照最新规范进行清洁工作', '行政部', 'active');
