package fr.hachim.quizapi.core.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * Configuration principale du module Core.
 * Active les fonctionnalités Spring nécessaires et configure les packages à scanner.
 */
@Configuration
@EnableWebMvc
@EnableTransactionManagement
@PropertySource("classpath:application-core.properties")
@ComponentScan("fr.hachim.quizapi.core")
@EntityScan("fr.hachim.quizapi.core.model")
@EnableJpaRepositories("fr.hachim.quizapi.core.repository")
public class CoreConfig {

    /**
     * Configuration de l'API OpenAPI/Swagger
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Quiz API")
                        .version("1.0.0")
                        .description("API modulaire pour la gestion de quiz")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}