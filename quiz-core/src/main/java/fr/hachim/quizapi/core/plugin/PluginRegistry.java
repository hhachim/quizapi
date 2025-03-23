package fr.hachim.quizapi.core.plugin;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.hachim.quizapi.core.plugin.spi.QuizPlugin;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * Registre central des plugins de l'application.
 * Gère l'enregistrement, l'activation, la désactivation et la découverte des plugins.
 */
@Slf4j
@Component
public class PluginRegistry {

    private final Map<String, QuizPlugin> plugins = new ConcurrentHashMap<>();
    
    @Value("${quizapi.plugins.scan-on-startup:true}")
    private boolean scanOnStartup;

    /**
     * Initialise le registre des plugins.
     * Si scanOnStartup est true, découvre automatiquement les plugins.
     */
    @PostConstruct
    public void init() {
        if (scanOnStartup) {
            discoverPlugins();
        }
    }

    /**
     * Découvre les plugins disponibles via le ServiceLoader de Java.
     */
    public void discoverPlugins() {
        log.info("Découverte des plugins via ServiceLoader...");
        ServiceLoader<QuizPlugin> loader = ServiceLoader.load(QuizPlugin.class);
        
        for (QuizPlugin plugin : loader) {
            registerPlugin(plugin);
        }
        log.info("{} plugins ont été découverts", plugins.size());
    }

    /**
     * Enregistre un plugin dans le registre.
     * @param plugin le plugin à enregistrer
     * @return true si l'enregistrement a réussi, false si un plugin avec le même ID existe déjà
     */
    public boolean registerPlugin(QuizPlugin plugin) {
        if (plugins.containsKey(plugin.getId())) {
            log.warn("Un plugin avec l'ID {} est déjà enregistré", plugin.getId());
            return false;
        }
        
        log.info("Enregistrement du plugin : {} ({})", plugin.getName(), plugin.getId());
        plugins.put(plugin.getId(), plugin);
        return true;
    }

    /**
     * Active un plugin.
     * @param pluginId l'identifiant du plugin à activer
     * @return true si l'activation a réussi, false si le plugin n'existe pas
     */
    public boolean enablePlugin(String pluginId) {
        QuizPlugin plugin = plugins.get(pluginId);
        if (plugin == null) {
            log.warn("Impossible d'activer le plugin {} : non trouvé", pluginId);
            return false;
        }
        
        try {
            log.info("Activation du plugin : {}", pluginId);
            plugin.initialize();
            plugin.setEnabled(true);
            return true;
        } catch (Exception e) {
            log.error("Erreur lors de l'activation du plugin {} : {}", pluginId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Désactive un plugin.
     * @param pluginId l'identifiant du plugin à désactiver
     * @return true si la désactivation a réussi, false si le plugin n'existe pas
     */
    public boolean disablePlugin(String pluginId) {
        QuizPlugin plugin = plugins.get(pluginId);
        if (plugin == null) {
            log.warn("Impossible de désactiver le plugin {} : non trouvé", pluginId);
            return false;
        }
        
        try {
            log.info("Désactivation du plugin : {}", pluginId);
            plugin.shutdown();
            plugin.setEnabled(false);
            return true;
        } catch (Exception e) {
            log.error("Erreur lors de la désactivation du plugin {} : {}", pluginId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Retourne un plugin par son identifiant.
     * @param pluginId l'identifiant du plugin
     * @return le plugin ou null s'il n'existe pas
     */
    public QuizPlugin getPlugin(String pluginId) {
        return plugins.get(pluginId);
    }

    /**
     * Retourne tous les plugins enregistrés.
     * @return une collection de tous les plugins
     */
    public Collection<QuizPlugin> getAllPlugins() {
        return Collections.unmodifiableCollection(plugins.values());
    }

    /**
     * Retourne les plugins activés.
     * @return une liste des plugins activés
     */
    public List<QuizPlugin> getEnabledPlugins() {
        return plugins.values().stream()
                .filter(QuizPlugin::isEnabled)
                .toList();
    }
}