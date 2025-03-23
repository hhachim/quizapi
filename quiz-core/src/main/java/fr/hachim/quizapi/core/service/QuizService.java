package fr.hachim.quizapi.core.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.hachim.quizapi.core.model.Quiz;

/**
 * Service pour gérer les opérations sur les quiz.
 */
public interface QuizService {

    /**
     * Récupère tous les quiz avec pagination.
     * 
     * @param pageable Options de pagination
     * @return Page de quiz
     */
    Page<Quiz> findAllQuizzes(Pageable pageable);
    
    /**
     * Récupère un quiz par son ID.
     * 
     * @param id ID du quiz
     * @return Le quiz trouvé (optionnel)
     */
    Optional<Quiz> findQuizById(UUID id);
    
    /**
     * Recherche des quiz par critères multiples avec pagination.
     * 
     * @param searchTerm Terme de recherche
     * @param categoryId ID de catégorie (optionnel)
     * @param difficultyLevel Niveau de difficulté (optionnel)
     * @param isPublic Visibilité publique (optionnel)
     * @param pageable Options de pagination
     * @return Page de quiz correspondants
     */
    Page<Quiz> searchQuizzes(String searchTerm, UUID categoryId, String difficultyLevel, Boolean isPublic, Pageable pageable);
    
    /**
     * Récupère les quiz populaires.
     * 
     * @param limit Nombre maximum de quiz à retourner
     * @return Liste des quiz populaires
     */
    List<Quiz> findPopularQuizzes(int limit);
    
    /**
     * Récupère les quiz créés par un utilisateur.
     * 
     * @param userId ID de l'utilisateur
     * @param pageable Options de pagination
     * @return Page de quiz
     */
    Page<Quiz> findQuizzesByUser(UUID userId, Pageable pageable);
    
    /**
     * Récupère les quiz dans une catégorie.
     * 
     * @param categoryId ID de la catégorie
     * @param pageable Options de pagination
     * @return Page de quiz
     */
    Page<Quiz> findQuizzesByCategory(UUID categoryId, Pageable pageable);
    
    /**
     * Récupère les quiz avec un tag spécifique.
     * 
     * @param tagId ID du tag
     * @param pageable Options de pagination
     * @return Page de quiz
     */
    Page<Quiz> findQuizzesByTag(UUID tagId, Pageable pageable);
    
    /**
     * Crée un nouveau quiz.
     * 
     * @param quiz Le quiz à créer
     * @return Le quiz créé
     */
    Quiz createQuiz(Quiz quiz);
    
    /**
     * Met à jour un quiz existant.
     * 
     * @param quiz Le quiz avec les modifications
     * @return Le quiz mis à jour
     */
    Quiz updateQuiz(Quiz quiz);
    
    /**
     * Met à jour le statut d'un quiz.
     * 
     * @param quiz Le quiz à modifier
     * @param status Le nouveau statut
     * @return Le quiz mis à jour
     */
    Quiz updateQuizStatus(Quiz quiz, String status);
    
    /**
     * Supprime un quiz (soft delete).
     * 
     * @param id ID du quiz à supprimer
     * @return true si supprimé avec succès, false sinon
     */
    boolean deleteQuiz(UUID id);
    
    /**
     * Met à jour les tags associés à un quiz.
     * 
     * @param quizId ID du quiz
     * @param tagIds IDs des tags à associer
     * @return Ensemble des IDs de tags qui ont été associés
     */
    Set<UUID> updateQuizTags(UUID quizId, Set<UUID> tagIds);
    
    /**
     * Publie un quiz.
     * 
     * @param id ID du quiz à publier
     * @return Le quiz publié
     */
    Quiz publishQuiz(UUID id);
    
    /**
     * Archive un quiz.
     * 
     * @param id ID du quiz à archiver
     * @return Le quiz archivé
     */
    Quiz archiveQuiz(UUID id);
    
    /**
     * Compte le nombre de questions dans un quiz.
     * 
     * @param quizId ID du quiz
     * @return Nombre de questions
     */
    Long countQuestionsByQuizId(UUID quizId);
    
    /**
     * Compte le nombre de tentatives pour un quiz.
     * 
     * @param quizId ID du quiz
     * @return Nombre de tentatives
     */
    Long countAttemptsByQuizId(UUID quizId);
}