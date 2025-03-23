package fr.hachim.quizapi.core.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.hachim.quizapi.core.model.User;

/**
 * Service pour gérer les opérations sur les utilisateurs.
 */
public interface UserService {

    /**
     * Récupère tous les utilisateurs avec pagination.
     * 
     * @param pageable Options de pagination
     * @return Page d'utilisateurs
     */
    Page<User> findAllUsers(Pageable pageable);
    
    /**
     * Récupère un utilisateur par son ID.
     * 
     * @param id ID de l'utilisateur
     * @return L'utilisateur trouvé (optionnel)
     */
    Optional<User> findUserById(UUID id);
    
    /**
     * Récupère un utilisateur par son nom d'utilisateur.
     * 
     * @param username Nom d'utilisateur
     * @return L'utilisateur trouvé (optionnel)
     */
    Optional<User> findUserByUsername(String username);
    
    /**
     * Récupère un utilisateur par son email.
     * 
     * @param email Adresse email
     * @return L'utilisateur trouvé (optionnel)
     */
    Optional<User> findUserByEmail(String email);
    
    /**
     * Recherche des utilisateurs par terme de recherche.
     * 
     * @param searchTerm Terme de recherche
     * @param limit Nombre maximum de résultats
     * @return Liste d'utilisateurs
     */
    List<User> searchUsers(String searchTerm, int limit);
    
    /**
     * Crée un nouvel utilisateur.
     * 
     * @param user L'utilisateur à créer
     * @return L'utilisateur créé
     */
    User createUser(User user);
    
    /**
     * Met à jour un utilisateur existant.
     * 
     * @param user L'utilisateur avec les modifications
     * @return L'utilisateur mis à jour
     */
    User updateUser(User user);
    
    /**
     * Supprime un utilisateur (soft delete).
     * 
     * @param id ID de l'utilisateur à supprimer
     * @return true si supprimé avec succès, false sinon
     */
    boolean deleteUser(UUID id);
    
    /**
     * Met à jour la date de dernière connexion d'un utilisateur.
     * 
     * @param id ID de l'utilisateur
     * @return L'utilisateur mis à jour
     */
    User updateLastLoginDate(UUID id);
    
    /**
     * Vérifie si un nom d'utilisateur est disponible.
     * 
     * @param username Nom d'utilisateur à vérifier
     * @return true si disponible, false sinon
     */
    boolean isUsernameAvailable(String username);
    
    /**
     * Vérifie si une adresse email est disponible.
     * 
     * @param email Adresse email à vérifier
     * @return true si disponible, false sinon
     */
    boolean isEmailAvailable(String email);
}