package fr.hachim.quizapi.core.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.hachim.quizapi.core.model.QuizTag;
import fr.hachim.quizapi.core.model.QuizTagId;

/**
 * Repository pour l'entité QuizTag.
 * Gère les associations entre les quiz et les tags.
 */
@Repository
public interface QuizTagRepository extends JpaRepository<QuizTag, QuizTagId> {
    
    /**
     * Trouve tous les tags associés à un quiz.
     * 
     * @param quizId L'ID du quiz
     * @return Liste des associations quiz-tag
     */
    List<QuizTag> findByQuizId(UUID quizId);
    
    /**
     * Trouve tous les quiz associés à un tag.
     * 
     * @param tagId L'ID du tag
     * @return Liste des associations quiz-tag
     */
    List<QuizTag> findByTagId(UUID tagId);
    
    /**
     * Supprime toutes les associations pour un quiz.
     * 
     * @param quizId L'ID du quiz
     */
    @Modifying
    @Query("DELETE FROM QuizTag qt WHERE qt.quizId = :quizId")
    void deleteByQuizId(@Param("quizId") UUID quizId);
    
    /**
     * Supprime toutes les associations pour un tag.
     * 
     * @param tagId L'ID du tag
     */
    @Modifying
    @Query("DELETE FROM QuizTag qt WHERE qt.tagId = :tagId")
    void deleteByTagId(@Param("tagId") UUID tagId);
    
    /**
     * Vérifie si une association existe.
     * 
     * @param quizId L'ID du quiz
     * @param tagId L'ID du tag
     * @return true si l'association existe, false sinon
     */
    boolean existsByQuizIdAndTagId(UUID quizId, UUID tagId);
    
    /**
     * Compte le nombre de quiz associés à un tag.
     * 
     * @param tagId L'ID du tag
     * @return Le nombre de quiz
     */
    @Query("SELECT COUNT(qt.quizId) FROM QuizTag qt WHERE qt.tagId = :tagId")
    Long countQuizzesByTagId(@Param("tagId") UUID tagId);
    
    /**
     * Compte le nombre de tags associés à un quiz.
     * 
     * @param quizId L'ID du quiz
     * @return Le nombre de tags
     */
    @Query("SELECT COUNT(qt.tagId) FROM QuizTag qt WHERE qt.quizId = :quizId")
    Long countTagsByQuizId(@Param("quizId") UUID quizId);
}