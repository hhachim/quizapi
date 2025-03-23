package fr.hachim.quizapi.core.repository.specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import fr.hachim.quizapi.core.model.Quiz;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

/**
 * Spécifications JPA pour les requêtes dynamiques sur les quiz.
 * Permet de construire des requêtes complexes avec des critères multiples.
 */
public class QuizSpecifications {
    
    /**
     * Crée une spécification pour rechercher des quiz par titre (recherche partielle, insensible à la casse).
     * 
     * @param title Le titre à rechercher
     * @return La spécification correspondante
     */
    public static Specification<Quiz> titleContains(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("title")),
                "%" + title.toLowerCase() + "%"
            );
        };
    }
    
    /**
     * Crée une spécification pour rechercher des quiz par description (recherche partielle, insensible à la casse).
     * 
     * @param description La description à rechercher
     * @return La spécification correspondante
     */
    public static Specification<Quiz> descriptionContains(String description) {
        return (root, query, criteriaBuilder) -> {
            if (description == null || description.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("description")),
                "%" + description.toLowerCase() + "%"
            );
        };
    }
    
    /**
     * Crée une spécification pour rechercher des quiz par catégorie.
     * 
     * @param categoryId L'ID de la catégorie
     * @return La spécification correspondante
     */
    public static Specification<Quiz> hasCategory(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("category").get("id"), categoryId);
        };
    }
    
    /**
     * Crée une spécification pour rechercher des quiz par niveau de difficulté.
     * 
     * @param difficultyLevel Le niveau de difficulté
     * @return La spécification correspondante
     */
    public static Specification<Quiz> hasDifficultyLevel(String difficultyLevel) {
        return (root, query, criteriaBuilder) -> {
            if (difficultyLevel == null || difficultyLevel.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("difficultyLevel"), difficultyLevel);
        };
    }
    
    /**
     * Crée une spécification pour rechercher des quiz par statut.
     * 
     * @param status Le statut
     * @return La spécification correspondante
     */
    public static Specification<Quiz> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
    
    /**
     * Crée une spécification pour rechercher des quiz publics ou privés.
     * 
     * @param isPublic true pour les quiz publics, false pour les privés
     * @return La spécification correspondante
     */
    public static Specification<Quiz> isPublic(Boolean isPublic) {
        return (root, query, criteriaBuilder) -> {
            if (isPublic == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isPublic"), isPublic);
        };
    }
    
    /**
     * Crée une spécification pour rechercher des quiz créés par un utilisateur.
     * 
     * @param userId L'ID de l'utilisateur
     * @return La spécification correspondante
     */
    public static Specification<Quiz> createdBy(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("createdBy"), userId);
        };
    }
    
    /**
     * Crée une spécification pour rechercher des quiz créés après une date donnée.
     * 
     * @param date La date de référence
     * @return La spécification correspondante
     */
    public static Specification<Quiz> createdAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThan(root.get("createdAt"), date);
        };
    }
    
    /**
     * Crée une spécification pour rechercher des quiz par tag.
     * 
     * @param tagId L'ID du tag
     * @return La spécification correspondante
     */
    public static Specification<Quiz> hasTag(Long tagId) {
        return (root, query, criteriaBuilder) -> {
            if (tagId == null) {
                return criteriaBuilder.conjunction();
            }
            
            Join<Object, Object> quizTags = root.join("quizTags", JoinType.INNER);
            return criteriaBuilder.equal(quizTags.get("tagId"), tagId);
        };
    }
    
    /**
     * Crée une spécification pour rechercher des quiz non supprimés.
     * 
     * @return La spécification correspondante
     */
    public static Specification<Quiz> notDeleted() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.isNull(root.get("deletedAt"));
    }
    
    /**
     * Crée une spécification pour rechercher des quiz par terme de recherche global
     * (recherche dans le titre et la description).
     * 
     * @param searchTerm Le terme de recherche
     * @return La spécification correspondante
     */
    public static Specification<Quiz> searchByTerm(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            
            String searchPattern = "%" + searchTerm.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();
            
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get("title")), 
                searchPattern
            ));
            
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get("description")), 
                searchPattern
            ));
            
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}