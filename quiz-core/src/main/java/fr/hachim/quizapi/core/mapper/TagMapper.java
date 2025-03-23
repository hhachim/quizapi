package fr.hachim.quizapi.core.mapper;

import org.springframework.stereotype.Component;

import fr.hachim.quizapi.core.dto.TagDTO;
import fr.hachim.quizapi.core.model.Tag;

/**
 * Mapper pour convertir entre les objets Tag et TagDTO.
 */
@Component
public class TagMapper {
    
    /**
     * Convertit une entité Tag en TagDTO.
     * 
     * @param tag L'entité à convertir
     * @return Le DTO correspondant
     */
    public TagDTO toDTO(Tag tag) {
        if (tag == null) {
            return null;
        }
        
        return TagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .createdAt(tag.getCreatedAt())
                .createdBy(tag.getCreatedBy())
                .build();
    }
    
    /**
     * Convertit une entité Tag en TagDTO avec le nombre de quiz associés.
     * 
     * @param tag L'entité à convertir
     * @param quizCount Le nombre de quiz utilisant ce tag
     * @return Le DTO correspondant
     */
    public TagDTO toDTOWithQuizCount(Tag tag, Long quizCount) {
        TagDTO dto = toDTO(tag);
        if (dto != null) {
            dto.setQuizCount(quizCount);
        }
        return dto;
    }
    
    /**
     * Convertit un TagDTO en entité Tag.
     * 
     * @param dto Le DTO à convertir
     * @return L'entité correspondante
     */
    public Tag toEntity(TagDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Tag tag = new Tag();
        tag.setName(dto.getName());
        
        return tag;
    }
    
    /**
     * Met à jour une entité Tag existante avec les valeurs d'un TagDTO.
     * 
     * @param tag L'entité à mettre à jour
     * @param dto Le DTO contenant les nouvelles valeurs
     */
    public void updateFromDTO(Tag tag, TagDTO dto) {
        if (tag == null || dto == null) {
            return;
        }
        
        if (dto.getName() != null) {
            tag.setName(dto.getName());
        }
    }
}