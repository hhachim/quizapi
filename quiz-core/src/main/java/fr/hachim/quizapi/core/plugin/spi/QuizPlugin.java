package fr.hachim.quizapi.core.plugin.spi;

/**
 * Interface principale que tous les plugins doivent implémenter.
 * Définit le contrat de base pour le cycle de vie et les métadonnées d'un plugin.
 */
public interface QuizPlugin {

    /**
     * Retourne l'identifiant unique du plugin.
     * @return l'identifiant du plugin (doit être unique)
     */
    String getId();

    /**
     * Retourne le nom du plugin lisible par un humain.
     * @return le nom du plugin
     */
    String getName();

    /**
     * Retourne la version du plugin.
     * @return la version du plugin sous format sémantique (ex: 1.0.0)
     */
    String getVersion();

    /**
     * Retourne une description du plugin.
     * @return la description du plugin
     */
    String getDescription();

    /**
     * Initialise le plugin. Cette méthode est appelée lorsque le plugin est activé.
     * À utiliser pour allouer les ressources nécessaires.
     */
    void initialize();

    /**
     * Arrête le plugin. Cette méthode est appelée lorsque le plugin est désactivé.
     * À utiliser pour libérer les ressources allouées.
     */
    void shutdown();

    /**
     * Indique si le plugin est actuellement activé.
     * @return true si le plugin est activé, false sinon
     */
    boolean isEnabled();

    /**
     * Définit l'état d'activation du plugin.
     * @param enabled true pour activer le plugin, false pour le désactiver
     */
    void setEnabled(boolean enabled);
}