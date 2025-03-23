package fr.hachim.quizapi.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration pour les mappers utilisés dans l'application.
 */
@Configuration
public class MapperConfig {

    /**
     * Bean pour l'encodeur de mot de passe utilisé dans les mappers.
     * 
     * @return L'encodeur de mot de passe
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}