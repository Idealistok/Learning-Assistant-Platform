-- H2数据库初始化脚本（仅用于测试环境）

-- 用户表
CREATE TABLE IF NOT EXISTS "user" (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    role VARCHAR(10) DEFAULT 'USER',
    avatar VARCHAR(255),
    status VARCHAR(10) DEFAULT 'ACTIVE',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);
CREATE INDEX idx_username ON "user"(username);
CREATE INDEX idx_email ON "user"(email);
CREATE INDEX idx_phone ON "user"(phone);

-- 学习资料表
CREATE TABLE IF NOT EXISTS material (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description CLOB,
    subject VARCHAR(100) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    file_size BIGINT NOT NULL,
    upload_user_id BIGINT NOT NULL,
    download_count INT DEFAULT 0,
    status VARCHAR(10) DEFAULT 'PENDING',
    upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (upload_user_id) REFERENCES "user"(id)
);
CREATE INDEX idx_subject ON material(subject);
CREATE INDEX idx_upload_user ON material(upload_user_id);
CREATE INDEX idx_status ON material(status);
CREATE INDEX idx_upload_time ON material(upload_time);

-- 问答会话表
CREATE TABLE IF NOT EXISTS qa_session (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    question CLOB NOT NULL,
    answer CLOB,
    feedback VARCHAR(15),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (user_id) REFERENCES "user"(id)
);
CREATE INDEX idx_user_id_qa ON qa_session(user_id);
CREATE INDEX idx_created_time_qa ON qa_session(created_time);

-- 学习记录表
CREATE TABLE IF NOT EXISTS study_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    material_id BIGINT NOT NULL,
    duration INT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    progress_percent DECIMAL(5,2) DEFAULT 0.00,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (user_id) REFERENCES "user"(id),
    FOREIGN KEY (material_id) REFERENCES material(id)
);
CREATE INDEX idx_user_id_study ON study_record(user_id);
CREATE INDEX idx_material_id_study ON study_record(material_id);
CREATE INDEX idx_start_time_study ON study_record(start_time);

-- 学习进度表
CREATE TABLE IF NOT EXISTS progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subject VARCHAR(100) NOT NULL,
    percent DECIMAL(5,2) DEFAULT 0.00,
    total_study_time INT DEFAULT 0,
    goal_hours INT DEFAULT 0,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    UNIQUE (user_id, subject),
    FOREIGN KEY (user_id) REFERENCES "user"(id)
);
CREATE INDEX idx_user_id_progress ON progress(user_id);
CREATE INDEX idx_subject_progress ON progress(subject);

-- 系统日志表
CREATE TABLE IF NOT EXISTS system_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    operation VARCHAR(100) NOT NULL,
    description CLOB,
    ip_address VARCHAR(50),
    user_agent CLOB,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (user_id) REFERENCES "user"(id)
);
CREATE INDEX idx_user_id_log ON system_log(user_id);
CREATE INDEX idx_operation_log ON system_log(operation);
CREATE INDEX idx_created_time_log ON system_log(created_time);

-- 插入默认管理员用户（密码: admin123）
INSERT INTO "user" (username, password, email, role) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'admin@learning.com', 'ADMIN'); 