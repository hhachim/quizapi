package fr.hachim.quizapi.core.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import fr.hachim.quizapi.core.model.Category;

/**
 * Service pour gérer les opérations sur les catégories.
 */
public interface CategoryService {

    /**
     * Récupère toutes les catégories non supprimées.
     * 
     * @return Liste de catégories
     */
    List<Category> findAllCategories();
    
    /**
     * Récupère une catégorie par son ID.
     * 
     * @param id ID de la catégorie
     * @return La catégorie trouvée (optionnel)
     */
    Optional<Category> findCategoryById(UUID id);
    
    /**
     * Récupère les catégories racines (sans parent).
     * 
     * @return Liste de catégories racines
     */
    List<Category> findRootCategories();
    
    /**
     * Récupère les sous-catégories d'une catégorie.
     * 
     * @param parentId ID de la catégorie parente
     * @return Liste de sous-catégories
     */
    List<Category> findSubcategories(UUID parentId);
    
    /**
     * Recherche des catégories par nom.
     * 
     * @param name Nom à rechercher
     * @return Liste de catégories correspondantes
     */
    List<Category> searchCategoriesByName(String name);
    
    /**
     * Crée une nouvelle catégorie.
     * 
     * @param category La catégorie à créer
     * @return La catégorie créée
     */
    Category createCategory(Category category);
    
    /**
     * Met à jour une catégorie existante.
     * 
     * @param category La catégorie avec les modifications
     * @return La catégorie mise à jour
     */
    Category updateCategory(Category category);
    
    /**
     * Supprime une catégorie (soft delete).
     * 
     * @param id ID de la catégorie à supprimer
     * @return true si supprimée avec succès, false sinon
     */
    boolean deleteCategory(UUID id);
    
    /**
     * Compte le nombre de quiz dans une catégorie.
     * 
     * @param categoryId ID de la catégorie
     * @return Nombre de quiz
     */
    Long countQuizzesByCategory(UUID categoryId);
    
    /**
     * Vérifie si une catégorie a des sous-catégories.
     * 
     * @param categoryId ID de la catégorie
     * @return true si elle a des sous-catégories, false sinon
     */
    boolean hasSubcategories(UUID categoryId);
    
    /**
     * Récupère le chemin d'héritage d'une catégorie (parents).
     * 
     * @param categoryId ID de la catégorie
     * @return Liste des catégories parentes (de la plus proche à la racine)
     */
    List<Category> getCategoryAncestors(UUID categoryId);
}