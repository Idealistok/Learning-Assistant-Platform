package org.example.backend.repository;

import org.example.backend.entity.Material;
import org.example.backend.entity.StudyRecord;
import org.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StudyRecordRepository extends JpaRepository<StudyRecord, Long> {

    /**
     * 根据用户查找学习记录
     */
    List<StudyRecord> findByUser(User user);

    /**
     * 根据用户分页查找学习记录
     */
    Page<StudyRecord> findByUser(User user, Pageable pageable);

    /**
     * 根据学习资料查找学习记录
     */
    List<StudyRecord> findByMaterial(Material material);

    /**
     * 根据学习资料分页查找学习记录
     */
    Page<StudyRecord> findByMaterial(Material material, Pageable pageable);

    /**
     * 根据用户和学习资料查找学习记录
     */
    List<StudyRecord> findByUserAndMaterial(User user, Material material);

    /**
     * 根据用户和学习资料分页查找学习记录
     */
    Page<StudyRecord> findByUserAndMaterial(User user, Material material, Pageable pageable);

    /**
     * 根据开始时间范围查找学习记录
     */
    List<StudyRecord> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据用户和开始时间范围查找学习记录
     */
    List<StudyRecord> findByUserAndStartTimeBetween(User user, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据学习时长范围查找学习记录
     */
    List<StudyRecord> findByDurationBetween(Integer minDuration, Integer maxDuration);

    /**
     * 根据用户和学习时长范围查找学习记录
     */
    List<StudyRecord> findByUserAndDurationBetween(User user, Integer minDuration, Integer maxDuration);

    /**
     * 根据学习进度范围查找学习记录
     */
    List<StudyRecord> findByProgressPercentBetween(BigDecimal minProgress, BigDecimal maxProgress);

    /**
     * 根据用户和学习进度范围查找学习记录
     */
    List<StudyRecord> findByUserAndProgressPercentBetween(User user, BigDecimal minProgress, BigDecimal maxProgress);

    /**
     * 查找用户最新的学习记录
     */
    @Query("SELECT s FROM StudyRecord s WHERE s.user = :user ORDER BY s.startTime DESC")
    List<StudyRecord> findLatestStudyRecordsByUser(@Param("user") User user, Pageable pageable);

    /**
     * 查找资料最新的学习记录
     */
    @Query("SELECT s FROM StudyRecord s WHERE s.material = :material ORDER BY s.startTime DESC")
    List<StudyRecord> findLatestStudyRecordsByMaterial(@Param("material") Material material, Pageable pageable);

    /**
     * 统计用户总学习时长（秒）
     */
    @Query("SELECT SUM(s.duration) FROM StudyRecord s WHERE s.user = :user")
    Long sumDurationByUser(@Param("user") User user);

    /**
     * 统计用户在指定时间范围内的总学习时长（秒）
     */
    @Query("SELECT SUM(s.duration) FROM StudyRecord s WHERE s.user = :user AND s.startTime BETWEEN :startTime AND :endTime")
    Long sumDurationByUserAndTimeRange(
            @Param("user") User user,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 统计资料的总学习时长（秒）
     */
    @Query("SELECT SUM(s.duration) FROM StudyRecord s WHERE s.material = :material")
    Long sumDurationByMaterial(@Param("material") Material material);

    /**
     * 统计用户学习资料数量
     */
    @Query("SELECT COUNT(DISTINCT s.material) FROM StudyRecord s WHERE s.user = :user")
    Long countDistinctMaterialsByUser(@Param("user") User user);

    /**
     * 统计学习资料的学习人数
     */
    @Query("SELECT COUNT(DISTINCT s.user) FROM StudyRecord s WHERE s.material = :material")
    Long countDistinctUsersByMaterial(@Param("material") Material material);

    /**
     * 统计用户平均学习时长
     */
    @Query("SELECT AVG(s.duration) FROM StudyRecord s WHERE s.user = :user")
    Double avgDurationByUser(@Param("user") User user);

    /**
     * 统计资料平均学习时长
     */
    @Query("SELECT AVG(s.duration) FROM StudyRecord s WHERE s.material = :material")
    Double avgDurationByMaterial(@Param("material") Material material);

    /**
     * 统计用户平均学习进度
     */
    @Query("SELECT AVG(s.progressPercent) FROM StudyRecord s WHERE s.user = :user")
    BigDecimal avgProgressByUser(@Param("user") User user);

    /**
     * 统计资料平均学习进度
     */
    @Query("SELECT AVG(s.progressPercent) FROM StudyRecord s WHERE s.material = :material")
    BigDecimal avgProgressByMaterial(@Param("material") Material material);

    /**
     * 统计每日学习时长
     */
    @Query("SELECT DATE(s.startTime), SUM(s.duration) FROM StudyRecord s " +
            "WHERE s.user = :user AND s.startTime BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(s.startTime) ORDER BY DATE(s.startTime)")
    List<Object[]> sumDurationByUserAndDateRange(
            @Param("user") User user,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * 查找学习时长最长的记录
     */
    @Query("SELECT s FROM StudyRecord s ORDER BY s.duration DESC")
    List<StudyRecord> findTopDurationStudyRecords(Pageable pageable);

    /**
     * 查找用户学习时长最长的记录
     */
    @Query("SELECT s FROM StudyRecord s WHERE s.user = :user ORDER BY s.duration DESC")
    List<StudyRecord> findTopDurationStudyRecordsByUser(@Param("user") User user, Pageable pageable);

    /**
     * 查找学习进度最高的记录
     */
    @Query("SELECT s FROM StudyRecord s ORDER BY s.progressPercent DESC")
    List<StudyRecord> findTopProgressStudyRecords(Pageable pageable);

    /**
     * 查找用户学习进度最高的记录
     */
    @Query("SELECT s FROM StudyRecord s WHERE s.user = :user ORDER BY s.progressPercent DESC")
    List<StudyRecord> findTopProgressStudyRecordsByUser(@Param("user") User user, Pageable pageable);
}