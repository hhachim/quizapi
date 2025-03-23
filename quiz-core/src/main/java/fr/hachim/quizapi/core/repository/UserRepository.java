package fr.hachim.quizapi.core.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.hachim.quizapi.core.model.User;

/**
 * Repository pour l'entité User.
 * Fournit des méthodes d'accès aux données pour les utilisateurs.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     * 
     * @param username Le nom d'utilisateur
     * @return L'utilisateur trouvé (optionnel)
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Recherche un utilisateur par son adresse email.
     * 
     * @param email L'adresse email
     * @return L'utilisateur trouvé (optionnel)
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Vérifie si un nom d'utilisateur existe déjà.
     * 
     * @param username Le nom d'utilisateur à vérifier
     * @return true si le nom d'utilisateur existe, false sinon
     */
    boolean existsByUsername(String username);
    
    /**
     * Vérifie si une adresse email existe déjà.
     * 
     * @param email L'adresse email à vérifier
     * @return true si l'adresse email existe, false sinon
     */
    boolean existsByEmail(String email);
    
    /**
     * Recherche des utilisateurs par état d'activité.
     * 
     * @param isActive État d'activité (actif/inactif)
     * @return Liste des utilisateurs correspondants
     */
    List<User> findByIsActive(boolean isActive);
    
    /**
     * Recherche des utilisateurs qui se sont connectés depuis une date donnée.
     * 
     * @param since Date de référence
     * @return Liste des utilisateurs correspondants
     */
    List<User> findByLastLoginAtAfter(LocalDateTime since);
    
    /**
     * Recherche des utilisateurs par nom ou prénom (recherche partielle).
     * 
     * @param firstName Prénom (partiel)
     * @param lastName Nom (partiel)
     * @return Liste des utilisateurs correspondants
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<User> findByNameContaining(@Param("firstName") String firstName, @Param("lastName") String lastName);
    
    /**
     * Recherche des utilisateurs par critères multiples.
     * 
     * @param searchTerm Terme de recherche (nom d'utilisateur, email, nom, prénom)
     * @return Liste des utilisateurs correspondants
     */
    @Query("SELECT u FROM User u WHERE " +
           "u.deletedAt IS NULL AND " +
           "(LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<User> searchUsers(@Param("searchTerm") String searchTerm);
}