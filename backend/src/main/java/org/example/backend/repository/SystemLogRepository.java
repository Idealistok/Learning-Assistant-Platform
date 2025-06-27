package org.example.backend.repository;

import org.example.backend.entity.SystemLog;
import org.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {

    /**
     * 根据操作用户查找日志
     */
    List<SystemLog> findByUser(User user);

    /**
     * 根据操作用户分页查找日志
     */
    Page<SystemLog> findByUser(User user, Pageable pageable);

    /**
     * 根据操作类型查找日志
     */
    List<SystemLog> findByOperation(String operation);

    /**
     * 根据操作类型分页查找日志
     */
    Page<SystemLog> findByOperation(String operation, Pageable pageable);

    /**
     * 根据用户和操作类型查找日志
     */
    List<SystemLog> findByUserAndOperation(User user, String operation);

    /**
     * 根据用户和操作类型分页查找日志
     */
    Page<SystemLog> findByUserAndOperation(User user, String operation, Pageable pageable);

    /**
     * 根据创建时间范围查找日志
     */
    List<SystemLog> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据创建时间范围分页查找日志
     */
    Page<SystemLog> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 根据用户和创建时间范围查找日志
     */
    List<SystemLog> findByUserAndCreatedTimeBetween(User user, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据IP地址查找日志
     */
    List<SystemLog> findByIpAddress(String ipAddress);

    /**
     * 根据IP地址分页查找日志
     */
    Page<SystemLog> findByIpAddress(String ipAddress, Pageable pageable);

    /**
     * 根据用户和IP地址查找日志
     */
    List<SystemLog> findByUserAndIpAddress(User user, String ipAddress);

    /**
     * 根据描述模糊查询
     */
    List<SystemLog> findByDescriptionContainingIgnoreCase(String description);

    /**
     * 根据描述模糊查询并分页
     */
    Page<SystemLog> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);

    /**
     * 根据用户和描述模糊查询
     */
    List<SystemLog> findByUserAndDescriptionContainingIgnoreCase(User user, String description);

    /**
     * 查找最新的日志记录
     */
    @Query("SELECT s FROM SystemLog s ORDER BY s.createdTime DESC")
    List<SystemLog> findLatestLogs(Pageable pageable);

    /**
     * 查找用户最新的日志记录
     */
    @Query("SELECT s FROM SystemLog s WHERE s.user = :user ORDER BY s.createdTime DESC")
    List<SystemLog> findLatestLogsByUser(@Param("user") User user, Pageable pageable);

    /**
     * 统计各操作类型数量
     */
    @Query("SELECT s.operation, COUNT(s) FROM SystemLog s GROUP BY s.operation ORDER BY COUNT(s) DESC")
    List<Object[]> countByOperation();

    /**
     * 统计用户各操作类型数量
     */
    @Query("SELECT s.operation, COUNT(s) FROM SystemLog s WHERE s.user = :user GROUP BY s.operation ORDER BY COUNT(s) DESC")
    List<Object[]> countByOperationAndUser(@Param("user") User user);

    /**
     * 统计每日操作数量
     */
    @Query("SELECT DATE(s.createdTime), COUNT(s) FROM SystemLog s " +
            "WHERE s.createdTime BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(s.createdTime) ORDER BY DATE(s.createdTime)")
    List<Object[]> countByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * 统计用户每日操作数量
     */
    @Query("SELECT DATE(s.createdTime), COUNT(s) FROM SystemLog s " +
            "WHERE s.user = :user AND s.createdTime BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(s.createdTime) ORDER BY DATE(s.createdTime)")
    List<Object[]> countByUserAndDateRange(
            @Param("user") User user,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * 统计各IP地址操作数量
     */
    @Query("SELECT s.ipAddress, COUNT(s) FROM SystemLog s WHERE s.ipAddress IS NOT NULL GROUP BY s.ipAddress ORDER BY COUNT(s) DESC")
    List<Object[]> countByIpAddress();

    /**
     * 查找错误日志
     */
    @Query("SELECT s FROM SystemLog s WHERE s.operation LIKE '%ERROR%' ORDER BY s.createdTime DESC")
    List<SystemLog> findErrorLogs(Pageable pageable);

    /**
     * 查找用户错误日志
     */
    @Query("SELECT s FROM SystemLog s WHERE s.user = :user AND s.operation LIKE '%ERROR%' ORDER BY s.createdTime DESC")
    List<SystemLog> findErrorLogsByUser(@Param("user") User user, Pageable pageable);

    /**
     * 查找警告日志
     */
    @Query("SELECT s FROM SystemLog s WHERE s.operation LIKE '%WARNING%' ORDER BY s.createdTime DESC")
    List<SystemLog> findWarningLogs(Pageable pageable);

    /**
     * 查找用户警告日志
     */
    @Query("SELECT s FROM SystemLog s WHERE s.user = :user AND s.operation LIKE '%WARNING%' ORDER BY s.createdTime DESC")
    List<SystemLog> findWarningLogsByUser(@Param("user") User user, Pageable pageable);

    /**
     * 查找登录日志
     */
    @Query("SELECT s FROM SystemLog s WHERE s.operation IN ('USER_LOGIN', 'USER_LOGOUT') ORDER BY s.createdTime DESC")
    List<SystemLog> findLoginLogs(Pageable pageable);

    /**
     * 查找用户登录日志
     */
    @Query("SELECT s FROM SystemLog s WHERE s.user = :user AND s.operation IN ('USER_LOGIN', 'USER_LOGOUT') ORDER BY s.createdTime DESC")
    List<SystemLog> findLoginLogsByUser(@Param("user") User user, Pageable pageable);

    /**
     * 查找资料相关操作日志
     */
    @Query("SELECT s FROM SystemLog s WHERE s.operation LIKE '%MATERIAL%' ORDER BY s.createdTime DESC")
    List<SystemLog> findMaterialOperationLogs(Pageable pageable);

    /**
     * 查找用户资料相关操作日志
     */
    @Query("SELECT s FROM SystemLog s WHERE s.user = :user AND s.operation LIKE '%MATERIAL%' ORDER BY s.createdTime DESC")
    List<SystemLog> findMaterialOperationLogsByUser(@Param("user") User user, Pageable pageable);
}