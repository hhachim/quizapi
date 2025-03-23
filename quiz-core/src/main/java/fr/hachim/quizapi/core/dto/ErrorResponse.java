package fr.hachim.quizapi.core.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour représenter une réponse d'erreur standardisée.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    private String status;
    private Integer statusCode;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    
    @Builder.Default
    private List<ValidationError> errors = new ArrayList<>();
    
    /**
     * Ajoute une erreur de validation.
     */
    public void addValidationError(String field, String message) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(new ValidationError(field, message));
    }
    
    /**
     * Classe interne pour représenter une erreur de validation spécifique.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
    }
}