# Repository层设计说明

## 概述
本包包含智能学习助手平台的所有Repository接口，用于数据访问层操作。所有Repository都继承自`JpaRepository`，提供基础的CRUD操作和自定义查询方法。

## Repository接口列表

### 1. UserRepository.java - 用户数据访问
**功能**: 用户信息的增删改查操作

**主要方法**:
- `findByUsername(String username)` - 根据用户名查找用户
- `findByEmail(String email)` - 根据邮箱查找用户
- `findByPhone(String phone)` - 根据手机号查找用户
- `existsByUsername(String username)` - 检查用户名是否存在
- `findByUsernameOrEmail(String usernameOrEmail)` - 根据用户名或邮箱查找（登录用）
- `findByRole(User.Role role)` - 根据角色查找用户
- `findByStatus(User.Status status)` - 根据状态查找用户
- `countByRole()` - 统计各角色用户数量
- `countByStatus()` - 统计各状态用户数量

### 2. MaterialRepository.java - 学习资料数据访问
**功能**: 学习资料的管理和查询操作

**主要方法**:
- `findByUploadUser(User uploadUser)` - 根据上传用户查找资料
- `findBySubject(String subject)` - 根据学科查找资料
- `findByStatus(Material.Status status)` - 根据审核状态查找资料
- `findByNameContainingIgnoreCase(String name)` - 根据名称模糊查询
- `findByConditions()` - 多条件组合查询
- `findTopDownloadedMaterials()` - 查找下载次数最多的资料
- `findLatestMaterials()` - 查找最新上传的资料
- `countBySubject()` - 统计各学科资料数量
- `countByStatus()` - 统计各状态资料数量

### 3. QASessionRepository.java - 问答会话数据访问
**功能**: 智能问答记录的管理和查询

**主要方法**:
- `findByUser(User user)` - 根据用户查找问答记录
- `findByFeedback(QASession.Feedback feedback)` - 根据反馈查找记录
- `findByQuestionContainingIgnoreCase(String question)` - 根据问题内容模糊查询
- `findByQuestionOrAnswerContainingIgnoreCase(String keyword)` - 根据问题或答案模糊查询
- `findLatestQASessions()` - 查找最新的问答记录
- `countByFeedback()` - 统计各反馈类型数量
- `countByDateRange()` - 统计每日问答数量
- `findSatisfiedQASessions()` - 查找满意度最高的记录

### 4. StudyRecordRepository.java - 学习记录数据访问
**功能**: 用户学习记录的管理和统计分析

**主要方法**:
- `findByUser(User user)` - 根据用户查找学习记录
- `findByMaterial(Material material)` - 根据学习资料查找记录
- `findByUserAndMaterial(User user, Material material)` - 根据用户和资料查找
- `findByStartTimeBetween()` - 根据时间范围查找
- `findByDurationBetween()` - 根据学习时长范围查找
- `sumDurationByUser(User user)` - 统计用户总学习时长
- `avgDurationByUser(User user)` - 统计用户平均学习时长
- `avgProgressByUser(User user)` - 统计用户平均学习进度
- `countDistinctMaterialsByUser(User user)` - 统计用户学习资料数量
- `findTopDurationStudyRecords()` - 查找学习时长最长的记录
- `findTopProgressStudyRecords()` - 查找学习进度最高的记录

### 5. ProgressRepository.java - 学习进度数据访问
**功能**: 用户学习进度的管理和分析

**主要方法**:
- `findByUser(User user)` - 根据用户查找学习进度
- `findByUserAndSubject(User user, String subject)` - 根据用户和学科查找
- `findByPercentBetween()` - 根据学习进度范围查找
- `findTopProgressRecords()` - 查找学习进度最高的记录
- `findTopStudyTimeRecords()` - 查找学习时长最长的记录
- `findTopGoalCompletionRecords()` - 查找目标完成度最高的记录
- `avgProgressByUser(User user)` - 统计用户平均学习进度
- `sumTotalStudyTimeByUser(User user)` - 统计用户总学习时长
- `getSubjectProgressStats()` - 统计各学科学习进度分布
- `getProgressDistribution()` - 统计学习进度分布
- `findUsersNeedingReminder()` - 查找需要提醒的用户

### 6. SystemLogRepository.java - 系统日志数据访问
**功能**: 系统操作日志的记录和查询

**主要方法**:
- `findByUser(User user)` - 根据操作用户查找日志
- `findByOperation(String operation)` - 根据操作类型查找日志
- `findByCreatedTimeBetween()` - 根据时间范围查找日志
- `findByIpAddress(String ipAddress)` - 根据IP地址查找日志
- `findLatestLogs()` - 查找最新的日志记录
- `countByOperation()` - 统计各操作类型数量
- `countByDateRange()` - 统计每日操作数量
- `findErrorLogs()` - 查找错误日志
- `findWarningLogs()` - 查找警告日志
- `findLoginLogs()` - 查找登录日志
- `findMaterialOperationLogs()` - 查找资料相关操作日志

## 设计特点

### 1. 方法命名规范
- 使用Spring Data JPA的命名查询规范
- 方法名清晰表达查询意图
- 支持多种查询条件组合

### 2. 分页支持
- 所有列表查询都支持分页
- 使用`Pageable`参数进行分页控制
- 返回`Page<T>`类型支持分页信息

### 3. 自定义查询
- 使用`@Query`注解编写复杂查询
- 支持JPQL和原生SQL查询
- 参数绑定使用`@Param`注解

### 4. 统计分析
- 提供丰富的统计查询方法
- 支持分组统计和聚合函数
- 返回`List<Object[]>`用于复杂统计结果

### 5. 模糊查询
- 支持不区分大小写的模糊查询
- 使用`ContainingIgnoreCase`方法名
- 支持多字段组合模糊查询

## 使用示例

### 基础查询
```java
// 根据用户名查找用户
Optional<User> user = userRepository.findByUsername("testuser");

// 分页查询资料
Page<Material> materials = materialRepository.findBySubject("Mathematics", 
    PageRequest.of(0, 10, Sort.by("uploadTime").descending()));
```

### 复杂查询
```java
// 多条件组合查询
Page<Material> materials = materialRepository.findByConditions(
    "Java", "编程", "计算机科学", Material.Status.APPROVED, 
    PageRequest.of(0, 20));

// 统计查询
List<Object[]> stats = progressRepository.getSubjectProgressStats();
```

### 统计分析
```java
// 统计用户学习时长
Long totalDuration = studyRecordRepository.sumDurationByUser(user);

// 统计各学科进度
List<Object[]> subjectProgress = progressRepository.getUserSubjectProgress(user);
```

## 扩展建议

1. **缓存优化**: 为频繁查询的方法添加缓存注解
2. **索引优化**: 根据查询需求在数据库中添加相应索引
3. **批量操作**: 添加批量插入、更新、删除方法
4. **审计功能**: 扩展为支持JPA审计功能
5. **查询优化**: 使用`@EntityGraph`优化关联查询性能 