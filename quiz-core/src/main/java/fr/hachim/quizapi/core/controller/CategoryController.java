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
import fr.hachim.quizapi.core.dto.CategoryDTO;
import fr.hachim.quizapi.core.mapper.CategoryMapper;
import fr.hachim.quizapi.core.model.Category;
import fr.hachim.quizapi.core.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Contrôleur REST pour gérer les opérations sur les catégories.
 */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    
    /**
     * Récupère toutes les catégories.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories(
            @RequestParam(required = false) Boolean onlyRoots) {
        
        List<Category> categories;
        if (Boolean.TRUE.equals(onlyRoots)) {
            categories = categoryService.findRootCategories();
        } else {
            categories = categoryService.findAllCategories();
        }
        
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(categoryDTOs, 
                "Catégories récupérées avec succès"));
    }
    
    /**
     * Récupère une catégorie par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryById(@PathVariable UUID id) {
        return categoryService.findCategoryById(id)
                .map(category -> ResponseEntity.ok(ApiResponse.success(
                        categoryMapper.toDTO(category), 
                        "Catégorie récupérée avec succès")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Catégorie non trouvée")));
    }
    
    /**
     * Récupère les sous-catégories d'une catégorie.
     */
    @GetMapping("/{id}/subcategories")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getSubcategories(@PathVariable UUID id) {
        List<Category> subcategories = categoryService.findSubcategories(id);
        List<CategoryDTO> subcategoryDTOs = subcategories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(subcategoryDTOs, 
                "Sous-catégories récupérées avec succès"));
    }
    
    /**
     * Crée une nouvelle catégorie.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        Category category = categoryMapper.toEntity(categoryDTO);
        Category savedCategory = categoryService.createCategory(category);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(categoryMapper.toDTO(savedCategory), 
                        "Catégorie créée avec succès"));
    }
    
    /**
     * Met à jour une catégorie existante.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(
            @PathVariable UUID id, 
            @RequestBody CategoryDTO categoryDTO) {
        
        return categoryService.findCategoryById(id)
                .map(category -> {
                    categoryMapper.updateFromDTO(category, categoryDTO);
                    Category updatedCategory = categoryService.updateCategory(category);
                    return ResponseEntity.ok(ApiResponse.success(
                            categoryMapper.toDTO(updatedCategory), 
                            "Catégorie mise à jour avec succès"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Catégorie non trouvée")));
    }
    
    /**
     * Supprime une catégorie (soft delete).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable UUID id) {
        boolean deleted = categoryService.deleteCategory(id);
        
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success(null, "Catégorie supprimée avec succès"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Catégorie non trouvée ou contient des sous-catégories"));
        }
    }
    
    /**
     * Recherche des catégories par nom.
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> searchCategories(@RequestParam String name) {
        List<Category> categories = categoryService.searchCategoriesByName(name);
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(categoryDTOs, 
                "Recherche de catégories effectuée avec succès"));
    }
}