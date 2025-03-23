package fr.hachim.quizapi.core.repository;

import fr.hachim.quizapi.core.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité Category.
 * Fournit des méthodes d'accès aux données pour les catégories.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * Recherche une catégorie par son UUID.
     * 
     * @param uuid L'UUID de la catégorie
     * @return La catégorie trouvée (optionnel)
     */
    Optional<Category> findByUuid(UUID uuid);
    
    /**
     * Recherche des catégories par nom (recherche exacte).
     * 
     * @param name Le nom de la catégorie
     * @return La catégorie trouvée (optionnel)
     */
    Optional<Category> findByNameAndDeletedAtIsNull(String name);
    
    /**
     * Recherche des catégories par nom (recherche partielle).
     * 
     * @param name Le nom partiel
     * @return Liste des catégories correspondantes
     */
    List<Category> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String name);
    
    /**
     * Recherche des catégories racines (sans parent).
     * 
     * @return Liste des catégories racines
     */
    List<Category> findByParentIdIsNullAndDeletedAtIsNull();
    
    /**
     * Recherche des sous-catégories d'une catégorie.
     * 
     * @param parentId L'ID de la catégorie parente
     * @return Liste des sous-catégories
     */
    List<Category> findByParentIdAndDeletedAtIsNull(Long parentId);
    
    /**
     * Recherche des catégories par créateur.
     * 
     * @param createdBy L'ID de l'utilisateur créateur
     * @return Liste des catégories
     */
    List<Category> findByCreatedByAndDeletedAtIsNull(Long createdBy);
    
    /**
     * Vérifie si une catégorie a des sous-catégories.
     * 
     * @param categoryId L'ID de la catégorie
     * @return Le nombre de sous-catégories
     */
    @Query("SELECT COUNT(c) FROM Category c WHERE c.parentId = :categoryId AND c.deletedAt IS NULL")
    Long countSubcategories(@Param("categoryId") Long categoryId);
    
    /**
     * Compte le nombre de quiz dans une catégorie.
     * 
     * @param categoryId L'ID de la catégorie
     * @return Le nombre de quiz
     */
    @Query("SELECT COUNT(q) FROM Quiz q WHERE q.category.id = :categoryId AND q.deletedAt IS NULL")
    Long countQuizzesByCategory(@Param("categoryId") Long categoryId);
    
    /**
     * Recherche toutes les catégories non supprimées.
     * 
     * @return Liste des catégories actives
     */
    List<Category> findByDeletedAtIsNull();
    
    /**
     * Recherche toutes les catégories avec leur nombre de quiz.
     * 
     * @return Liste des catégories avec des statistiques
     */
    @Query("SELECT c, COUNT(q) FROM Category c LEFT JOIN Quiz q ON q.category.id = c.id AND q.deletedAt IS NULL " +
           "WHERE c.deletedAt IS NULL " +
           "GROUP BY c.id " +
           "ORDER BY c.name")
    List<Object[]> findAllWithQuizCount();
    
    /**
     * Recherche toute la hiérarchie des ancêtres d'une catégorie.
     * 
     * @param categoryId L'ID de la catégorie
     * @return Liste des catégories parentes (de la plus proche à la racine)
     */
    @Query(value = "WITH RECURSIVE category_hierarchy AS ( " +
                   "  SELECT * FROM categories WHERE id = :categoryId AND deleted_at IS NULL " +
                   "  UNION ALL " +
                   "  SELECT c.* FROM categories c " +
                   "  JOIN category_hierarchy ch ON c.id = ch.parent_id " +
                   "  WHERE c.deleted_at IS NULL " +
                   ") " +
                   "SELECT * FROM category_hierarchy WHERE id != :categoryId " +
                   "ORDER BY id DESC", 
                   nativeQuery = true)
    List<Category> findAncestorHierarchy(@Param("categoryId") Long categoryId);
}