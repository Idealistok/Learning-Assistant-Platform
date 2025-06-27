-- 智能学习助手平台数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS learning_assistant_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE learning_assistant_platform;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '加密密码',
    `email` VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '手机号',
    `role` ENUM('USER', 'ADMIN') DEFAULT 'USER' COMMENT '用户角色',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `status` ENUM('ACTIVE', 'INACTIVE', 'BANNED') DEFAULT 'ACTIVE' COMMENT '用户状态',
    `created_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_username` (`username`),
    INDEX `idx_email` (`email`),
    INDEX `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 学习资料表
CREATE TABLE IF NOT EXISTS `material` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(200) NOT NULL COMMENT '资料名称',
    `description` TEXT COMMENT '资料描述',
    `subject` VARCHAR(100) NOT NULL COMMENT '所属学科',
    `file_path` VARCHAR(500) NOT NULL COMMENT '文件存储路径',
    `file_type` VARCHAR(50) NOT NULL COMMENT '文件类型',
    `file_size` BIGINT NOT NULL COMMENT '文件大小(字节)',
    `upload_user_id` BIGINT NOT NULL COMMENT '上传用户ID',
    `download_count` INT DEFAULT 0 COMMENT '下载次数',
    `status` ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING' COMMENT '审核状态',
    `upload_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `updated_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`upload_user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX `idx_subject` (`subject`),
    INDEX `idx_upload_user` (`upload_user_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_upload_time` (`upload_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习资料表';

-- 问答会话表
CREATE TABLE IF NOT EXISTS `qa_session` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `question` TEXT NOT NULL COMMENT '问题内容',
    `answer` TEXT COMMENT '答案内容',
    `feedback` ENUM('SATISFIED', 'UNSATISFIED') COMMENT '用户反馈',
    `created_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问答会话表';

-- 学习记录表
CREATE TABLE IF NOT EXISTS `study_record` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `material_id` BIGINT NOT NULL COMMENT '学习资料ID',
    `duration` INT NOT NULL COMMENT '学习时长(秒)',
    `start_time` TIMESTAMP NOT NULL COMMENT '开始时间',
    `end_time` TIMESTAMP NOT NULL COMMENT '结束时间',
    `progress_percent` DECIMAL(5,2) DEFAULT 0.00 COMMENT '学习进度百分比',
    `created_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`material_id`) REFERENCES `material`(`id`) ON DELETE CASCADE,
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_material_id` (`material_id`),
    INDEX `idx_start_time` (`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习记录表';

-- 学习进度表
CREATE TABLE IF NOT EXISTS `progress` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `subject` VARCHAR(100) NOT NULL COMMENT '学科',
    `percent` DECIMAL(5,2) DEFAULT 0.00 COMMENT '学习进度百分比',
    `total_study_time` INT DEFAULT 0 COMMENT '总学习时长(分钟)',
    `goal_hours` INT DEFAULT 0 COMMENT '目标学习时长(小时)',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_subject` (`user_id`, `subject`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_subject` (`subject`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习进度表';

-- 系统日志表
CREATE TABLE IF NOT EXISTS `system_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT COMMENT '操作用户ID',
    `operation` VARCHAR(100) NOT NULL COMMENT '操作类型',
    `description` TEXT COMMENT '操作描述',
    `ip_address` VARCHAR(50) COMMENT 'IP地址',
    `user_agent` TEXT COMMENT '用户代理',
    `created_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE SET NULL,
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_operation` (`operation`),
    INDEX `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志表';

-- 插入默认管理员用户 (密码: admin123)
INSERT INTO `user` (`username`, `password`, `email`, `role`) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'admin@learning.com', 'ADMIN')
ON DUPLICATE KEY UPDATE `role` = 'ADMIN'; 