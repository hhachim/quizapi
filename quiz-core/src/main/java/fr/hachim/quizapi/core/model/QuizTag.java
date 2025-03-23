package fr.hachim.quizapi.core.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entité représentant l'association entre un quiz et un tag.
 */
@Data
@Entity
@Table(name = "quiz_tags")
@IdClass(QuizTagId.class)
public class QuizTag {
    
    @Id
    @Column(name = "quiz_id")
    private UUID quizId;
    
    @Id
    @Column(name = "tag_id")
    private UUID tagId;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}