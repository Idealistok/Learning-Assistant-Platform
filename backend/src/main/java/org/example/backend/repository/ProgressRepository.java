package org.example.backend.repository;

import org.example.backend.entity.Progress;
import org.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {

    /**
     * 根据用户查找学习进度
     */
    List<Progress> findByUser(User user);

    /**
     * 根据用户和学科查找学习进度
     */
    Optional<Progress> findByUserAndSubject(User user, String subject);

    /**
     * 根据学科查找学习进度
     */
    List<Progress> findBySubject(String subject);

    /**
     * 根据学习进度范围查找
     */
    List<Progress> findByPercentBetween(BigDecimal minPercent, BigDecimal maxPercent);

    /**
     * 根据用户和学习进度范围查找
     */
    List<Progress> findByUserAndPercentBetween(User user, BigDecimal minPercent, BigDecimal maxPercent);

    /**
     * 根据总学习时长范围查找
     */
    List<Progress> findByTotalStudyTimeBetween(Integer minTime, Integer maxTime);

    /**
     * 根据用户和总学习时长范围查找
     */
    List<Progress> findByUserAndTotalStudyTimeBetween(User user, Integer minTime, Integer maxTime);

    /**
     * 根据目标学习时长范围查找
     */
    List<Progress> findByGoalHoursBetween(Integer minHours, Integer maxHours);

    /**
     * 根据用户和目标学习时长范围查找
     */
    List<Progress> findByUserAndGoalHoursBetween(User user, Integer minHours, Integer maxHours);

    /**
     * 查找学习进度最高的记录
     */
    @Query("SELECT p FROM Progress p ORDER BY p.percent DESC")
    List<Progress> findTopProgressRecords();

    /**
     * 查找用户学习进度最高的记录
     */
    @Query("SELECT p FROM Progress p WHERE p.user = :user ORDER BY p.percent DESC")
    List<Progress> findTopProgressRecordsByUser(@Param("user") User user);

    /**
     * 查找学习时长最长的记录
     */
    @Query("SELECT p FROM Progress p ORDER BY p.totalStudyTime DESC")
    List<Progress> findTopStudyTimeRecords();

    /**
     * 查找用户学习时长最长的记录
     */
    @Query("SELECT p FROM Progress p WHERE p.user = :user ORDER BY p.totalStudyTime DESC")
    List<Progress> findTopStudyTimeRecordsByUser(@Param("user") User user);

    /**
     * 查找目标完成度最高的记录（学习时长/目标时长）
     */
    @Query("SELECT p FROM Progress p WHERE p.goalHours > 0 ORDER BY (p.totalStudyTime / 60.0 / p.goalHours) DESC")
    List<Progress> findTopGoalCompletionRecords();

    /**
     * 查找用户目标完成度最高的记录
     */
    @Query("SELECT p FROM Progress p WHERE p.user = :user AND p.goalHours > 0 ORDER BY (p.totalStudyTime / 60.0 / p.goalHours) DESC")
    List<Progress> findTopGoalCompletionRecordsByUser(@Param("user") User user);

    /**
     * 统计用户平均学习进度
     */
    @Query("SELECT AVG(p.percent) FROM Progress p WHERE p.user = :user")
    BigDecimal avgProgressByUser(@Param("user") User user);

    /**
     * 统计学科平均学习进度
     */
    @Query("SELECT AVG(p.percent) FROM Progress p WHERE p.subject = :subject")
    BigDecimal avgProgressBySubject(@Param("subject") String subject);

    /**
     * 统计用户总学习时长（分钟）
     */
    @Query("SELECT SUM(p.totalStudyTime) FROM Progress p WHERE p.user = :user")
    Integer sumTotalStudyTimeByUser(@Param("user") User user);

    /**
     * 统计学科总学习时长（分钟）
     */
    @Query("SELECT SUM(p.totalStudyTime) FROM Progress p WHERE p.subject = :subject")
    Integer sumTotalStudyTimeBySubject(@Param("subject") String subject);

    /**
     * 统计各学科学习进度分布
     */
    @Query("SELECT p.subject, AVG(p.percent), COUNT(p) FROM Progress p GROUP BY p.subject ORDER BY AVG(p.percent) DESC")
    List<Object[]> getSubjectProgressStats();

    /**
     * 统计用户各学科学习进度
     */
    @Query("SELECT p.subject, p.percent, p.totalStudyTime FROM Progress p WHERE p.user = :user ORDER BY p.percent DESC")
    List<Object[]> getUserSubjectProgress(@Param("user") User user);

    /**
     * 统计学习进度分布（按百分比区间）
     */
    @Query("SELECT " +
            "CASE " +
            "  WHEN p.percent < 25 THEN '0-25%' " +
            "  WHEN p.percent < 50 THEN '25-50%' " +
            "  WHEN p.percent < 75 THEN '50-75%' " +
            "  ELSE '75-100%' " +
            "END as progressRange, " +
            "COUNT(p) " +
            "FROM Progress p GROUP BY " +
            "CASE " +
            "  WHEN p.percent < 25 THEN '0-25%' " +
            "  WHEN p.percent < 50 THEN '25-50%' " +
            "  WHEN p.percent < 75 THEN '50-75%' " +
            "  ELSE '75-100%' " +
            "END")
    List<Object[]> getProgressDistribution();

    /**
     * 统计用户学习进度分布
     */
    @Query("SELECT " +
            "CASE " +
            "  WHEN p.percent < 25 THEN '0-25%' " +
            "  WHEN p.percent < 50 THEN '25-50%' " +
            "  WHEN p.percent < 75 THEN '50-75%' " +
            "  ELSE '75-100%' " +
            "END as progressRange, " +
            "COUNT(p) " +
            "FROM Progress p WHERE p.user = :user GROUP BY " +
            "CASE " +
            "  WHEN p.percent < 25 THEN '0-25%' " +
            "  WHEN p.percent < 50 THEN '25-50%' " +
            "  WHEN p.percent < 75 THEN '50-75%' " +
            "  ELSE '75-100%' " +
            "END")
    List<Object[]> getUserProgressDistribution(@Param("user") User user);

    /**
     * 查找需要提醒的用户（学习进度低于50%且有目标）
     */
    @Query("SELECT p FROM Progress p WHERE p.percent < 50 AND p.goalHours > 0")
    List<Progress> findUsersNeedingReminder();

    /**
     * 查找用户需要提醒的学科
     */
    @Query("SELECT p FROM Progress p WHERE p.user = :user AND p.percent < 50 AND p.goalHours > 0")
    List<Progress> findUserSubjectsNeedingReminder(@Param("user") User user);
}