package fr.hachim.quizapi.core.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import fr.hachim.quizapi.core.model.Tag;

/**
 * Service pour gérer les opérations sur les tags.
 */
public interface TagService {

    /**
     * Récupère tous les tags non supprimés.
     * 
     * @return Liste de tags
     */
    List<Tag> findAllTags();
    
    /**
     * Récupère un tag par son ID.
     * 
     * @param id ID du tag
     * @return Le tag trouvé (optionnel)
     */
    Optional<Tag> findTagById(UUID id);
    
    /**
     * Récupère un tag par son nom.
     * 
     * @param name Nom du tag
     * @return Le tag trouvé (optionnel)
     */
    Optional<Tag> findTagByName(String name);
    
    /**
     * Recherche des tags par nom.
     * 
     * @param name Nom à rechercher
     * @return Liste de tags correspondants
     */
    List<Tag> searchTagsByName(String name);
    
    /**
     * Récupère les tags populaires.
     * 
     * @param limit Nombre maximum de tags à retourner
     * @return Liste des tags populaires
     */
    List<Tag> findPopularTags(int limit);
    
    /**
     * Récupère les tags associés à un quiz.
     * 
     * @param quizId ID du quiz
     * @return Liste des tags du quiz
     */
    List<Tag> findTagsByQuizId(UUID quizId);
    
    /**
     * Crée un nouveau tag.
     * 
     * @param tag Le tag à créer
     * @return Le tag créé
     */
    Tag createTag(Tag tag);
    
    /**
     * Met à jour un tag existant.
     * 
     * @param tag Le tag avec les modifications
     * @return Le tag mis à jour
     */
    Tag updateTag(Tag tag);
    
    /**
     * Supprime un tag (soft delete).
     * 
     * @param id ID du tag à supprimer
     * @return true si supprimé avec succès, false sinon
     */
    boolean deleteTag(UUID id);
    
    /**
     * Vérifie si un nom de tag est disponible.
     * 
     * @param name Nom à vérifier
     * @return true si disponible, false sinon
     */
    boolean isTagNameAvailable(String name);
    
    /**
     * Compte le nombre de quiz associés à un tag.
     * 
     * @param tagId ID du tag
     * @return Nombre de quiz
     */
    Long countQuizzesByTagId(UUID tagId);
}