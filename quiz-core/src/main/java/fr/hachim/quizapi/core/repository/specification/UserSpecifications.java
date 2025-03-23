package fr.hachim.quizapi.core.repository.specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import fr.hachim.quizapi.core.model.User;
import jakarta.persistence.criteria.Predicate;

/**
 * Spécifications JPA pour les requêtes dynamiques sur les utilisateurs.
 * Permet de construire des requêtes complexes avec des critères multiples.
 */
public class UserSpecifications {
    
    /**
     * Crée une spécification pour rechercher des utilisateurs par nom d'utilisateur (recherche partielle, insensible à la casse).
     * 
     * @param username Le nom d'utilisateur à rechercher
     * @return La spécification correspondante
     */
    public static Specification<User> usernameContains(String username) {
        return (root, query, criteriaBuilder) -> {
            if (username == null || username.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("username")),
                "%" + username.toLowerCase() + "%"
            );
        };
    }
    
    /**
     * Crée une spécification pour rechercher des utilisateurs par email (recherche partielle, insensible à la casse).
     * 
     * @param email L'email à rechercher
     * @return La spécification correspondante
     */
    public static Specification<User> emailContains(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("email")),
                "%" + email.toLowerCase() + "%"
            );
        };
    }
    
    /**
     * Crée une spécification pour rechercher des utilisateurs par prénom (recherche partielle, insensible à la casse).
     * 
     * @param firstName Le prénom à rechercher
     * @return La spécification correspondante
     */
    public static Specification<User> firstNameContains(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null || firstName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("firstName")),
                "%" + firstName.toLowerCase() + "%"
            );
        };
    }
    
    /**
     * Crée une spécification pour rechercher des utilisateurs par nom (recherche partielle, insensible à la casse).
     * 
     * @param lastName Le nom à rechercher
     * @return La spécification correspondante
     */
    public static Specification<User> lastNameContains(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (lastName == null || lastName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("lastName")),
                "%" + lastName.toLowerCase() + "%"
            );
        };
    }
    
    /**
     * Crée une spécification pour rechercher des utilisateurs actifs ou inactifs.
     * 
     * @param isActive true pour les utilisateurs actifs, false pour les inactifs
     * @return La spécification correspondante
     */
    public static Specification<User> isActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            if (isActive == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }
    
    /**
     * Crée une spécification pour rechercher des utilisateurs qui se sont connectés après une date donnée.
     * 
     * @param date La date de référence
     * @return La spécification correspondante
     */
    public static Specification<User> lastLoginAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThan(root.get("lastLoginAt"), date);
        };
    }
    
    /**
     * Crée une spécification pour rechercher des utilisateurs créés après une date donnée.
     * 
     * @param date La date de référence
     * @return La spécification correspondante
     */
    public static Specification<User> createdAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThan(root.get("createdAt"), date);
        };
    }
    
    /**
     * Crée une spécification pour rechercher des utilisateurs non supprimés.
     * 
     * @return La spécification correspondante
     */
    public static Specification<User> notDeleted() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.isNull(root.get("deletedAt"));
    }
    
    /**
     * Crée une spécification pour rechercher des utilisateurs par terme de recherche global
     * (recherche dans le nom d'utilisateur, email, prénom et nom).
     * 
     * @param searchTerm Le terme de recherche
     * @return La spécification correspondante
     */
    public static Specification<User> searchByTerm(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            
            String searchPattern = "%" + searchTerm.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();
            
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get("username")), 
                searchPattern
            ));
            
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get("email")), 
                searchPattern
            ));
            
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get("firstName")), 
                searchPattern
            ));
            
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get("lastName")), 
                searchPattern
            ));
            
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}