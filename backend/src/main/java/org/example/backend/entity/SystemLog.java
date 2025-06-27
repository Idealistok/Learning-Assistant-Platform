package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 操作用户，可为空（系统操作）

    @Column(nullable = false, length = 100)
    private String operation; // 操作类型

    @Column(columnDefinition = "TEXT")
    private String description; // 操作描述

    @Column(name = "ip_address", length = 50)
    private String ipAddress; // IP地址

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent; // 用户代理

    @CreationTimestamp
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    // 操作类型枚举
    public enum OperationType {
        // 用户相关
        USER_REGISTER("用户注册"),
        USER_LOGIN("用户登录"),
        USER_LOGOUT("用户登出"),
        USER_UPDATE("用户信息更新"),

        // 资料相关
        MATERIAL_UPLOAD("资料上传"),
        MATERIAL_DOWNLOAD("资料下载"),
        MATERIAL_DELETE("资料删除"),
        MATERIAL_APPROVE("资料审核通过"),
        MATERIAL_REJECT("资料审核拒绝"),

        // 问答相关
        QA_ASK("提问"),
        QA_ANSWER("回答"),
        QA_FEEDBACK("问答反馈"),

        // 学习相关
        STUDY_START("开始学习"),
        STUDY_END("结束学习"),
        PROGRESS_UPDATE("进度更新"),

        // 系统相关
        SYSTEM_ERROR("系统错误"),
        SYSTEM_WARNING("系统警告"),
        SYSTEM_INFO("系统信息");

        private final String description;

        OperationType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}