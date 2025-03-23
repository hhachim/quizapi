package fr.hachim.quizapi.core.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale.Category;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

/**
 * Entité représentant un quiz dans le système.
 */
@Data
@Entity
@Table(name = "quizzes")
public class Quiz {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private UUID uuid;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "difficulty_level")
    private String difficultyLevel;
    
    @Column(name = "time_limit")
    private Integer timeLimit;
    
    @Column(name = "passing_score", precision = 5, scale = 2)
    private BigDecimal passingScore;
    
    @Column(nullable = false)
    private String status = "DRAFT";
    
    @Column(name = "is_public")
    private Boolean isPublic = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "created_by", nullable = false)
    private Long createdBy;
    
    @Column(name = "updated_by")
    private Long updatedBy;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    /**
     * Méthode appelée avant la persistance pour initialiser l'UUID s'il n'est pas défini.
     */
    @PrePersist
    public void prePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }
    
    /**
     * Méthode appelée avant la mise à jour pour mettre à jour le timestamp.
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Vérifie si le quiz est publié.
     * 
     * @return true si le quiz est publié, false sinon
     */
    @Transient
    public boolean isPublished() {
        return "PUBLISHED".equals(status);
    }
    
    /**
     * Vérifie si le quiz est archivé.
     * 
     * @return true si le quiz est archivé, false sinon
     */
    @Transient
    public boolean isArchived() {
        return "ARCHIVED".equals(status);
    }
    
    /**
     * Vérifie si le quiz est en cours de révision.
     * 
     * @return true si le quiz est en révision, false sinon
     */
    @Transient
    public boolean isReviewing() {
        return "REVIEWING".equals(status);
    }
    
    /**
     * Vérifie si le quiz est un brouillon.
     * 
     * @return true si le quiz est un brouillon, false sinon
     */
    @Transient
    public boolean isDraft() {
        return "DRAFT".equals(status);
    }
    
    /**
     * Vérifie si le quiz est supprimé.
     * 
     * @return true si le quiz est supprimé, false sinon
     */
    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }
}