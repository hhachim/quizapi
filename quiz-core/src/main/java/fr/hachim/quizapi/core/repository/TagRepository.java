package fr.hachim.quizapi.core.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.hachim.quizapi.core.model.Tag;

/**
 * Repository pour l'entité Tag.
 * Fournit des méthodes d'accès aux données pour les tags.
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
    
    /**
     * Recherche un tag par son nom.
     * 
     * @param name Le nom du tag
     * @return Le tag trouvé (optionnel)
     */
    Optional<Tag> findByNameAndDeletedAtIsNull(String name);
    
    /**
     * Recherche des tags par nom (recherche partielle).
     * 
     * @param name Le nom partiel
     * @return Liste des tags correspondants
     */
    List<Tag> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String name);
    
    /**
     * Recherche des tags par créateur.
     * 
     * @param createdBy L'ID de l'utilisateur créateur
     * @return Liste des tags
     */
    List<Tag> findByCreatedByAndDeletedAtIsNull(UUID createdBy);
    
    /**
     * Recherche tous les tags non supprimés.
     * 
     * @return Liste de tous les tags actifs
     */
    List<Tag> findByDeletedAtIsNull();
    
    /**
     * Vérifie si un tag avec ce nom existe déjà.
     * 
     * @param name Le nom du tag
     * @return true si le tag existe, false sinon
     */
    boolean existsByNameIgnoreCaseAndDeletedAtIsNull(String name);
    
    /**
     * Récupère les tags associés à un quiz.
     * 
     * @param quizId L'ID du quiz
     * @return Liste des tags du quiz
     */
    @Query("SELECT t FROM Tag t JOIN QuizTag qt ON t.id = qt.tagId " +
           "WHERE qt.quizId = :quizId AND t.deletedAt IS NULL")
    List<Tag> findTagsByQuizId(@Param("quizId") UUID quizId);
    
    /**
     * Recherche les tags populaires basés sur leur utilisation.
     * 
     * @param limit Nombre maximum de tags à retourner
     * @return Liste des tags populaires
     */
    @Query(value = "SELECT t.* FROM tags t " +
                   "JOIN quiz_tags qt ON t.id = qt.tag_id " +
                   "WHERE t.deleted_at IS NULL " +
                   "GROUP BY t.id " +
                   "ORDER BY COUNT(qt.quiz_id) DESC " +
                   "LIMIT :limit", 
                   nativeQuery = true)
    List<Tag> findPopularTags(@Param("limit") int limit);
    
    /**
     * Recherche les tags avec le nombre de quiz associés.
     * 
     * @return Liste des tags avec leur utilisation
     */
    @Query("SELECT t, COUNT(qt.quizId) FROM Tag t LEFT JOIN QuizTag qt ON t.id = qt.tagId " +
           "WHERE t.deletedAt IS NULL " +
           "GROUP BY t.id " +
           "ORDER BY t.name")
    List<Object[]> findAllWithQuizCount();
    
    /**
     * Recherche les tags associés aux quiz d'un utilisateur.
     * 
     * @param userId L'ID de l'utilisateur
     * @return Liste des tags utilisés par l'utilisateur
     */
    @Query("SELECT DISTINCT t FROM Tag t " +
           "JOIN QuizTag qt ON t.id = qt.tagId " +
           "JOIN Quiz q ON qt.quizId = q.id " +
           "WHERE q.createdBy = :userId AND t.deletedAt IS NULL AND q.deletedAt IS NULL")
    List<Tag> findTagsUsedByUser(@Param("userId") UUID userId);
}