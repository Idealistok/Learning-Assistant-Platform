package org.example.backend.repository;

import org.example.backend.entity.Material;
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
public interface MaterialRepository extends JpaRepository<Material, Long> {

    /**
     * 根据上传用户查找资料
     */
    List<Material> findByUploadUser(User uploadUser);

    /**
     * 根据上传用户分页查找资料
     */
    Page<Material> findByUploadUser(User uploadUser, Pageable pageable);

    /**
     * 根据学科查找资料
     */
    List<Material> findBySubject(String subject);

    /**
     * 根据学科分页查找资料
     */
    Page<Material> findBySubject(String subject, Pageable pageable);

    /**
     * 根据审核状态查找资料
     */
    List<Material> findByStatus(Material.Status status);

    /**
     * 根据审核状态分页查找资料
     */
    Page<Material> findByStatus(Material.Status status, Pageable pageable);

    /**
     * 根据文件类型查找资料
     */
    List<Material> findByFileType(String fileType);

    /**
     * 根据上传时间范围查找资料
     */
    List<Material> findByUploadTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据上传用户和审核状态查找资料
     */
    List<Material> findByUploadUserAndStatus(User uploadUser, Material.Status status);

    /**
     * 根据学科和审核状态查找资料
     */
    List<Material> findBySubjectAndStatus(String subject, Material.Status status);

    /**
     * 根据学科和审核状态分页查找资料
     */
    Page<Material> findBySubjectAndStatus(String subject, Material.Status status, Pageable pageable);

    /**
     * 根据名称模糊查询
     */
    List<Material> findByNameContainingIgnoreCase(String name);

    /**
     * 根据名称模糊查询并分页
     */
    Page<Material> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * 根据描述模糊查询
     */
    List<Material> findByDescriptionContainingIgnoreCase(String description);

    /**
     * 根据名称或描述模糊查询
     */
    @Query("SELECT m FROM Material m WHERE m.name LIKE %:keyword% OR m.description LIKE %:keyword%")
    List<Material> findByNameOrDescriptionContainingIgnoreCase(@Param("keyword") String keyword);

    /**
     * 根据名称或描述模糊查询并分页
     */
    @Query("SELECT m FROM Material m WHERE m.name LIKE %:keyword% OR m.description LIKE %:keyword%")
    Page<Material> findByNameOrDescriptionContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据名称、描述、学科模糊查询
     */
    @Query("SELECT m FROM Material m WHERE " +
            "(:name IS NULL OR m.name LIKE %:name%) AND " +
            "(:description IS NULL OR m.description LIKE %:description%) AND " +
            "(:subject IS NULL OR m.subject = :subject) AND " +
            "(:status IS NULL OR m.status = :status)")
    Page<Material> findByConditions(
            @Param("name") String name,
            @Param("description") String description,
            @Param("subject") String subject,
            @Param("status") Material.Status status,
            Pageable pageable);

    /**
     * 统计各学科资料数量
     */
    @Query("SELECT m.subject, COUNT(m) FROM Material m GROUP BY m.subject")
    List<Object[]> countBySubject();

    /**
     * 统计各状态资料数量
     */
    @Query("SELECT m.status, COUNT(m) FROM Material m GROUP BY m.status")
    List<Object[]> countByStatus();

    /**
     * 统计各文件类型数量
     */
    @Query("SELECT m.fileType, COUNT(m) FROM Material m GROUP BY m.fileType")
    List<Object[]> countByFileType();

    /**
     * 查找下载次数最多的资料
     */
    @Query("SELECT m FROM Material m WHERE m.status = 'APPROVED' ORDER BY m.downloadCount DESC")
    List<Material> findTopDownloadedMaterials(Pageable pageable);

    /**
     * 查找最新上传的资料
     */
    @Query("SELECT m FROM Material m WHERE m.status = 'APPROVED' ORDER BY m.uploadTime DESC")
    List<Material> findLatestMaterials(Pageable pageable);
}