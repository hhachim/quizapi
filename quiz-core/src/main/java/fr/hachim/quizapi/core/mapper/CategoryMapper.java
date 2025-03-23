package fr.hachim.quizapi.core.mapper;

import org.springframework.stereotype.Component;

import fr.hachim.quizapi.core.dto.CategoryDTO;
import fr.hachim.quizapi.core.model.Category;

/**
 * Mapper pour convertir entre les objets Category et CategoryDTO.
 */
@Component
public class CategoryMapper {
    
    /**
     * Convertit une entité Category en CategoryDTO.
     * 
     * @param category L'entité à convertir
     * @return Le DTO correspondant
     */
    public CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }
        
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .parentId(category.getParentId())
                .createdAt(category.getCreatedAt())
                .createdBy(category.getCreatedBy())
                .isRootCategory(category.isRootCategory())
                .build();
    }
    
    /**
     * Convertit une entité Category en CategoryDTO avec le nombre de quiz associés.
     * 
     * @param category L'entité à convertir
     * @param quizCount Le nombre de quiz dans cette catégorie
     * @return Le DTO correspondant
     */
    public CategoryDTO toDTOWithQuizCount(Category category, Long quizCount) {
        CategoryDTO dto = toDTO(category);
        if (dto != null) {
            dto.setQuizCount(quizCount);
        }
        return dto;
    }
    
    /**
     * Convertit un CategoryDTO en entité Category.
     * 
     * @param dto Le DTO à convertir
     * @return L'entité correspondante
     */
    public Category toEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setParentId(dto.getParentId());
        
        return category;
    }
    
    /**
     * Met à jour une entité Category existante avec les valeurs d'un CategoryDTO.
     * 
     * @param category L'entité à mettre à jour
     * @param dto Le DTO contenant les nouvelles valeurs
     */
    public void updateFromDTO(Category category, CategoryDTO dto) {
        if (category == null || dto == null) {
            return;
        }
        
        if (dto.getName() != null) {
            category.setName(dto.getName());
        }
        
        if (dto.getDescription() != null) {
            category.setDescription(dto.getDescription());
        }
        
        if (dto.getParentId() != null) {
            category.setParentId(dto.getParentId());
        }
    }
}