package fr.hachim.quizapi.core.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import fr.hachim.quizapi.core.dto.PageResponse;
import fr.hachim.quizapi.core.dto.UserCreationDTO;
import fr.hachim.quizapi.core.dto.UserDTO;
import fr.hachim.quizapi.core.mapper.UserMapper;
import fr.hachim.quizapi.core.model.User;
import fr.hachim.quizapi.core.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Contrôleur REST pour gérer les opérations sur les utilisateurs.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    
    /**
     * Récupère tous les utilisateurs, avec pagination.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        Page<User> usersPage = userService.findAllUsers(pageable);
        Page<UserDTO> userDTOsPage = usersPage.map(userMapper::toDTO);
        
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(userDTOsPage), 
                "Utilisateurs récupérés avec succès"));
    }
    
    /**
     * Récupère un utilisateur par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable UUID id) {
        return userService.findUserById(id)
                .map(user -> ResponseEntity.ok(ApiResponse.success(
                        userMapper.toDTO(user), 
                        "Utilisateur récupéré avec succès")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Utilisateur non trouvé")));
    }
    
    /**
     * Recherche des utilisateurs par terme de recherche.
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserDTO>>> searchUsers(
            @RequestParam String term,
            @RequestParam(defaultValue = "10") int limit) {
        
        List<User> users = userService.searchUsers(term, limit);
        List<UserDTO> userDTOs = users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(userDTOs, 
                "Recherche d'utilisateurs effectuée avec succès"));
    }
    
    /**
     * Crée un nouvel utilisateur.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserCreationDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User savedUser = userService.createUser(user);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(userMapper.toDTO(savedUser), 
                        "Utilisateur créé avec succès"));
    }
    
    /**
     * Met à jour un utilisateur existant.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable UUID id, 
            @RequestBody UserDTO userDTO) {
        
        return userService.findUserById(id)
                .map(user -> {
                    userMapper.updateFromDTO(user, userDTO);
                    User updatedUser = userService.updateUser(user);
                    return ResponseEntity.ok(ApiResponse.success(
                            userMapper.toDTO(updatedUser), 
                            "Utilisateur mis à jour avec succès"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Utilisateur non trouvé")));
    }
    
    /**
     * Désactive un utilisateur (soft delete).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        boolean deleted = userService.deleteUser(id);
        
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success(null, "Utilisateur supprimé avec succès"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Utilisateur non trouvé"));
        }
    }
}