package fr.hachim.quizapi.core.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import fr.hachim.quizapi.core.dto.UserCreationDTO;
import fr.hachim.quizapi.core.dto.UserDTO;
import fr.hachim.quizapi.core.model.User;
import lombok.RequiredArgsConstructor;

/**
 * Mapper pour convertir entre les objets User et UserDTO.
 */
@Component
@RequiredArgsConstructor
public class UserMapper {
    
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Convertit une entité User en UserDTO.
     * 
     * @param user L'entité à convertir
     * @return Le DTO correspondant
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .fullName(user.getFullName())
                .build();
    }
    
    /**
     * Convertit un UserCreationDTO en entité User.
     * 
     * @param dto Le DTO à convertir
     * @return L'entité correspondante
     */
    public User toEntity(UserCreationDTO dto) {
        if (dto == null) {
            return null;
        }
        
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setIsActive(true);
        
        return user;
    }
    
    /**
     * Met à jour une entité User existante avec les valeurs d'un UserDTO.
     * 
     * @param user L'entité à mettre à jour
     * @param dto Le DTO contenant les nouvelles valeurs
     */
    public void updateFromDTO(User user, UserDTO dto) {
        if (user == null || dto == null) {
            return;
        }
        
        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        
        if (dto.getIsActive() != null) {
            user.setIsActive(dto.getIsActive());
        }
    }
}