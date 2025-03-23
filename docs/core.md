# Feuille de route pour compléter le core de l'API Quiz

Pour rendre votre core fonctionnel avec quelques routes avant de passer aux phases concernant les plugins, voici les tâches restantes à accomplir :

## 1. Modèle de données fondamental

- **Créer les entités de base manquantes**
  - `User` pour gérer les utilisateurs
  - `Quiz` pour les questionnaires
  - `Category` pour organiser les quiz
  - `Tag` pour le système de tags

- **Finaliser le mapping JPA**
  - Définir les relations entre entités
  - Configurer les auditeurs (created_at, updated_at)
  - Mettre en place les validations des champs

- **Ajouter les migrations Flyway supplémentaires**
  - Créer les tables pour ces entités fondamentales
  - Ajouter les contraintes et index nécessaires

## 2. Repositories

- **Développer les repositories JPA**
  - `UserRepository`
  - `QuizRepository`
  - `CategoryRepository`
  - `TagRepository`

- **Ajouter des méthodes de recherche personnalisées**
  - Recherche par critères
  - Requêtes optimisées pour les cas d'usage fréquents

## 3. Services métier

- **Créer les services CRUD pour chaque entité**
  - Services de base pour la création, lecture, mise à jour, suppression
  - Logique métier pour la validation et les règles spécifiques

- **Développer des services transversaux**
  - Service de gestion des utilisateurs
  - Service de gestion des quiz

- **Intégrer la gestion des erreurs**
  - Exceptions métier personnalisées
  - Traduction des exceptions techniques en erreurs compréhensibles

## 4. API REST fondamentale

- **Développer les contrôleurs REST**
  - Contrôleur pour gérer les utilisateurs
  - Contrôleur pour les quiz
  - Contrôleur pour les catégories

- **Implémenter les DTOs**
  - Objets de transfert de données pour le découplage
  - Conversion entité/DTO dans les deux sens

- **Configurer les mappings d'URL**
  - Structure d'API RESTful cohérente
  - Versionner les endpoints (/api/v1/...)

## 5. Sécurité de base

- **Configurer Spring Security**
  - Authentification basique
  - Protection des routes
  - Système de rôles (ADMIN, USER)

- **Mettre en place la gestion des JWT**
  - Génération et validation des tokens
  - Refresh token mechanism

## 6. Documentation de l'API

- **Finaliser la configuration OpenAPI/Swagger**
  - Documentation des endpoints
  - Description des modèles
  - Exemples de requêtes/réponses

- **Documenter les codes d'erreur**
  - Liste des codes d'erreur possibles
  - Description des conditions d'erreur

## 7. Tests

- **Développer les tests unitaires**
  - Tests des repositories
  - Tests des services
  - Tests des contrôleurs

- **Ajouter des tests d'intégration**
  - Tests des endpoints de l'API
  - Tests de la couche persistence

## 8. Mise en place d'un système d'événements

- **Créer un bus d'événements**
  - Définir les événements du système
  - Implémenter les mécanismes de publication/souscription

- **Intégrer Spring Application Events**
  - Définir les événements spécifiques à l'application
  - Mettre en place les listeners

## Ordre de priorité recommandé

1. Modèle de données fondamental + Migrations
2. Repositories
3. Services métier
4. API REST (contrôleurs + DTOs)
5. Documentation de l'API
6. Système d'événements
7. Sécurité de base
8. Tests

Une fois ces éléments mis en place, vous aurez un core fonctionnel avec des routes utilisables, avant même d'implémenter les plugins. Cette base solide facilitera le développement des plugins dans les phases suivantes.