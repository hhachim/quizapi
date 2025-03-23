package fr.hachim.quizapi.core.service.impl;

import fr.hachim.quizapi.core.model.Plugin;
import fr.hachim.quizapi.core.plugin.spi.QuizPlugin;
import fr.hachim.quizapi.core.repository.PluginRepository;
import fr.hachim.quizapi.core.service.PluginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation du service PluginService.
 */
@Service
@RequiredArgsConstructor
public class PluginServiceImpl implements PluginService {

    private final PluginRepository pluginRepository;

    @Override
    public List<Plugin> getAllPlugins() {
        return pluginRepository.findAll();
    }

    @Override
    public Optional<Plugin> getPluginById(String id) {
        return pluginRepository.findById(id);
    }

    @Override
    public List<Plugin> getEnabledPlugins() {
        return pluginRepository.findByEnabledTrue();
    }

    @Override
    @Transactional
    public Plugin registerPlugin(QuizPlugin quizPlugin) {
        Plugin plugin = new Plugin();
        plugin.setId(quizPlugin.getId());
        plugin.setName(quizPlugin.getName());
        plugin.setVersion(quizPlugin.getVersion());
        plugin.setDescription(quizPlugin.getDescription());
        plugin.setEnabled(quizPlugin.isEnabled());
        plugin.setInstalledAt(LocalDateTime.now());
        
        return pluginRepository.save(plugin);
    }

    @Override
    @Transactional
    public Plugin updatePluginFromRegistry(QuizPlugin quizPlugin) {
        Optional<Plugin> existingPlugin = getPluginById(quizPlugin.getId());
        
        if (existingPlugin.isPresent()) {
            Plugin plugin = existingPlugin.get();
            boolean wasEnabled = plugin.isEnabled();
            boolean isNowEnabled = quizPlugin.isEnabled();
            
            plugin.setName(quizPlugin.getName());
            plugin.setVersion(quizPlugin.getVersion());
            plugin.setDescription(quizPlugin.getDescription());
            plugin.setEnabled(quizPlugin.isEnabled());
            
            // Mettre à jour les timestamps d'activation/désactivation
            if (!wasEnabled && isNowEnabled) {
                plugin.setLastEnabledAt(LocalDateTime.now());
            } else if (wasEnabled && !isNowEnabled) {
                plugin.setLastDisabledAt(LocalDateTime.now());
            }
            
            return pluginRepository.save(plugin);
        } else {
            return registerPlugin(quizPlugin);
        }
    }

    @Override
    @Transactional
    public List<Plugin> synchronizePlugins(List<QuizPlugin> quizPlugins) {
        // Trouver tous les plugins existants
        List<Plugin> existingPlugins = getAllPlugins();
        Map<String, Plugin> existingPluginMap = existingPlugins.stream()
                .collect(Collectors.toMap(Plugin::getId, p -> p));
        
        List<Plugin> updatedPlugins = new ArrayList<>();
        
        // Mettre à jour ou créer des plugins
        for (QuizPlugin quizPlugin : quizPlugins) {
            Plugin plugin;
            if (existingPluginMap.containsKey(quizPlugin.getId())) {
                plugin = updatePluginFromRegistry(quizPlugin);
            } else {
                plugin = registerPlugin(quizPlugin);
            }
            updatedPlugins.add(plugin);
        }
        
        // Désactiver les plugins qui n'existent plus dans le registre
        for (Plugin existingPlugin : existingPlugins) {
            boolean stillExists = quizPlugins.stream()
                    .anyMatch(qp -> qp.getId().equals(existingPlugin.getId()));
            
            if (!stillExists && existingPlugin.isEnabled()) {
                existingPlugin.setEnabled(false);
                existingPlugin.setLastDisabledAt(LocalDateTime.now());
                pluginRepository.save(existingPlugin);
            }
        }
        
        return updatedPlugins;
    }

    @Override
    @Transactional
    public Plugin updatePluginConfig(String id, Map<String, Object> config) {
        return getPluginById(id)
                .map(plugin -> {
                    plugin.setConfig(config);
                    return pluginRepository.save(plugin);
                })
                .orElseThrow(() -> new IllegalArgumentException("Plugin non trouvé: " + id));
    }

    @Override
    public boolean isPluginInstalled(String id) {
        return pluginRepository.existsById(id);
    }

    @Override
    public boolean isPluginEnabled(String id) {
        return getPluginById(id)
                .map(Plugin::isEnabled)
                .orElse(false);
    }
}