package fr.hachim.quizapi.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.hachim.quizapi.core.exception.BusinessException;
import fr.hachim.quizapi.core.model.Tag;
import fr.hachim.quizapi.core.repository.QuizTagRepository;
import fr.hachim.quizapi.core.repository.TagRepository;
import fr.hachim.quizapi.core.service.TagService;
import lombok.RequiredArgsConstructor;

/**
 * Implémentation du service TagService.
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final QuizTagRepository quizTagRepository;

    @Override
    public List<Tag> findAllTags() {
        return tagRepository.findByDeletedAtIsNull();
    }

    @Override
    public Optional<Tag> findTagById(UUID id) {
        return tagRepository.findById(id)
                .filter(tag -> tag.getDeletedAt() == null);
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        return tagRepository.findByNameAndDeletedAtIsNull(name);
    }

    @Override
    public List<Tag> searchTagsByName(String name) {
        return tagRepository.findByNameContainingIgnoreCaseAndDeletedAtIsNull(name);
    }

    @Override
    public List<Tag> findPopularTags(int limit) {
        return tagRepository.findPopularTags(limit);
    }

    @Override
    public List<Tag> findTagsByQuizId(UUID quizId) {
        return tagRepository.findTagsByQuizId(quizId);
    }

    @Override
    @Transactional
    public Tag createTag(Tag tag) {
        // Vérifier si le nom du tag est disponible
        if (!isTagNameAvailable(tag.getName())) {
            throw new BusinessException("Un tag avec ce nom existe déjà");
        }
        
        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public Tag updateTag(Tag tag) {
        // Vérifier si le nouveau nom est déjà pris par un autre tag
        Tag existingTag = tagRepository.findByNameAndDeletedAtIsNull(tag.getName()).orElse(null);
        if (existingTag != null && !existingTag.getId().equals(tag.getId())) {
            throw new BusinessException("Un tag avec ce nom existe déjà");
        }
        
        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public boolean deleteTag(UUID id) {
        return findTagById(id)
                .map(tag -> {
                    // Soft delete
                    tag.setDeletedAt(LocalDateTime.now());
                    tagRepository.save(tag);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public boolean isTagNameAvailable(String name) {
        return !tagRepository.existsByNameIgnoreCaseAndDeletedAtIsNull(name);
    }

    @Override
    public Long countQuizzesByTagId(UUID tagId) {
        return quizTagRepository.countQuizzesByTagId(tagId);
    }
}