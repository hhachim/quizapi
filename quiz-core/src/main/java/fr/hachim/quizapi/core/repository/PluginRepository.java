package fr.hachim.quizapi.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.hachim.quizapi.core.model.Plugin;

/**
 * Repository pour l'entité Plugin.
 */
@Repository
public interface PluginRepository extends JpaRepository<Plugin, String> {
    
    /**
     * Trouve tous les plugins activés.
     * 
     * @return Liste des plugins activés
     */
    List<Plugin> findByEnabledTrue();
}