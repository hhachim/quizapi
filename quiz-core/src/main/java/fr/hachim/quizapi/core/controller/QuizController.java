package fr.hachim.quizapi.core.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.hachim.quizapi.core.dto.ApiResponse;
import fr.hachim.quizapi.core.dto.PageResponse;
import fr.hachim.quizapi.core.dto.QuizCreationDTO;
import fr.hachim.quizapi.core.dto.QuizDTO;
import fr.hachim.quizapi.core.exception.BusinessException;
import fr.hachim.quizapi.core.exception.ResourceNotFoundException;
import fr.hachim.quizapi.core.mapper.QuizMapper;
import fr.hachim.quizapi.core.model.Category;
import fr.hachim.quizapi.core.model.Quiz;
import fr.hachim.quizapi.core.model.Tag;
import fr.hachim.quizapi.core.service.CategoryService;
import fr.hachim.quizapi.core.service.QuizService;
import fr.hachim.quizapi.core.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Contrôleur REST pour gérer les opérations sur les quiz.
 */
@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final QuizMapper quizMapper;
    
    /**
     * Récupère tous les quiz, avec pagination.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<QuizDTO>>> getAllQuizzes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        Page<Quiz> quizzesPage = quizService.findAllQuizzes(pageable);
        Page<QuizDTO> quizDTOsPage = quizzesPage.map(quiz -> {
            List<Tag> tags = tagService.findTagsByQuizId(quiz.getId());
            return quizMapper.toDTOWithTags(quiz, tags.stream().collect(Collectors.toSet()));
        });
        
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(quizDTOsPage), 
                "Quiz récupérés avec succès"));
    }
    
    /**
     * Récupère un quiz par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizDTO>> getQuizById(@PathVariable UUID id) {
        return quizService.findQuizById(id)
                .map(quiz -> {
                    List<Tag> tags = tagService.findTagsByQuizId(quiz.getId());
                    return ResponseEntity.ok(ApiResponse.success(
                            quizMapper.toDTOWithTags(quiz, tags.stream().collect(Collectors.toSet())), 
                            "Quiz récupéré avec succès"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Quiz non trouvé")));
    }
    
    /**
     * Recherche de quiz par critères multiples.
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<QuizDTO>>> searchQuizzes(
            @RequestParam(required = false) String term,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) String difficultyLevel,
            @RequestParam(required = false) Boolean isPublic,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        Page<Quiz> quizzesPage = quizService.searchQuizzes(term, categoryId, difficultyLevel, isPublic, pageable);
        Page<QuizDTO> quizDTOsPage = quizzesPage.map(quiz -> {
            List<Tag> tags = tagService.findTagsByQuizId(quiz.getId());
            return quizMapper.toDTOWithTags(quiz, tags.stream().collect(Collectors.toSet()));
        });
        
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(quizDTOsPage), 
                "Recherche de quiz effectuée avec succès"));
    }
    
    /**
     * Récupère les quiz populaires.
     */
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<QuizDTO>>> getPopularQuizzes(
            @RequestParam(defaultValue = "10") int limit) {
        
        List<Quiz> popularQuizzes = quizService.findPopularQuizzes(limit);
        List<QuizDTO> quizDTOs = popularQuizzes.stream()
                .map(quiz -> {
                    List<Tag> tags = tagService.findTagsByQuizId(quiz.getId());
                    return quizMapper.toDTOWithTags(quiz, tags.stream().collect(Collectors.toSet()));
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(quizDTOs, 
                "Quiz populaires récupérés avec succès"));
    }
    
    /**
     * Crée un nouveau quiz.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<QuizDTO>> createQuiz(@Valid @RequestBody QuizCreationDTO quizDTO) {
        Category category = null;
        if (quizDTO.getCategoryId() != null) {
            category = categoryService.findCategoryById(quizDTO.getCategoryId()).orElse(null);
        }
        
        Quiz quiz = quizMapper.toEntity(quizDTO, category);
        Quiz savedQuiz = quizService.createQuiz(quiz);
        
        // Associer les tags si spécifiés
        if (quizDTO.getTagIds() != null && !quizDTO.getTagIds().isEmpty()) {
            quizService.updateQuizTags(savedQuiz.getId(), quizDTO.getTagIds());
        }
        
        List<Tag> tags = tagService.findTagsByQuizId(savedQuiz.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        quizMapper.toDTOWithTags(savedQuiz, tags.stream().collect(Collectors.toSet())), 
                        "Quiz créé avec succès"));
    }
    
    /**
     * Met à jour un quiz existant.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizDTO>> updateQuiz(
            @PathVariable UUID id, 
            @RequestBody QuizCreationDTO quizDTO) {
        
        return quizService.findQuizById(id)
                .map(quiz -> {
                    Category category = null;
                    if (quizDTO.getCategoryId() != null) {
                        category = categoryService.findCategoryById(quizDTO.getCategoryId()).orElse(null);
                    }
                    
                    quizMapper.updateFromDTO(quiz, quizDTO, category);
                    Quiz updatedQuiz = quizService.updateQuiz(quiz);
                    
                    // Mettre à jour les tags si spécifiés
                    if (quizDTO.getTagIds() != null) {
                        quizService.updateQuizTags(updatedQuiz.getId(), quizDTO.getTagIds());
                    }
                    
                    List<Tag> tags = tagService.findTagsByQuizId(updatedQuiz.getId());
                    
                    return ResponseEntity.ok(ApiResponse.success(
                            quizMapper.toDTOWithTags(updatedQuiz, tags.stream().collect(Collectors.toSet())), 
                            "Quiz mis à jour avec succès"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Quiz non trouvé")));
    }
    
    /**
     * Change le statut d'un quiz.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<QuizDTO>> updateQuizStatus(
            @PathVariable UUID id, 
            @RequestParam String status) {
        
        if (!List.of("DRAFT", "PUBLISHED", "ARCHIVED", "REVIEWING").contains(status)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Statut invalide. Valeurs autorisées: DRAFT, PUBLISHED, ARCHIVED, REVIEWING"));
        }
        
        return quizService.findQuizById(id)
                .map(quiz -> {
                    Quiz updatedQuiz = quizService.updateQuizStatus(quiz, status);
                    List<Tag> tags = tagService.findTagsByQuizId(updatedQuiz.getId());
                    
                    return ResponseEntity.ok(ApiResponse.success(
                            quizMapper.toDTOWithTags(updatedQuiz, tags.stream().collect(Collectors.toSet())), 
                            "Statut du quiz mis à jour avec succès"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Quiz non trouvé")));
    }
    
    /**
     * Supprime un quiz (soft delete).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuiz(@PathVariable UUID id) {
        boolean deleted = quizService.deleteQuiz(id);
        
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success(null, "Quiz supprimé avec succès"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Quiz non trouvé"));
        }
    }

    /**
     * Publie un quiz.
     */
    @PostMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<QuizDTO>> publishQuiz(@PathVariable UUID id) {
        try {
            Quiz publishedQuiz = quizService.publishQuiz(id);
            List<Tag> tags = tagService.findTagsByQuizId(publishedQuiz.getId());
            
            return ResponseEntity.ok(ApiResponse.success(
                    quizMapper.toDTOWithTags(publishedQuiz, tags.stream().collect(Collectors.toSet())), 
                    "Quiz publié avec succès"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Archive un quiz.
     */
    @PostMapping("/{id}/archive")
    public ResponseEntity<ApiResponse<QuizDTO>> archiveQuiz(@PathVariable UUID id) {
        try {
            Quiz archivedQuiz = quizService.archiveQuiz(id);
            List<Tag> tags = tagService.findTagsByQuizId(archivedQuiz.getId());
            
            return ResponseEntity.ok(ApiResponse.success(
                    quizMapper.toDTOWithTags(archivedQuiz, tags.stream().collect(Collectors.toSet())), 
                    "Quiz archivé avec succès"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}