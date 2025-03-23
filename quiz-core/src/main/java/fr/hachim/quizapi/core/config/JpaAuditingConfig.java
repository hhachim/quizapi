package fr.hachim.quizapi.core.config;

import java.util.Optional;
import java.util.UUID;

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
    public AuditorAware<UUID> auditorProvider() {
        // Pour l'instant, on retourne un UUID fixe pour l'administrateur par défaut
        // Cet ID correspond à celui inséré dans le script SQL de migration
        return () -> Optional.of(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    }
}