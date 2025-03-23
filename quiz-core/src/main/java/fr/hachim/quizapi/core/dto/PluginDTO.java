package fr.hachim.quizapi.core.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour transférer les informations sur les plugins installés.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginDTO {
    
    private String id;
    
    private String name;
    
    private String version;
    
    private String description;
    
    private boolean enabled;
    
    private Map<String, Object> config;
    
    private LocalDateTime installedAt;
    
    private LocalDateTime lastEnabledAt;
    
    private LocalDateTime lastDisabledAt;
}