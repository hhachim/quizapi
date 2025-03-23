package fr.hachim.quizapi.core.exception;

/**
 * Exception lancée lors d'erreurs métier dans l'application.
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Crée une nouvelle exception métier avec le message spécifié.
     * 
     * @param message Le message d'erreur
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * Crée une nouvelle exception métier avec le message et la cause spécifiés.
     * 
     * @param message Le message d'erreur
     * @param cause La cause de l'exception
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}