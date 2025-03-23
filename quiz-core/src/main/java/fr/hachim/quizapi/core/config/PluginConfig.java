package fr.hachim.quizapi.core.config;

import fr.hachim.quizapi.core.plugin.PluginRegistry;
import fr.hachim.quizapi.core.plugin.spi.QuizPlugin;
import fr.hachim.quizapi.core.plugin.spi.SpringMvcPlugin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import jakarta.annotation.PostConstruct;
import java.util.Set;

/**
 * Configuration pour l'infrastructure des plugins.
 * S'occupe de l'enregistrement des contrôleurs REST des plugins.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class PluginConfig {

    private final PluginRegistry pluginRegistry;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * Initialise les contrôleurs REST des plugins activés.
     * Cette méthode enregistre dynamiquement les contrôleurs REST fournis par
     * les plugins qui implémentent SpringMvcPlugin.
     */
    @PostConstruct
    public void initializePluginControllers() {
        log.info("Initialisation des contrôleurs REST des plugins...");
        
        for (QuizPlugin plugin : pluginRegistry.getEnabledPlugins()) {
            if (plugin instanceof SpringMvcPlugin springMvcPlugin) {
                registerPluginControllers(springMvcPlugin);
            }
        }
    }

    /**
     * Enregistre les contrôleurs d'un plugin dans le contexte Spring MVC.
     * @param springMvcPlugin le plugin fournissant des contrôleurs REST
     */
    private void registerPluginControllers(SpringMvcPlugin springMvcPlugin) {
        Set<Object> controllers = springMvcPlugin.getControllers();
        if (controllers == null || controllers.isEmpty()) {
            return;
        }
        
        log.info("Enregistrement de {} contrôleurs pour le plugin {}", 
                controllers.size(), 
                ((QuizPlugin) springMvcPlugin).getId());
        
        for (Object controller : controllers) {
            try {
                requestMappingHandlerMapping.detectHandlerMethods(controller);
                log.debug("Contrôleur enregistré : {}", controller.getClass().getName());
            } catch (Exception e) {
                log.error("Erreur lors de l'enregistrement du contrôleur {} : {}", 
                        controller.getClass().getName(), 
                        e.getMessage(), e);
            }
        }
    }
}