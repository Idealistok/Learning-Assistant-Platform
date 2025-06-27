package org.example.backend.repository;

import org.example.backend.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private QASessionRepository qaSessionRepository;

    @Autowired
    private StudyRecordRepository studyRecordRepository;

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Test
    public void testUserRepository() {
        System.out.println("=== 测试 UserRepository ===");

        // 创建测试用户
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setPhone("13800138000");
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.ACTIVE);

        // 保存用户
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId());
        System.out.println("用户保存成功，ID: " + savedUser.getId());

        // 测试查询方法
        Optional<User> foundUser = userRepository.findByUsername("testuser");
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
        System.out.println("根据用户名查询成功");

        // 测试邮箱查询
        Optional<User> foundByEmail = userRepository.findByEmail("test@example.com");
        assertTrue(foundByEmail.isPresent());
        System.out.println("根据邮箱查询成功");

        // 测试手机号查询
        Optional<User> foundByPhone = userRepository.findByPhone("13800138000");
        assertTrue(foundByPhone.isPresent());
        System.out.println("根据手机号查询成功");

        // 测试存在性检查
        assertTrue(userRepository.existsByUsername("testuser"));
        assertTrue(userRepository.existsByEmail("test@example.com"));
        System.out.println("存在性检查成功");

        // 测试角色查询
        List<User> users = userRepository.findByRole(User.Role.USER);
        assertFalse(users.isEmpty());
        System.out.println("根据角色查询成功，找到 " + users.size() + " 个用户");

        System.out.println("UserRepository 测试通过！\n");
    }

    @Test
    public void testMaterialRepository() {
        System.out.println("=== 测试 MaterialRepository ===");

        // 创建测试用户
        User user = new User();
        user.setUsername("materialuser");
        user.setEmail("material@example.com");
        user.setPassword("password123");
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.ACTIVE);
        User savedUser = userRepository.save(user);

        // 创建测试资料
        Material material = new Material();
        material.setName("Java编程基础");
        material.setDescription("Java编程入门教程");
        material.setSubject("计算机科学");
        material.setFilePath("/uploads/java-basic.pdf");
        material.setFileType("PDF");
        material.setFileSize(1024L);
        material.setUploadUser(savedUser);
        material.setStatus(Material.Status.PENDING);

        // 保存资料
        Material savedMaterial = materialRepository.save(material);
        assertNotNull(savedMaterial.getId());
        System.out.println("资料保存成功，ID: " + savedMaterial.getId());

        // 测试查询方法
        List<Material> materials = materialRepository.findByUploadUser(savedUser);
        assertFalse(materials.isEmpty());
        System.out.println("根据上传用户查询成功，找到 " + materials.size() + " 个资料");

        // 测试学科查询
        List<Material> subjectMaterials = materialRepository.findBySubject("计算机科学");
        assertFalse(subjectMaterials.isEmpty());
        System.out.println("根据学科查询成功");

        // 测试状态查询
        List<Material> pendingMaterials = materialRepository.findByStatus(Material.Status.PENDING);
        assertFalse(pendingMaterials.isEmpty());
        System.out.println("根据状态查询成功");

        // 测试模糊查询
        List<Material> nameMaterials = materialRepository.findByNameContainingIgnoreCase("Java");
        assertFalse(nameMaterials.isEmpty());
        System.out.println("名称模糊查询成功");

        // 测试分页查询
        Pageable pageable = PageRequest.of(0, 10);
        Page<Material> materialPage = materialRepository.findByUploadUser(savedUser, pageable);
        assertFalse(materialPage.getContent().isEmpty());
        System.out.println("分页查询成功，总页数: " + materialPage.getTotalPages());

        System.out.println("MaterialRepository 测试通过！\n");
    }

    @Test
    public void testQASessionRepository() {
        System.out.println("=== 测试 QASessionRepository ===");

        // 创建测试用户
        User user = new User();
        user.setUsername("qauser");
        user.setEmail("qa@example.com");
        user.setPassword("password123");
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.ACTIVE);
        User savedUser = userRepository.save(user);

        // 创建测试问答记录
        QASession qaSession = new QASession();
        qaSession.setUser(savedUser);
        qaSession.setQuestion("什么是Java？");
        qaSession.setAnswer("Java是一种面向对象的编程语言");
        qaSession.setFeedback(QASession.Feedback.SATISFIED);

        // 保存问答记录
        QASession savedQASession = qaSessionRepository.save(qaSession);
        assertNotNull(savedQASession.getId());
        System.out.println("问答记录保存成功，ID: " + savedQASession.getId());

        // 测试查询方法
        List<QASession> userQASessions = qaSessionRepository.findByUser(savedUser);
        assertFalse(userQASessions.isEmpty());
        System.out.println("根据用户查询问答记录成功，找到 " + userQASessions.size() + " 条记录");

        // 测试反馈查询
        List<QASession> satisfiedQASessions = qaSessionRepository.findByFeedback(QASession.Feedback.SATISFIED);
        assertFalse(satisfiedQASessions.isEmpty());
        System.out.println("根据反馈查询成功");

        // 测试问题模糊查询
        List<QASession> questionQASessions = qaSessionRepository.findByQuestionContainingIgnoreCase("Java");
        assertFalse(questionQASessions.isEmpty());
        System.out.println("问题模糊查询成功");

        // 测试分页查询
        Pageable pageable = PageRequest.of(0, 10);
        Page<QASession> qaPage = qaSessionRepository.findByUser(savedUser, pageable);
        assertFalse(qaPage.getContent().isEmpty());
        System.out.println("分页查询成功");

        System.out.println("QASessionRepository 测试通过！\n");
    }

    @Test
    public void testStudyRecordRepository() {
        System.out.println("=== 测试 StudyRecordRepository ===");

        // 创建测试用户和资料
        User user = new User();
        user.setUsername("studyuser");
        user.setEmail("study@example.com");
        user.setPassword("password123");
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.ACTIVE);
        User savedUser = userRepository.save(user);

        Material material = new Material();
        material.setName("数学基础");
        material.setDescription("高等数学基础");
        material.setSubject("数学");
        material.setFilePath("/uploads/math.pdf");
        material.setFileType("PDF");
        material.setFileSize(2048L);
        material.setUploadUser(savedUser);
        material.setStatus(Material.Status.APPROVED);
        Material savedMaterial = materialRepository.save(material);

        // 创建测试学习记录
        StudyRecord studyRecord = new StudyRecord();
        studyRecord.setUser(savedUser);
        studyRecord.setMaterial(savedMaterial);
        studyRecord.setDuration(3600); // 1小时
        studyRecord.setStartTime(LocalDateTime.now().minusHours(1));
        studyRecord.setEndTime(LocalDateTime.now());
        studyRecord.setProgressPercent(new BigDecimal("75.50"));

        // 保存学习记录
        StudyRecord savedStudyRecord = studyRecordRepository.save(studyRecord);
        assertNotNull(savedStudyRecord.getId());
        System.out.println("学习记录保存成功，ID: " + savedStudyRecord.getId());

        // 测试查询方法
        List<StudyRecord> userRecords = studyRecordRepository.findByUser(savedUser);
        assertFalse(userRecords.isEmpty());
        System.out.println("根据用户查询学习记录成功，找到 " + userRecords.size() + " 条记录");

        // 测试资料查询
        List<StudyRecord> materialRecords = studyRecordRepository.findByMaterial(savedMaterial);
        assertFalse(materialRecords.isEmpty());
        System.out.println("根据资料查询学习记录成功");

        // 测试时长范围查询
        List<StudyRecord> durationRecords = studyRecordRepository.findByDurationBetween(3000, 4000);
        assertFalse(durationRecords.isEmpty());
        System.out.println("根据时长范围查询成功");

        // 测试统计方法
        Long totalDuration = studyRecordRepository.sumDurationByUser(savedUser);
        assertNotNull(totalDuration);
        System.out.println("用户总学习时长: " + totalDuration + " 秒");

        // 测试平均时长
        Double avgDuration = studyRecordRepository.avgDurationByUser(savedUser);
        assertNotNull(avgDuration);
        System.out.println("用户平均学习时长: " + avgDuration + " 秒");

        System.out.println("StudyRecordRepository 测试通过！\n");
    }

    @Test
    public void testProgressRepository() {
        System.out.println("=== 测试 ProgressRepository ===");

        // 创建测试用户
        User user = new User();
        user.setUsername("progressuser");
        user.setEmail("progress@example.com");
        user.setPassword("password123");
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.ACTIVE);
        User savedUser = userRepository.save(user);

        // 创建测试学习进度
        Progress progress = new Progress();
        progress.setUser(savedUser);
        progress.setSubject("数学");
        progress.setPercent(new BigDecimal("85.25"));
        progress.setTotalStudyTime(120); // 2小时
        progress.setGoalHours(10);

        // 保存学习进度
        Progress savedProgress = progressRepository.save(progress);
        assertNotNull(savedProgress.getId());
        System.out.println("学习进度保存成功，ID: " + savedProgress.getId());

        // 测试查询方法
        List<Progress> userProgress = progressRepository.findByUser(savedUser);
        assertFalse(userProgress.isEmpty());
        System.out.println("根据用户查询学习进度成功，找到 " + userProgress.size() + " 条记录");

        // 测试用户和学科查询
        Optional<Progress> foundProgress = progressRepository.findByUserAndSubject(savedUser, "数学");
        assertTrue(foundProgress.isPresent());
        System.out.println("根据用户和学科查询成功");

        // 测试进度范围查询
        List<Progress> progressRange = progressRepository.findByPercentBetween(
                new BigDecimal("80.00"), new BigDecimal("90.00"));
        assertFalse(progressRange.isEmpty());
        System.out.println("根据进度范围查询成功");

        // 测试统计方法
        BigDecimal avgProgress = progressRepository.avgProgressByUser(savedUser);
        assertNotNull(avgProgress);
        System.out.println("用户平均学习进度: " + avgProgress + "%");

        Integer totalStudyTime = progressRepository.sumTotalStudyTimeByUser(savedUser);
        assertNotNull(totalStudyTime);
        System.out.println("用户总学习时长: " + totalStudyTime + " 分钟");

        System.out.println("ProgressRepository 测试通过！\n");
    }

    @Test
    public void testSystemLogRepository() {
        System.out.println("=== 测试 SystemLogRepository ===");

        // 创建测试用户
        User user = new User();
        user.setUsername("loguser");
        user.setEmail("log@example.com");
        user.setPassword("password123");
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.ACTIVE);
        User savedUser = userRepository.save(user);

        // 创建测试系统日志
        SystemLog systemLog = new SystemLog();
        systemLog.setUser(savedUser);
        systemLog.setOperation(SystemLog.OperationType.USER_LOGIN.name());
        systemLog.setDescription("用户登录成功");
        systemLog.setIpAddress("192.168.1.1");
        systemLog.setUserAgent("Mozilla/5.0");

        // 保存系统日志
        SystemLog savedSystemLog = systemLogRepository.save(systemLog);
        assertNotNull(savedSystemLog.getId());
        System.out.println("系统日志保存成功，ID: " + savedSystemLog.getId());

        // 测试查询方法
        List<SystemLog> userLogs = systemLogRepository.findByUser(savedUser);
        assertFalse(userLogs.isEmpty());
        System.out.println("根据用户查询系统日志成功，找到 " + userLogs.size() + " 条记录");

        // 测试操作类型查询
        List<SystemLog> operationLogs = systemLogRepository.findByOperation("USER_LOGIN");
        assertFalse(operationLogs.isEmpty());
        System.out.println("根据操作类型查询成功");

        // 测试IP地址查询
        List<SystemLog> ipLogs = systemLogRepository.findByIpAddress("192.168.1.1");
        assertFalse(ipLogs.isEmpty());
        System.out.println("根据IP地址查询成功");

        // 测试描述模糊查询
        List<SystemLog> descLogs = systemLogRepository.findByDescriptionContainingIgnoreCase("登录");
        assertFalse(descLogs.isEmpty());
        System.out.println("描述模糊查询成功");

        // 测试分页查询
        Pageable pageable = PageRequest.of(0, 10);
        Page<SystemLog> logPage = systemLogRepository.findByUser(savedUser, pageable);
        assertFalse(logPage.getContent().isEmpty());
        System.out.println("分页查询成功");

        System.out.println("SystemLogRepository 测试通过！\n");
    }

    @Test
    public void testRepositoryIntegration() {
        System.out.println("=== 测试 Repository 集成功能 ===");

        // 创建完整的测试数据链
        User user = new User();
        user.setUsername("integrationuser");
        user.setEmail("integration@example.com");
        user.setPassword("password123");
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.ACTIVE);
        User savedUser = userRepository.save(user);

        // 创建资料
        Material material = new Material();
        material.setName("集成测试资料");
        material.setDescription("用于集成测试的学习资料");
        material.setSubject("测试学科");
        material.setFilePath("/uploads/test.pdf");
        material.setFileType("PDF");
        material.setFileSize(1024L);
        material.setUploadUser(savedUser);
        material.setStatus(Material.Status.APPROVED);
        Material savedMaterial = materialRepository.save(material);

        // 创建学习记录
        StudyRecord studyRecord = new StudyRecord();
        studyRecord.setUser(savedUser);
        studyRecord.setMaterial(savedMaterial);
        studyRecord.setDuration(1800); // 30分钟
        studyRecord.setStartTime(LocalDateTime.now().minusMinutes(30));
        studyRecord.setEndTime(LocalDateTime.now());
        studyRecord.setProgressPercent(new BigDecimal("60.00"));
        studyRecordRepository.save(studyRecord);

        // 创建学习进度
        Progress progress = new Progress();
        progress.setUser(savedUser);
        progress.setSubject("测试学科");
        progress.setPercent(new BigDecimal("60.00"));
        progress.setTotalStudyTime(30);
        progress.setGoalHours(5);
        progressRepository.save(progress);

        // 创建问答记录
        QASession qaSession = new QASession();
        qaSession.setUser(savedUser);
        qaSession.setQuestion("这是一个测试问题？");
        qaSession.setAnswer("这是一个测试答案");
        qaSession.setFeedback(QASession.Feedback.SATISFIED);
        qaSessionRepository.save(qaSession);

        // 创建系统日志
        SystemLog systemLog = new SystemLog();
        systemLog.setUser(savedUser);
        systemLog.setOperation(SystemLog.OperationType.USER_LOGIN.name());
        systemLog.setDescription("集成测试用户登录");
        systemLog.setIpAddress("127.0.0.1");
        systemLogRepository.save(systemLog);

        // 验证数据完整性
        assertEquals(1, materialRepository.findByUploadUser(savedUser).size());
        assertEquals(1, studyRecordRepository.findByUser(savedUser).size());
        assertEquals(1, progressRepository.findByUser(savedUser).size());
        assertEquals(1, qaSessionRepository.findByUser(savedUser).size());
        assertEquals(1, systemLogRepository.findByUser(savedUser).size());

        System.out.println("集成测试通过！所有Repository功能正常！");
        System.out.println("- 用户: " + savedUser.getUsername());
        System.out.println("- 资料: " + materialRepository.findByUploadUser(savedUser).size() + " 个");
        System.out.println("- 学习记录: " + studyRecordRepository.findByUser(savedUser).size() + " 条");
        System.out.println("- 学习进度: " + progressRepository.findByUser(savedUser).size() + " 条");
        System.out.println("- 问答记录: " + qaSessionRepository.findByUser(savedUser).size() + " 条");
        System.out.println("- 系统日志: " + systemLogRepository.findByUser(savedUser).size() + " 条");
    }
}