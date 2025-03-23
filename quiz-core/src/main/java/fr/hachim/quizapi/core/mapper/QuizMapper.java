package fr.hachim.quizapi.core.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import fr.hachim.quizapi.core.dto.QuizCreationDTO;
import fr.hachim.quizapi.core.dto.QuizDTO;
import fr.hachim.quizapi.core.model.Category;
import fr.hachim.quizapi.core.model.Quiz;
import fr.hachim.quizapi.core.model.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Mapper pour convertir entre les objets Quiz et QuizDTO.
 */
@Component
@RequiredArgsConstructor
public class QuizMapper {
    
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    
    /**
     * Convertit une entité Quiz en QuizDTO.
     * 
     * @param quiz L'entité à convertir
     * @return Le DTO correspondant
     */
    public QuizDTO toDTO(Quiz quiz) {
        if (quiz == null) {
            return null;
        }
        
        QuizDTO.QuizDTOBuilder builder = QuizDTO.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .difficultyLevel(quiz.getDifficultyLevel())
                .timeLimit(quiz.getTimeLimit())
                .passingScore(quiz.getPassingScore())
                .status(quiz.getStatus())
                .isPublic(quiz.getIsPublic())
                .createdAt(quiz.getCreatedAt())
                .createdBy(quiz.getCreatedBy());
                
        if (quiz.getCategory() != null) {
            builder.category(categoryMapper.toDTO(quiz.getCategory()));
        }
        
        return builder.build();
    }
    
    /**
     * Convertit une entité Quiz en QuizDTO avec les tags associés.
     * 
     * @param quiz L'entité à convertir
     * @param tags Les tags associés au quiz
     * @return Le DTO correspondant
     */
    public QuizDTO toDTOWithTags(Quiz quiz, Set<Tag> tags) {
        QuizDTO dto = toDTO(quiz);
        if (dto != null && tags != null) {
            dto.setTags(tags.stream()
                    .map(tagMapper::toDTO)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }
    
    /**
     * Convertit un QuizCreationDTO en entité Quiz.
     * 
     * @param dto Le DTO à convertir
     * @param category La catégorie du quiz
     * @return L'entité correspondante
     */
    public Quiz toEntity(QuizCreationDTO dto, Category category) {
        if (dto == null) {
            return null;
        }
        
        Quiz quiz = new Quiz();
        quiz.setTitle(dto.getTitle());
        quiz.setDescription(dto.getDescription());
        quiz.setDifficultyLevel(dto.getDifficultyLevel());
        quiz.setTimeLimit(dto.getTimeLimit());
        quiz.setPassingScore(dto.getPassingScore());
        quiz.setIsPublic(dto.getIsPublic() != null ? dto.getIsPublic() : false);
        quiz.setStatus("DRAFT"); // Statut initial
        quiz.setCategory(category);
        
        return quiz;
    }
    
    /**
     * Met à jour une entité Quiz existante avec les valeurs d'un QuizDTO.
     * 
     * @param quiz L'entité à mettre à jour
     * @param dto Le DTO contenant les nouvelles valeurs
     * @param category La nouvelle catégorie (peut être null pour ne pas modifier)
     */
    public void updateFromDTO(Quiz quiz, QuizCreationDTO dto, Category category) {
        if (quiz == null || dto == null) {
            return;
        }
        
        if (dto.getTitle() != null) {
            quiz.setTitle(dto.getTitle());
        }
        
        if (dto.getDescription() != null) {
            quiz.setDescription(dto.getDescription());
        }
        
        if (dto.getDifficultyLevel() != null) {
            quiz.setDifficultyLevel(dto.getDifficultyLevel());
        }
        
        if (dto.getTimeLimit() != null) {
            quiz.setTimeLimit(dto.getTimeLimit());
        }
        
        if (dto.getPassingScore() != null) {
            quiz.setPassingScore(dto.getPassingScore());
        }
        
        if (dto.getIsPublic() != null) {
            quiz.setIsPublic(dto.getIsPublic());
        }
        
        if (category != null) {
            quiz.setCategory(category);
        }
    }
}