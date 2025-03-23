package fr.hachim.quizapi.core.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour transférer les données de catégorie vers et depuis l'API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    
    private UUID id;
    
    private String name;
    
    private String description;
    
    private UUID parentId;
    
    private LocalDateTime createdAt;
    
    private UUID createdBy;
    
    private Long quizCount; // Nombre de quiz dans cette catégorie (optionnel)
    
    private Boolean isRootCategory; // Indique si c'est une catégorie racine
}