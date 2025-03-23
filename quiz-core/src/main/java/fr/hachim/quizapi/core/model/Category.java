package fr.hachim.quizapi.core.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

/**
 * Entité représentant une catégorie dans le système.
 */
@Data
@Entity
@Table(name = "categories")
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "parent_id")
    private UUID parentId;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "created_by")
    private UUID createdBy;
    
    @Column(name = "updated_by")
    private UUID updatedBy;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    /**
     * Méthode appelée avant la mise à jour pour mettre à jour le timestamp.
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Vérifie si cette catégorie est une catégorie racine.
     * 
     * @return true si c'est une catégorie racine, false sinon
     */
    @Transient
    public boolean isRootCategory() {
        return parentId == null;
    }
    
    /**
     * Vérifie si la catégorie est supprimée.
     * 
     * @return true si la catégorie est supprimée, false sinon
     */
    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }
}