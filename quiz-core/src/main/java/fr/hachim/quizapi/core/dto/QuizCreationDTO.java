package fr.hachim.quizapi.core.dto;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la création d'un nouveau quiz.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizCreationDTO {
    
    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 255, message = "Le titre doit contenir entre 3 et 255 caractères")
    private String title;
    
    private String description;
    
    @Pattern(regexp = "^(BEGINNER|EASY|MEDIUM|HARD|EXPERT)$", message = "Le niveau de difficulté doit être l'un des suivants : BEGINNER, EASY, MEDIUM, HARD, EXPERT")
    private String difficultyLevel;
    
    private Integer timeLimit; // En secondes
    
    @DecimalMin(value = "0.0", message = "Le score minimum pour réussir doit être supérieur ou égal à 0")
    @DecimalMax(value = "100.0", message = "Le score minimum pour réussir doit être inférieur ou égal à 100")
    private BigDecimal passingScore;
    
    private Boolean isPublic = false;
    
    private UUID categoryId;
    
    private Set<UUID> tagIds;
}