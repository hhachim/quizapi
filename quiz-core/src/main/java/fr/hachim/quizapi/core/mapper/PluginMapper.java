package fr.hachim.quizapi.core.mapper;

import org.springframework.stereotype.Component;

import fr.hachim.quizapi.core.dto.PluginDTO;
import fr.hachim.quizapi.core.model.Plugin;
import fr.hachim.quizapi.core.plugin.spi.QuizPlugin;

/**
 * Mapper pour convertir entre les objets Plugin, PluginDTO et QuizPlugin.
 */
@Component
public class PluginMapper {
    
    /**
     * Convertit une entité Plugin en PluginDTO.
     * 
     * @param plugin L'entité à convertir
     * @return Le DTO correspondant
     */
    public PluginDTO toDTO(Plugin plugin) {
        if (plugin == null) {
            return null;
        }
        
        return PluginDTO.builder()
                .id(plugin.getId())
                .name(plugin.getName())
                .version(plugin.getVersion())
                .description(plugin.getDescription())
                .enabled(plugin.isEnabled())
                .config(plugin.getConfig())
                .installedAt(plugin.getInstalledAt())
                .lastEnabledAt(plugin.getLastEnabledAt())
                .lastDisabledAt(plugin.getLastDisabledAt())
                .build();
    }
    
    /**
     * Convertit un objet QuizPlugin en entité Plugin.
     * 
     * @param quizPlugin L'objet plugin à convertir
     * @return L'entité correspondante
     */
    public Plugin toEntity(QuizPlugin quizPlugin) {
        if (quizPlugin == null) {
            return null;
        }
        
        Plugin plugin = new Plugin();
        plugin.setId(quizPlugin.getId());
        plugin.setName(quizPlugin.getName());
        plugin.setVersion(quizPlugin.getVersion());
        plugin.setDescription(quizPlugin.getDescription());
        plugin.setEnabled(quizPlugin.isEnabled());
        
        return plugin;
    }
    
    /**
     * Met à jour une entité Plugin existante avec les valeurs d'un QuizPlugin.
     * 
     * @param plugin L'entité à mettre à jour
     * @param quizPlugin L'objet contenant les nouvelles valeurs
     */
    public void updateFromQuizPlugin(Plugin plugin, QuizPlugin quizPlugin) {
        if (plugin == null || quizPlugin == null) {
            return;
        }
        
        plugin.setName(quizPlugin.getName());
        plugin.setVersion(quizPlugin.getVersion());
        plugin.setDescription(quizPlugin.getDescription());
        plugin.setEnabled(quizPlugin.isEnabled());
    }
}