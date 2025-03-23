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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

/**
 * Entité représentant un utilisateur dans le système.
 */
@Data
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private UUID uuid;
    
    @Column(nullable = false, unique = true, length = 100)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    
    @Column(name = "first_name", length = 100)
    private String firstName;
    
    @Column(name = "last_name", length = 100)
    private String lastName;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
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
     * Retourne le nom complet de l'utilisateur.
     * 
     * @return Le nom complet
     */
    @Transient
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        
        if (firstName != null && !firstName.isEmpty()) {
            fullName.append(firstName);
        }
        
        if (lastName != null && !lastName.isEmpty()) {
            if (fullName.length() > 0) {
                fullName.append(" ");
            }
            fullName.append(lastName);
        }
        
        return fullName.length() > 0 ? fullName.toString() : username;
    }
    
    /**
     * Vérifie si l'utilisateur est supprimé.
     * 
     * @return true si l'utilisateur est supprimé, false sinon
     */
    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }
}