package org.example.backend.repository;

import org.example.backend.entity.QASession;
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
public interface QASessionRepository extends JpaRepository<QASession, Long> {

    /**
     * 根据用户查找问答记录
     */
    List<QASession> findByUser(User user);

    /**
     * 根据用户分页查找问答记录
     */
    Page<QASession> findByUser(User user, Pageable pageable);

    /**
     * 根据用户和反馈查找问答记录
     */
    List<QASession> findByUserAndFeedback(User user, QASession.Feedback feedback);

    /**
     * 根据反馈查找问答记录
     */
    List<QASession> findByFeedback(QASession.Feedback feedback);

    /**
     * 根据反馈分页查找问答记录
     */
    Page<QASession> findByFeedback(QASession.Feedback feedback, Pageable pageable);

    /**
     * 根据创建时间范围查找问答记录
     */
    List<QASession> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据用户和创建时间范围查找问答记录
     */
    List<QASession> findByUserAndCreatedTimeBetween(User user, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据问题内容模糊查询
     */
    List<QASession> findByQuestionContainingIgnoreCase(String question);

    /**
     * 根据问题内容模糊查询并分页
     */
    Page<QASession> findByQuestionContainingIgnoreCase(String question, Pageable pageable);

    /**
     * 根据答案内容模糊查询
     */
    List<QASession> findByAnswerContainingIgnoreCase(String answer);

    /**
     * 根据问题或答案内容模糊查询
     */
    @Query("SELECT q FROM QASession q WHERE q.question LIKE %:keyword% OR q.answer LIKE %:keyword%")
    List<QASession> findByQuestionOrAnswerContainingIgnoreCase(@Param("keyword") String keyword);

    /**
     * 根据问题或答案内容模糊查询并分页
     */
    @Query("SELECT q FROM QASession q WHERE q.question LIKE %:keyword% OR q.answer LIKE %:keyword%")
    Page<QASession> findByQuestionOrAnswerContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据用户、问题或答案内容模糊查询
     */
    @Query("SELECT q FROM QASession q WHERE q.user = :user AND (q.question LIKE %:keyword% OR q.answer LIKE %:keyword%)")
    List<QASession> findByUserAndQuestionOrAnswerContainingIgnoreCase(
            @Param("user") User user,
            @Param("keyword") String keyword);

    /**
     * 查找最新的问答记录
     */
    @Query("SELECT q FROM QASession q ORDER BY q.createdTime DESC")
    List<QASession> findLatestQASessions(Pageable pageable);

    /**
     * 查找用户最新的问答记录
     */
    @Query("SELECT q FROM QASession q WHERE q.user = :user ORDER BY q.createdTime DESC")
    List<QASession> findLatestQASessionsByUser(@Param("user") User user, Pageable pageable);

    /**
     * 统计各反馈类型数量
     */
    @Query("SELECT q.feedback, COUNT(q) FROM QASession q GROUP BY q.feedback")
    List<Object[]> countByFeedback();

    /**
     * 统计用户各反馈类型数量
     */
    @Query("SELECT q.feedback, COUNT(q) FROM QASession q WHERE q.user = :user GROUP BY q.feedback")
    List<Object[]> countByFeedbackAndUser(@Param("user") User user);

    /**
     * 统计每日问答数量
     */
    @Query("SELECT DATE(q.createdTime), COUNT(q) FROM QASession q " +
            "WHERE q.createdTime BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(q.createdTime) ORDER BY DATE(q.createdTime)")
    List<Object[]> countByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * 统计用户每日问答数量
     */
    @Query("SELECT DATE(q.createdTime), COUNT(q) FROM QASession q " +
            "WHERE q.user = :user AND q.createdTime BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(q.createdTime) ORDER BY DATE(q.createdTime)")
    List<Object[]> countByUserAndDateRange(
            @Param("user") User user,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * 查找满意度最高的问答记录
     */
    @Query("SELECT q FROM QASession q WHERE q.feedback = 'SATISFIED' ORDER BY q.createdTime DESC")
    List<QASession> findSatisfiedQASessions(Pageable pageable);

    /**
     * 查找用户满意度最高的问答记录
     */
    @Query("SELECT q FROM QASession q WHERE q.user = :user AND q.feedback = 'SATISFIED' ORDER BY q.createdTime DESC")
    List<QASession> findSatisfiedQASessionsByUser(@Param("user") User user, Pageable pageable);
}