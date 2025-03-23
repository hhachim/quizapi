package fr.hachim.quizapi.core.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.hachim.quizapi.core.dto.ApiResponse;
import fr.hachim.quizapi.core.dto.TagDTO;
import fr.hachim.quizapi.core.mapper.TagMapper;
import fr.hachim.quizapi.core.model.Tag;
import fr.hachim.quizapi.core.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Contrôleur REST pour gérer les opérations sur les tags.
 */
@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;
    
    /**
     * Récupère tous les tags.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TagDTO>>> getAllTags() {
        List<Tag> tags = tagService.findAllTags();
        List<TagDTO> tagDTOs = tags.stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(tagDTOs, 
                "Tags récupérés avec succès"));
    }
    
    /**
     * Récupère un tag par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TagDTO>> getTagById(@PathVariable UUID id) {
        return tagService.findTagById(id)
                .map(tag -> ResponseEntity.ok(ApiResponse.success(
                        tagMapper.toDTO(tag), 
                        "Tag récupéré avec succès")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Tag non trouvé")));
    }
    
    /**
     * Récupère les tags populaires.
     */
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<TagDTO>>> getPopularTags(
            @RequestParam(defaultValue = "10") int limit) {
        
        List<Tag> popularTags = tagService.findPopularTags(limit);
        List<TagDTO> tagDTOs = popularTags.stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(tagDTOs, 
                "Tags populaires récupérés avec succès"));
    }
    
    /**
     * Recherche des tags par nom.
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TagDTO>>> searchTags(@RequestParam String name) {
        List<Tag> tags = tagService.searchTagsByName(name);
        List<TagDTO> tagDTOs = tags.stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(tagDTOs, 
                "Recherche de tags effectuée avec succès"));
    }
    
    /**
     * Crée un nouveau tag.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TagDTO>> createTag(@Valid @RequestBody TagDTO tagDTO) {
        Tag tag = tagMapper.toEntity(tagDTO);
        Tag savedTag = tagService.createTag(tag);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(tagMapper.toDTO(savedTag), 
                        "Tag créé avec succès"));
    }
    
    /**
     * Met à jour un tag existant.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TagDTO>> updateTag(
            @PathVariable UUID id, 
            @RequestBody TagDTO tagDTO) {
        
        return tagService.findTagById(id)
                .map(tag -> {
                    tagMapper.updateFromDTO(tag, tagDTO);
                    Tag updatedTag = tagService.updateTag(tag);
                    return ResponseEntity.ok(ApiResponse.success(
                            tagMapper.toDTO(updatedTag), 
                            "Tag mis à jour avec succès"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Tag non trouvé")));
    }
    
    /**
     * Supprime un tag (soft delete).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTag(@PathVariable UUID id) {
        boolean deleted = tagService.deleteTag(id);
        
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success(null, "Tag supprimé avec succès"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Tag non trouvé"));
        }
    }
}