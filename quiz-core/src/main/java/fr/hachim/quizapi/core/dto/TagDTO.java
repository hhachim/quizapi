package fr.hachim.quizapi.core.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour transférer les données de tag vers et depuis l'API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    
    private UUID id;
    
    private String name;
    
    private LocalDateTime createdAt;
    
    private UUID createdBy;
    
    private Long quizCount; // Nombre de quiz utilisant ce tag (optionnel)
}