package fr.hachim.quizapi.core.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.hachim.quizapi.core.model.Quiz;

/**
 * Repository pour l'entité Quiz.
 * Fournit des méthodes d'accès aux données pour les quiz.
 */
@Repository
public interface QuizRepository extends JpaRepository<Quiz, UUID> {
    
    /**
     * Recherche des quiz par titre (recherche partielle).
     * 
     * @param title Le titre (partiel)
     * @return Liste des quiz correspondants
     */
    List<Quiz> findByTitleContainingIgnoreCaseAndDeletedAtIsNull(String title);
    
    /**
     * Recherche des quiz par catégorie.
     * 
     * @param categoryId L'ID de la catégorie
     * @return Liste des quiz correspondants
     */
    List<Quiz> findByCategoryIdAndDeletedAtIsNull(UUID categoryId);
    
    /**
     * Recherche des quiz par statut.
     * 
     * @param status Le statut du quiz
     * @return Liste des quiz correspondants
     */
    List<Quiz> findByStatusAndDeletedAtIsNull(String status);
    
    /**
     * Recherche des quiz par niveau de difficulté.
     * 
     * @param difficultyLevel Le niveau de difficulté
     * @return Liste des quiz correspondants
     */
    List<Quiz> findByDifficultyLevelAndDeletedAtIsNull(String difficultyLevel);
    
    /**
     * Recherche des quiz par créateur.
     * 
     * @param createdBy L'ID de l'utilisateur créateur
     * @return Liste des quiz correspondants
     */
    List<Quiz> findByCreatedByAndDeletedAtIsNull(UUID createdBy);
    
    /**
     * Recherche des quiz publics.
     * 
     * @return Liste des quiz publics
     */
    List<Quiz> findByIsPublicTrueAndStatusAndDeletedAtIsNull(String status);
    
    /**
     * Recherche des quiz créés après une date donnée.
     * 
     * @param since Date de référence
     * @return Liste des quiz correspondants
     */
    List<Quiz> findByCreatedAtAfterAndDeletedAtIsNull(LocalDateTime since);
    
    /**
     * Recherche paginée de quiz par critères multiples.
     * 
     * @param searchTerm Terme de recherche (titre, description)
     * @param pageable Pagination
     * @return Page de quiz correspondants
     */
    @Query("SELECT q FROM Quiz q WHERE " +
           "q.deletedAt IS NULL AND " +
           "(LOWER(q.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(q.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Quiz> searchQuizzes(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    /**
     * Recherche paginée de quiz par critères avancés.
     * 
     * @param searchTerm Terme de recherche
     * @param categoryId ID de catégorie (optionnel)
     * @param difficultyLevel Niveau de difficulté (optionnel)
     * @param isPublic Visibilité publique (optionnel)
     * @param pageable Pagination
     * @return Page de quiz correspondants
     */
    @Query("SELECT q FROM Quiz q WHERE " +
           "q.deletedAt IS NULL AND " +
           "(:searchTerm IS NULL OR LOWER(q.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(q.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "(:categoryId IS NULL OR q.category.id = :categoryId) AND " +
           "(:difficultyLevel IS NULL OR q.difficultyLevel = :difficultyLevel) AND " +
           "(:isPublic IS NULL OR q.isPublic = :isPublic) AND " +
           "q.status = 'PUBLISHED'")
    Page<Quiz> findQuizzesByAdvancedSearch(
            @Param("searchTerm") String searchTerm,
            @Param("categoryId") UUID categoryId,
            @Param("difficultyLevel") String difficultyLevel,
            @Param("isPublic") Boolean isPublic,
            Pageable pageable);
    
    /**
     * Récupère les quiz populaires basés sur le nombre de tentatives.
     * 
     * @param limit Nombre maximum de quiz à retourner
     * @return Liste des quiz populaires
     */
    @Query(value = "SELECT q.* FROM quizzes q " +
                   "JOIN quiz_attempts qa ON q.id = qa.quiz_id " +
                   "WHERE q.deleted_at IS NULL AND q.status = 'PUBLISHED' " +
                   "GROUP BY q.id " +
                   "ORDER BY COUNT(qa.id) DESC " +
                   "LIMIT :limit", nativeQuery = true)
    List<Quiz> findPopularQuizzes(@Param("limit") int limit);
}