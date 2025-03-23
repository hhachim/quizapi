package fr.hachim.quizapi.core.plugin.spi;

import java.util.Set;

/**
 * Interface pour les plugins qui souhaitent exposer des contrôleurs REST.
 * À implémenter en plus de l'interface QuizPlugin pour étendre l'API REST.
 */
public interface SpringMvcPlugin {

    /**
     * Retourne l'ensemble des contrôleurs REST fournis par ce plugin.
     * Les contrôleurs doivent être annotés avec @RestController et @RequestMapping.
     * 
     * @return l'ensemble des contrôleurs à enregistrer
     */
    Set<Object> getControllers();
}