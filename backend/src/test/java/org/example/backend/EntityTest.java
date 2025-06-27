package org.example.backend;

import org.example.backend.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
public class EntityTest {

    @Test
    public void testUserEntity() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.ACTIVE);

        System.out.println("User entity created successfully: " + user.getUsername());
    }

    @Test
    public void testMaterialEntity() {
        Material material = new Material();
        material.setName("Test Material");
        material.setDescription("Test Description");
        material.setSubject("Mathematics");
        material.setFilePath("/uploads/test.pdf");
        material.setFileType("PDF");
        material.setFileSize(1024L);
        material.setStatus(Material.Status.PENDING);

        System.out.println("Material entity created successfully: " + material.getName());
    }

    @Test
    public void testQASessionEntity() {
        QASession qaSession = new QASession();
        qaSession.setQuestion("What is Java?");
        qaSession.setAnswer("Java is a programming language");
        qaSession.setFeedback(QASession.Feedback.SATISFIED);

        System.out.println("QASession entity created successfully");
    }

    @Test
    public void testStudyRecordEntity() {
        StudyRecord studyRecord = new StudyRecord();
        studyRecord.setDuration(3600); // 1 hour
        studyRecord.setStartTime(LocalDateTime.now());
        studyRecord.setEndTime(LocalDateTime.now().plusHours(1));
        studyRecord.setProgressPercent(new BigDecimal("75.50"));

        System.out.println("StudyRecord entity created successfully");
    }

    @Test
    public void testProgressEntity() {
        Progress progress = new Progress();
        progress.setSubject("Mathematics");
        progress.setPercent(new BigDecimal("85.25"));
        progress.setTotalStudyTime(120); // 2 hours
        progress.setGoalHours(10);

        System.out.println("Progress entity created successfully");
    }

    @Test
    public void testSystemLogEntity() {
        SystemLog systemLog = new SystemLog();
        systemLog.setOperation(SystemLog.OperationType.USER_LOGIN.name());
        systemLog.setDescription("User logged in successfully");
        systemLog.setIpAddress("192.168.1.1");
        systemLog.setUserAgent("Mozilla/5.0");

        System.out.println("SystemLog entity created successfully");
    }
}