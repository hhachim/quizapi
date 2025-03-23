package fr.hachim.quizapi.core.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.hachim.quizapi.core.dto.ApiResponse;
import fr.hachim.quizapi.core.dto.PluginDTO;
import fr.hachim.quizapi.core.mapper.PluginMapper;
import fr.hachim.quizapi.core.model.Plugin;
import fr.hachim.quizapi.core.plugin.PluginRegistry;
import fr.hachim.quizapi.core.plugin.spi.QuizPlugin;
import fr.hachim.quizapi.core.service.PluginService;
import lombok.RequiredArgsConstructor;

/**
 * Contrôleur REST pour gérer les plugins du système.
 */
@RestController
@RequestMapping("/api/v1/admin/plugins")
@RequiredArgsConstructor
public class PluginController {

    private final PluginRegistry pluginRegistry;
    private final PluginService pluginService;
    private final PluginMapper pluginMapper;
    
    /**
     * Récupère tous les plugins installés.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PluginDTO>>> getAllPlugins() {
        List<Plugin> plugins = pluginService.getAllPlugins();
        List<PluginDTO> pluginDTOs = plugins.stream()
                .map(pluginMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(pluginDTOs, 
                "Plugins récupérés avec succès"));
    }
    
    /**
     * Récupère un plugin par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PluginDTO>> getPluginById(@PathVariable String id) {
        return pluginService.getPluginById(id)
                .map(plugin -> ResponseEntity.ok(ApiResponse.success(
                        pluginMapper.toDTO(plugin), 
                        "Plugin récupéré avec succès")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Plugin non trouvé")));
    }
    
    /**
     * Active un plugin.
     */
    @PostMapping("/{id}/enable")
    public ResponseEntity<ApiResponse<PluginDTO>> enablePlugin(@PathVariable String id) {
        QuizPlugin quizPlugin = pluginRegistry.getPlugin(id);
        
        if (quizPlugin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Plugin non trouvé"));
        }
        
        boolean success = pluginRegistry.enablePlugin(id);
        
        if (success) {
            Plugin plugin = pluginService.updatePluginFromRegistry(quizPlugin);
            return ResponseEntity.ok(ApiResponse.success(
                    pluginMapper.toDTO(plugin), 
                    "Plugin activé avec succès"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Échec de l'activation du plugin"));
        }
    }
    
    /**
     * Désactive un plugin.
     */
    @PostMapping("/{id}/disable")
    public ResponseEntity<ApiResponse<PluginDTO>> disablePlugin(@PathVariable String id) {
        QuizPlugin quizPlugin = pluginRegistry.getPlugin(id);
        
        if (quizPlugin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Plugin non trouvé"));
        }
        
        boolean success = pluginRegistry.disablePlugin(id);
        
        if (success) {
            Plugin plugin = pluginService.updatePluginFromRegistry(quizPlugin);
            return ResponseEntity.ok(ApiResponse.success(
                    pluginMapper.toDTO(plugin), 
                    "Plugin désactivé avec succès"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Échec de la désactivation du plugin"));
        }
    }
    
    /**
     * Rafraîchit la liste des plugins disponibles.
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<List<PluginDTO>>> refreshPlugins() {
        pluginRegistry.discoverPlugins();
        List<Plugin> updatedPlugins = pluginService.synchronizePlugins(
                pluginRegistry.getAllPlugins().stream().collect(Collectors.toList()));
        
        List<PluginDTO> pluginDTOs = updatedPlugins.stream()
                .map(pluginMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(pluginDTOs, 
                "Liste des plugins rafraîchie avec succès"));
    }
}