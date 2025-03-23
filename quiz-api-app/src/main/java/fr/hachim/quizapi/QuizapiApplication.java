package fr.hachim.quizapi;

import fr.hachim.quizapi.core.config.CoreConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Point d'entrée principal de l'application Quiz API.
 * Importe la configuration du module Core.
 */
@SpringBootApplication
@Import(CoreConfig.class)
public class QuizapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizapiApplication.class, args);
    }
}