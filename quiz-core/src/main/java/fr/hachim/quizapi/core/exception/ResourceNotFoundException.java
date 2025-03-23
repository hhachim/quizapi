package fr.hachim.quizapi.core.exception;

/**
 * Exception lancée lorsqu'une ressource demandée n'est pas trouvée.
 */
public class ResourceNotFoundException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * Crée une nouvelle exception de ressource non trouvée pour le type et l'ID spécifiés.
     * 
     * @param resourceType Le type de ressource
     * @param id L'identifiant de la ressource
     */
    public ResourceNotFoundException(String resourceType, Object id) {
        super(String.format("%s avec l'ID %s non trouvé", resourceType, id));
    }
    
    /**
     * Crée une nouvelle exception de ressource non trouvée avec le message spécifié.
     * 
     * @param message Le message d'erreur
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}