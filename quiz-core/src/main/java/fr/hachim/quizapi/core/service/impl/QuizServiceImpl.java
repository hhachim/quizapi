package fr.hachim.quizapi.core.service.impl;

import fr.hachim.quizapi.core.exception.BusinessException;
import fr.hachim.quizapi.core.exception.ResourceNotFoundException;
import fr.hachim.quizapi.core.model.Quiz;
import fr.hachim.quizapi.core.model.QuizTag;
import fr.hachim.quizapi.core.model.QuizTagId;
import fr.hachim.quizapi.core.repository.QuizRepository;
import fr.hachim.quizapi.core.repository.QuizTagRepository;
import fr.hachim.quizapi.core.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Implémentation du service QuizService.
 */
@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizTagRepository quizTagRepository;

    @Override
    public Page<Quiz> findAllQuizzes(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    @Override
    public Optional<Quiz> findQuizById(UUID id) {
        return quizRepository.findById(id)
                .filter(quiz -> quiz.getDeletedAt() == null);
    }

    @Override
    public Page<Quiz> searchQuizzes(String searchTerm, UUID categoryId, String difficultyLevel, Boolean isPublic, Pageable pageable) {
        return quizRepository.findQuizzesByAdvancedSearch(searchTerm, categoryId, difficultyLevel, isPublic, pageable);
    }

    @Override
    public List<Quiz> findPopularQuizzes(int limit) {
        return quizRepository.findPopularQuizzes(limit);
    }

    @Override
    public Page<Quiz> findQuizzesByUser(UUID userId, Pageable pageable) {
        // Utiliser une méthode plus simple si le repository ne supporte pas JpaSpecificationExecutor
        return quizRepository.findByCreatedByAndDeletedAtIsNull(userId, pageable);
    }

    @Override
    public Page<Quiz> findQuizzesByCategory(UUID categoryId, Pageable pageable) {
        return quizRepository.findAll((root, query, cb) -> 
            cb.and(
                cb.equal(root.get("category").get("id"), categoryId),
                cb.isNull(root.get("deletedAt"))
            ), pageable);
    }

    @Override
    public Page<Quiz> findQuizzesByTag(UUID tagId, Pageable pageable) {
        return quizRepository.findAll((root, query, cb) -> {
            query.distinct(true);
            var join = root.join("quizTags");
            return cb.and(
                cb.equal(join.get("tagId"), tagId),
                cb.isNull(root.get("deletedAt"))
            );
        }, pageable);
    }

    @Override
    @Transactional
    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    @Transactional
    public Quiz updateQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    @Transactional
    public Quiz updateQuizStatus(Quiz quiz, String status) {
        quiz.setStatus(status);
        return quizRepository.save(quiz);
    }

    @Override
    @Transactional
    public boolean deleteQuiz(UUID id) {
        return findQuizById(id)
                .map(quiz -> {
                    // Soft delete
                    quiz.setDeletedAt(LocalDateTime.now());
                    quizRepository.save(quiz);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public Set<UUID> updateQuizTags(UUID quizId, Set<UUID> tagIds) {
        Quiz quiz = findQuizById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", quizId));
        
        // Supprimer tous les tags existants
        quizTagRepository.deleteByQuizId(quizId);
        
        // Ajouter les nouveaux tags
        Set<UUID> addedTags = new HashSet<>();
        if (tagIds != null && !tagIds.isEmpty()) {
            for (UUID tagId : tagIds) {
                QuizTag quizTag = new QuizTag();
                quizTag.setQuizId(quizId);
                quizTag.setTagId(tagId);
                quizTagRepository.save(quizTag);
                addedTags.add(tagId);
            }
        }
        
        return addedTags;
    }

    @Override
    @Transactional
    public Quiz publishQuiz(UUID id) {
        Quiz quiz = findQuizById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", id));
        
        // Vérifier si le quiz peut être publié
        if (countQuestionsByQuizId(id) == 0) {
            throw new BusinessException("Impossible de publier un quiz sans questions");
        }
        
        quiz.setStatus("PUBLISHED");
        return quizRepository.save(quiz);
    }

    @Override
    @Transactional
    public Quiz archiveQuiz(UUID id) {
        Quiz quiz = findQuizById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", id));
        
        quiz.setStatus("ARCHIVED");
        return quizRepository.save(quiz);
    }

    @Override
    public Long countQuestionsByQuizId(UUID quizId) {
        // Cette méthode nécessiterait un repository pour les questions
        // Pour l'instant, on retourne une valeur fictive
        return 0L;
    }

    @Override
    public Long countAttemptsByQuizId(UUID quizId) {
        // Cette méthode nécessiterait un repository pour les tentatives
        // Pour l'instant, on retourne une valeur fictive
        return 0L;
    }
}