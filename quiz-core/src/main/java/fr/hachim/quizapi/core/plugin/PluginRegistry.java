package fr.hachim.quizapi.core.plugin;

import fr.hachim.quizapi.core.plugin.spi.QuizPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registre des plugins pour l'application Quiz.
 * Gère l'enregistrement, l'activation, la désactivation et le suivi des plugins.
 */
@Slf4j
@Component
public class PluginRegistry {

    private final Map<String, QuizPlugin> plugins = new ConcurrentHashMap<>();

    /**
     * Enregistre un plugin dans le registre.
     *
     * @param plugin Le plugin à enregistrer
     */
    public void registerPlugin(QuizPlugin plugin) {
        log.info("Enregistrement du plugin: {} ({})", plugin.getName(), plugin.getId());
        plugins.put(plugin.getId(), plugin);
    }

    /**
     * Récupère un plugin par son identifiant.
     *
     * @param id L'identifiant du plugin
     * @return Le plugin, s'il existe
     */
    public Optional<QuizPlugin> getPlugin(String id) {
        return Optional.ofNullable(plugins.get(id));
    }

    /**
     * Récupère tous les plugins enregistrés.
     *
     * @return Collection de tous les plugins
     */
    public Collection<QuizPlugin> getAllPlugins() {
        return plugins.values();
    }

    /**
     * Active un plugin par son identifiant.
     *
     * @param id L'identifiant du plugin à activer
     */
    public void enablePlugin(String id) {
        getPlugin(id).ifPresent(plugin -> {
            log.info("Activation du plugin: {} ({})", plugin.getName(), plugin.getId());
            plugin.setEnabled(true);
            plugin.initialize();
        });
    }

    /**
     * Désactive un plugin par son identifiant.
     *
     * @param id L'identifiant du plugin à désactiver
     */
    public void disablePlugin(String id) {
        getPlugin(id).ifPresent(plugin -> {
            log.info("Désactivation du plugin: {} ({})", plugin.getName(), plugin.getId());
            plugin.shutdown();
            plugin.setEnabled(false);
        });
    }

    /**
     * Supprime un plugin du registre.
     *
     * @param id L'identifiant du plugin à supprimer
     */
    public void removePlugin(String id) {
        getPlugin(id).ifPresent(plugin -> {
            log.info("Suppression du plugin: {} ({})", plugin.getName(), plugin.getId());
            if (plugin.isEnabled()) {
                plugin.shutdown();
            }
            plugins.remove(id);
        });
    }
}