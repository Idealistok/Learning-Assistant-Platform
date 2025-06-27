# 实体类设计说明

## 概述
本包包含智能学习助手平台的所有JPA实体类，用于映射数据库表结构。

## 实体类列表

### 1. User.java - 用户实体
- **表名**: `user`
- **功能**: 存储用户基本信息
- **主要字段**:
  - `id`: 主键
  - `username`: 用户名（唯一）
  - `password`: 加密密码
  - `email`: 邮箱（唯一）
  - `phone`: 手机号
  - `role`: 用户角色（USER/ADMIN）
  - `status`: 用户状态（ACTIVE/INACTIVE/BANNED）

### 2. Material.java - 学习资料实体
- **表名**: `material`
- **功能**: 存储学习资料信息
- **主要字段**:
  - `id`: 主键
  - `name`: 资料名称
  - `description`: 资料描述
  - `subject`: 所属学科
  - `filePath`: 文件存储路径
  - `fileType`: 文件类型
  - `fileSize`: 文件大小
  - `uploadUser`: 上传用户（外键）
  - `downloadCount`: 下载次数
  - `status`: 审核状态（PENDING/APPROVED/REJECTED）

### 3. QASession.java - 问答会话实体
- **表名**: `qa_session`
- **功能**: 存储用户问答记录
- **主要字段**:
  - `id`: 主键
  - `user`: 用户（外键）
  - `question`: 问题内容
  - `answer`: 答案内容
  - `feedback`: 用户反馈（SATISFIED/UNSATISFIED）
  - `createdTime`: 创建时间

### 4. StudyRecord.java - 学习记录实体
- **表名**: `study_record`
- **功能**: 存储用户学习记录
- **主要字段**:
  - `id`: 主键
  - `user`: 用户（外键）
  - `material`: 学习资料（外键）
  - `duration`: 学习时长（秒）
  - `startTime`: 开始时间
  - `endTime`: 结束时间
  - `progressPercent`: 学习进度百分比

### 5. Progress.java - 学习进度实体
- **表名**: `progress`
- **功能**: 存储用户各学科学习进度
- **主要字段**:
  - `id`: 主键
  - `user`: 用户（外键）
  - `subject`: 学科
  - `percent`: 学习进度百分比
  - `totalStudyTime`: 总学习时长（分钟）
  - `goalHours`: 目标学习时长（小时）
  - **唯一约束**: 用户+学科组合唯一

### 6. SystemLog.java - 系统日志实体
- **表名**: `system_log`
- **功能**: 存储系统操作日志
- **主要字段**:
  - `id`: 主键
  - `user`: 操作用户（外键，可为空）
  - `operation`: 操作类型
  - `description`: 操作描述
  - `ipAddress`: IP地址
  - `userAgent`: 用户代理
  - `createdTime`: 创建时间

## 关系映射

### 一对多关系
- `User` → `Material` (一个用户可以上传多个资料)
- `User` → `QASession` (一个用户可以有多条问答记录)
- `User` → `StudyRecord` (一个用户可以有多条学习记录)
- `User` → `Progress` (一个用户可以有多个学科进度)
- `User` → `SystemLog` (一个用户可以有多条操作日志)

### 多对一关系
- `Material` → `User` (多个资料可以属于同一个用户)
- `StudyRecord` → `Material` (多条学习记录可以对应同一个资料)

## 注意事项

1. **懒加载**: 所有关联关系都使用 `FetchType.LAZY` 避免N+1查询问题
2. **时间戳**: 使用 `@CreationTimestamp` 和 `@UpdateTimestamp` 自动管理时间
3. **枚举类型**: 使用 `@Enumerated(EnumType.STRING)` 存储枚举值
4. **唯一约束**: 用户名、邮箱等字段设置了唯一约束
5. **复合唯一**: 学习进度表设置了用户+学科的复合唯一约束

## 扩展建议

1. **索引优化**: 根据查询需求添加适当的数据库索引
2. **软删除**: 考虑为重要实体添加软删除功能
3. **审计功能**: 可以扩展为支持JPA审计功能
4. **缓存策略**: 对于频繁查询的实体考虑添加缓存 