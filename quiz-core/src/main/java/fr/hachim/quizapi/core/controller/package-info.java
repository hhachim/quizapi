/**
 * Contrôleurs REST pour l'API Quiz.
 * 
 * Ce package contient tous les contrôleurs REST qui exposent les fonctionnalités
 * de l'API Quiz. La structure suit une architecture RESTful avec les conventions suivantes:
 * 
 * <ul>
 *   <li>Tous les endpoints commencent par /api/v1/</li>
 *   <li>Les resources sont nommées au pluriel (users, quizzes, categories, tags)</li>
 *   <li>Les méthodes HTTP standard sont utilisées (GET, POST, PUT, PATCH, DELETE)</li>
 *   <li>Les réponses sont enveloppées dans un objet ApiResponse pour standardisation</li>
 *   <li>La pagination est supportée pour les listes volumineuses</li>
 * </ul>
 * 
 * Les contrôleurs principaux sont:
 * <ul>
 *   <li>{@link fr.hachim.quizapi.core.controller.UserController} - Gestion des utilisateurs</li>
 *   <li>{@link fr.hachim.quizapi.core.controller.QuizController} - Gestion des quiz</li>
 *   <li>{@link fr.hachim.quizapi.core.controller.CategoryController} - Gestion des catégories</li>
 *   <li>{@link fr.hachim.quizapi.core.controller.TagController} - Gestion des tags</li>
 *   <li>{@link fr.hachim.quizapi.core.controller.PluginController} - Gestion des plugins</li>
 * </ul>
 */
package fr.hachim.quizapi.core.controller;