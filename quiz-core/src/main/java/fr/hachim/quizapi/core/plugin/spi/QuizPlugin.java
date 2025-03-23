package fr.hachim.quizapi.core.plugin.spi;

/**
 * Interface de base pour tous les plugins du système Quiz API.
 * Définit le contrat que tous les plugins doivent implémenter pour 
 * s'intégrer dans l'architecture modulaire.
 */
public interface QuizPlugin {
    /**
     * Retourne l'identifiant unique du plugin.
     * Cet identifiant doit être constant et unique pour chaque type de plugin.
     * 
     * @return Identifiant unique du plugin
     */
    String getId();
    
    /**
     * Retourne le nom lisible du plugin.
     * Ce nom peut être utilisé dans l'interface utilisateur.
     * 
     * @return Nom lisible du plugin
     */
    String getName();
    
    /**
     * Retourne la version du plugin.
     * Devrait suivre le versionnement sémantique.
     * 
     * @return Version du plugin
     */
    String getVersion();
    
    /**
     * Retourne une description des fonctionnalités du plugin.
     * 
     * @return Description du plugin
     */
    String getDescription();
    
    /**
     * Initialise le plugin. 
     * Cette méthode est appelée lors de l'activation du plugin.
     * Elle doit initialiser toutes les ressources nécessaires.
     */
    void initialize();
    
    /**
     * Arrête le plugin. 
     * Cette méthode est appelée lors de la désactivation du plugin.
     * Elle doit libérer proprement toutes les ressources.
     */
    void shutdown();
    
    /**
     * Indique si le plugin est actuellement activé.
     * 
     * @return true si le plugin est activé, false sinon
     */
    boolean isEnabled();
    
    /**
     * Définit l'état d'activation du plugin.
     * 
     * @param enabled true pour activer, false pour désactiver
     */
    void setEnabled(boolean enabled);
}