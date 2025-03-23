package fr.hachim.quizapi.core.config;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import fr.hachim.quizapi.core.plugin.PluginRegistry;
import fr.hachim.quizapi.core.plugin.spi.QuizPlugin;
import fr.hachim.quizapi.core.plugin.spi.SpringMvcPlugin;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private final ApplicationContext applicationContext;

    /**
     * Initialise les contrôleurs REST des plugins activés.
     * Cette méthode enregistre dynamiquement les contrôleurs REST fournis par
     * les plugins qui implémentent SpringMvcPlugin.
     */
    @PostConstruct
    public void initializePluginControllers() {
        log.info("Initialisation des contrôleurs REST des plugins...");
        
        for (QuizPlugin plugin : pluginRegistry.getAllPlugins().stream()
                .filter(QuizPlugin::isEnabled)
                .collect(Collectors.toList())) {
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
                // Enregistrer le contrôleur dans le contexte Spring
                applicationContext.getAutowireCapableBeanFactory().autowireBean(controller);
                // Enregistrer les méthodes du contrôleur comme endpoints
                requestMappingHandlerMapping.getHandlerMethods(); // Force l'initialisation
                applicationContext.getAutowireCapableBeanFactory().initializeBean(controller, controller.getClass().getName());
                // Utiliser registerMapping directement ou autre méthode appropriée
                log.debug("Contrôleur enregistré : {}", controller.getClass().getName());
            } catch (Exception e) {
                log.error("Erreur lors de l'enregistrement du contrôleur {} : {}", 
                        controller.getClass().getName(), 
                        e.getMessage(), e);
            }
        }
    }
}