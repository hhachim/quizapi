package fr.hachim.quizapi.core.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
    private Long quizId;
    
    @Id
    @Column(name = "tag_id")
    private Long tagId;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}