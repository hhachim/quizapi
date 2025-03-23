package fr.hachim.quizapi.core.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.hachim.quizapi.core.model.Plugin;
import fr.hachim.quizapi.core.plugin.spi.QuizPlugin;

/**
 * Service pour gérer les opérations sur les plugins.
 */
public interface PluginService {

    /**
     * Récupère tous les plugins enregistrés.
     * 
     * @return Liste de plugins
     */
    List<Plugin> getAllPlugins();
    
    /**
     * Récupère un plugin par son ID.
     * 
     * @param id ID du plugin
     * @return Le plugin trouvé (optionnel)
     */
    Optional<Plugin> getPluginById(String id);
    
    /**
     * Récupère tous les plugins activés.
     * 
     * @return Liste de plugins activés
     */
    List<Plugin> getEnabledPlugins();
    
    /**
     * Enregistre un nouveau plugin dans la base de données.
     * 
     * @param quizPlugin Le plugin à enregistrer
     * @return Le plugin enregistré
     */
    Plugin registerPlugin(QuizPlugin quizPlugin);
    
    /**
     * Met à jour les informations d'un plugin dans la base de données
     * à partir des données du registre de plugins.
     * 
     * @param quizPlugin Le plugin avec les nouvelles informations
     * @return Le plugin mis à jour
     */
    Plugin updatePluginFromRegistry(QuizPlugin quizPlugin);
    
    /**
     * Synchronise la base de données avec la liste des plugins découverts.
     * 
     * @param quizPlugins Liste des plugins découverts
     * @return Liste des plugins mis à jour
     */
    List<Plugin> synchronizePlugins(List<QuizPlugin> quizPlugins);
    
    /**
     * Met à jour la configuration d'un plugin.
     * 
     * @param id ID du plugin
     * @param config Nouvelle configuration
     * @return Le plugin mis à jour
     */
    Plugin updatePluginConfig(String id, Map<String, Object> config);
    
    /**
     * Vérifie si un plugin est installé.
     * 
     * @param id ID du plugin
     * @return true si installé, false sinon
     */
    boolean isPluginInstalled(String id);
    
    /**
     * Vérifie si un plugin est activé.
     * 
     * @param id ID du plugin
     * @return true si activé, false sinon
     */
    boolean isPluginEnabled(String id);
}