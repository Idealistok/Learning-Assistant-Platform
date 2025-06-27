package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "progress", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "subject" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String subject; // 学科

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal percent = BigDecimal.ZERO; // 学习进度百分比

    @Column(name = "total_study_time", nullable = false)
    private Integer totalStudyTime = 0; // 总学习时长(分钟)

    @Column(name = "goal_hours", nullable = false)
    private Integer goalHours = 0; // 目标学习时长(小时)

    @UpdateTimestamp
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;
}