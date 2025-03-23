package fr.hachim.quizapi.core.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour envelopper les réponses de l'API avec des métadonnées.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    private String status;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    
    /**
     * Crée une réponse de succès avec des données.
     * 
     * @param <T> Type des données
     * @param data Données à inclure dans la réponse
     * @param message Message optionnel
     * @return Objet ApiResponse
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Crée une réponse de succès avec des données mais sans message.
     * 
     * @param <T> Type des données
     * @param data Données à inclure dans la réponse
     * @return Objet ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, null);
    }
    
    /**
     * Crée une réponse d'erreur.
     * 
     * @param <T> Type des données (généralement Void pour les erreurs)
     * @param message Message d'erreur
     * @return Objet ApiResponse
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Crée une réponse d'erreur avec des données.
     * 
     * @param <T> Type des données
     * @param message Message d'erreur
     * @param data Données associées à l'erreur
     * @return Objet ApiResponse
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}