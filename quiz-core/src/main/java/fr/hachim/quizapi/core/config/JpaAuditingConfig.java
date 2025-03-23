package fr.hachim.quizapi.core.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration pour l'auditing JPA.
 * Permet de suivre automatiquement qui a créé ou modifié une entité.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    /**
     * Bean qui fournit l'identifiant de l'utilisateur actuel pour l'audit.
     * Dans une implémentation réelle, cela devrait récupérer l'utilisateur 
     * connecté depuis le contexte de sécurité.
     * 
     * @return Instance d'AuditorAware
     */
    @Bean
    public AuditorAware<Long> auditorProvider() {
        // Pour l'instant, on retourne l'ID 1 (admin) comme utilisateur par défaut
        // À remplacer par l'implémentation réelle une fois l'authentification en place
        return () -> Optional.of(1L);
    }
}