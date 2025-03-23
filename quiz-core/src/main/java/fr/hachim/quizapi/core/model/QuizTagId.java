package fr.hachim.quizapi.core.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe d'ID composite pour l'entité QuizTag.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizTagId implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long quizId;
    private Long tagId;
}