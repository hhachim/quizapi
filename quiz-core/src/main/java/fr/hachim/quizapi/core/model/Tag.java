package fr.hachim.quizapi.core.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

/**
 * Entité représentant un tag dans le système.
 */
@Data
@Entity
@Table(name = "tags")
public class Tag {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private UUID uuid;
    
    @Column(nullable = false, unique = true, length = 50)
    private String name;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "created_by")
    private Long createdBy;
    
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
     * Vérifie si le tag est supprimé.
     * 
     * @return true si le tag est supprimé, false sinon
     */
    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }
}