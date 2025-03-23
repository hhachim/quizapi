package fr.hachim.quizapi.core.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Entité représentant un plugin installé dans le système.
 * Stocke les métadonnées et l'état du plugin.
 */
@Data
@Entity
@Table(name = "plugins")
public class Plugin {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "description")
    private String description;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = false;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "config", columnDefinition = "jsonb")
    private Map<String, Object> config = new HashMap<>();

    @Column(name = "installed_at", nullable = false)
    private LocalDateTime installedAt = LocalDateTime.now();

    @Column(name = "last_enabled_at")
    private LocalDateTime lastEnabledAt;

    @Column(name = "last_disabled_at")
    private LocalDateTime lastDisabledAt;
}