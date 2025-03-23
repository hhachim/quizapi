package fr.hachim.quizapi.core.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour transférer les données de quiz vers et depuis l'API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {
    
    private UUID id;
    
    private String title;
    
    private String description;
    
    private String difficultyLevel;
    
    private Integer timeLimit;
    
    private BigDecimal passingScore;
    
    private String status;
    
    private Boolean isPublic;
    
    private CategoryDTO category;
    
    private Set<TagDTO> tags;
    
    private LocalDateTime createdAt;
    
    private UUID createdBy;
    
    private String creatorUsername; // Nom d'utilisateur du créateur (optionnel)
    
    private Long questionCount; // Nombre de questions dans ce quiz (optionnel)
    
    private Long attemptCount; // Nombre de tentatives pour ce quiz (optionnel)
}