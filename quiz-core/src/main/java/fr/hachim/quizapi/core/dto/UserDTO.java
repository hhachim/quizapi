package fr.hachim.quizapi.core.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour transférer les données d'utilisateur vers et depuis l'API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    private UUID id;
    
    private String username;
    
    private String email;
    
    private String firstName;
    
    private String lastName;
    
    private Boolean isActive;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime lastLoginAt;
    
    // Champ calculé à partir des propriétés firstName et lastName
    private String fullName;
    
    // Ne pas inclure le mot de passe dans les réponses
}