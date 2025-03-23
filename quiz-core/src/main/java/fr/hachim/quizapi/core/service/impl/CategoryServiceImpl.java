package fr.hachim.quizapi.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.hachim.quizapi.core.model.Category;
import fr.hachim.quizapi.core.repository.CategoryRepository;
import fr.hachim.quizapi.core.service.CategoryService;
import lombok.RequiredArgsConstructor;

/**
 * Implémentation du service CategoryService.
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findByDeletedAtIsNull();
    }

    @Override
    public Optional<Category> findCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .filter(category -> category.getDeletedAt() == null);
    }

    @Override
    public List<Category> findRootCategories() {
        return categoryRepository.findByParentIdIsNullAndDeletedAtIsNull();
    }

    @Override
    public List<Category> findSubcategories(UUID parentId) {
        return categoryRepository.findByParentIdAndDeletedAtIsNull(parentId);
    }

    @Override
    public List<Category> searchCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCaseAndDeletedAtIsNull(name);
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public boolean deleteCategory(UUID id) {
        return findCategoryById(id)
                .map(category -> {
                    // Vérifier si la catégorie a des sous-catégories
                    if (hasSubcategories(id)) {
                        return false;
                    }
                    
                    // Vérifier s'il y a des quiz dans cette catégorie
                    if (countQuizzesByCategory(id) > 0) {
                        return false;
                    }
                    
                    // Soft delete
                    category.setDeletedAt(LocalDateTime.now());
                    categoryRepository.save(category);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Long countQuizzesByCategory(UUID categoryId) {
        return categoryRepository.countQuizzesByCategory(categoryId);
    }

    @Override
    public boolean hasSubcategories(UUID categoryId) {
        return categoryRepository.countSubcategories(categoryId) > 0;
    }

    @Override
    public List<Category> getCategoryAncestors(UUID categoryId) {
        return categoryRepository.findAncestorHierarchy(categoryId);
    }
}