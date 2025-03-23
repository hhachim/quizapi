package fr.hachim.quizapi.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.hachim.quizapi.core.exception.BusinessException;
import fr.hachim.quizapi.core.model.User;
import fr.hachim.quizapi.core.repository.UserRepository;
import fr.hachim.quizapi.core.service.UserService;
import lombok.RequiredArgsConstructor;

/**
 * Implémentation du service UserService.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id)
                .filter(user -> user.getDeletedAt() == null);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getDeletedAt() == null);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getDeletedAt() == null);
    }

    @Override
    public List<User> searchUsers(String searchTerm, int limit) {
        return userRepository.searchUsers(searchTerm).stream()
                .limit(limit)
                .toList();
    }

    @Override
    @Transactional
    public User createUser(User user) {
        // Vérifier si le nom d'utilisateur est disponible
        if (!isUsernameAvailable(user.getUsername())) {
            throw new BusinessException("Ce nom d'utilisateur est déjà pris");
        }
        
        // Vérifier si l'email est disponible
        if (!isEmailAvailable(user.getEmail())) {
            throw new BusinessException("Cette adresse email est déjà utilisée");
        }
        
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean deleteUser(UUID id) {
        return findUserById(id)
                .map(user -> {
                    // Soft delete
                    user.setDeletedAt(LocalDateTime.now());
                    user.setIsActive(false);
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public User updateLastLoginDate(UUID id) {
        return findUserById(id)
                .map(user -> {
                    user.setLastLoginAt(LocalDateTime.now());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new BusinessException("Utilisateur non trouvé"));
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
}