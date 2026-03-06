# 📑 INDEX - Guide de Navigation

Bienvenue dans le projet API des Aventuriers ! Ce fichier vous guide pour naviguer facilement dans toute la documentation et le code.

---

## 🚀 Démarrage Rapide (5-30 minutes)

| Durée | Fichier | But |
|-------|---------|-----|
| **5 min** | [GETTING_STARTED.md](GETTING_STARTED.md) | Comprendre le projet |
| **15 min** | [QUICKSTART.md](QUICKSTART.md) | Compiler et démarrer |
| **10 min** | Tester avec curl | Valider l'API |

---

## 📚 Documentation Complète

### Vue d'Ensemble
- **[README.md](README.md)** - Vue générale du projet (architecture, endpoints, technologies)
- **[FINAL_SUMMARY.md](FINAL_SUMMARY.md)** - Résumé de l'implémentation complète
- **[COMPLETION_SUMMARY.md](COMPLETION_SUMMARY.md)** - Ce qui a été livré

### Utilisation de l'API
- **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** - Référence complète (endpoints, paramètres, réponses)
- **[CURL_EXAMPLES.md](CURL_EXAMPLES.md)** - 14 exemples pratiques de requêtes
- **[QUICKSTART.md](QUICKSTART.md)** - Guide de démarrage avec pas à pas

### Technique et Architecture
- **[IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)** - Détails techniques et architecture
- **[CHECKLIST.md](CHECKLIST.md)** - Vérification point par point de l'implémentation
- **[INVENTORY.md](INVENTORY.md)** - Inventaire détaillé de tous les fichiers créés

### Contrat API
- **[openapi.yaml](openapi.yaml)** - Contrat OpenAPI 3.0.3 complet

---

## 💻 Code Source

### Par Rôle

#### Pour les Utilisateurs de l'API
1. Lire [GETTING_STARTED.md](GETTING_STARTED.md)
2. Consulter [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
3. Copier des exemples de [CURL_EXAMPLES.md](CURL_EXAMPLES.md)

#### Pour les Développeurs
1. Lire [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)
2. Explorer `src/main/java/com/ynov/adventures/`
3. Consulter [CHECKLIST.md](CHECKLIST.md) pour vérifier

#### Pour les Contributeurs
1. Tout lire 📖
2. Exécuter `mvn test` 🧪
3. Consulter les tests dans `src/test/` 📝

### Fichiers Java

#### Contrôleur REST
- **src/main/java/com/ynov/adventures/controller/AventurierController.java**
  - 6 endpoints REST (GET list, POST, GET id, PUT, PATCH, DELETE)

#### Service Métier
- **src/main/java/com/ynov/adventures/service/AventurierService.java**
  - Logique métier complète
  - Validation des règles
  - Pagination et filtrage

#### Entités JPA
- **src/main/java/com/ynov/adventures/domain/Aventurier.java**
- **src/main/java/com/ynov/adventures/domain/Caracteristiques.java**
- **src/main/java/com/ynov/adventures/domain/Classe.java**

#### DTOs (Data Transfer Objects)
- **src/main/java/com/ynov/adventures/dto/** (7 fichiers)
  - AventurierDTO, AventurierCreateDTO, AventurierPatchDTO
  - CaracteristiquesDTO, CaracteristiquesPatchDTO
  - AventurierListResponseDTO, PaginationDTO

#### Repository
- **src/main/java/com/ynov/adventures/repository/AventurierRepository.java**
  - JpaRepository avec requêtes personnalisées

#### Gestion des Erreurs
- **src/main/java/com/ynov/adventures/exception/GlobalExceptionHandler.java**
- **src/main/java/com/ynov/adventures/exception/AventurierNotFoundException.java**
- **src/main/java/com/ynov/adventures/exception/AventurierBusinessException.java**

#### Tests
- **src/test/java/com/ynov/adventures/service/AventurierServiceTest.java**
- **src/test/java/com/ynov/adventures/controller/AventurierControllerTest.java**

#### Configuration
- **src/main/resources/application.yaml** - Configuration Spring Boot
- **src/main/resources/data.sql** - Données de test (7 aventuriers)

---

## 🎯 Par Cas d'Usage

### "Je veux juste utiliser l'API"
1. Lire [GETTING_STARTED.md](GETTING_STARTED.md)
2. Suivre [QUICKSTART.md](QUICKSTART.md)
3. Consulter [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
4. Copier des exemples de [CURL_EXAMPLES.md](CURL_EXAMPLES.md)

### "Je veux comprendre l'architecture"
1. Lire [README.md](README.md)
2. Lire [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)
3. Explorer `src/main/java/com/ynov/adventures/`
4. Consulter [CHECKLIST.md](CHECKLIST.md)

### "Je veux contribuer/modifier"
1. Lire [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)
2. Examiner les tests `src/test/`
3. Modifier le code `src/main/`
4. Tester : `mvn test`
5. Consulter [CHECKLIST.md](CHECKLIST.md) pour vérifier

### "Je veux déployer en production"
1. Lire [QUICKSTART.md](QUICKSTART.md) - Configuration
2. Modifier `src/main/resources/application.yaml`
3. Utiliser une vraie base de données
4. Exécuter `mvn clean package`
5. Déployer le JAR

---

## 🧪 Tests et Validation

### Tests Unitaires
```bash
mvn test
```
Voir : `src/test/java/`

### Tests Manuels
- **Bash/Linux/Mac** : `bash test_api.sh`
- **PowerShell/Windows** : `powershell -ExecutionPolicy Bypass -File test_api.ps1`

### Tester Manuellement
Consulter [CURL_EXAMPLES.md](CURL_EXAMPLES.md) pour 14 exemples

---

## ⚙️ Configuration

### application.yaml
Voir : `src/main/resources/application.yaml`

**Clés importantes :**
- `spring.datasource.url` - Base de données
- `spring.jpa.hibernate.ddl-auto` - Schéma auto-créé
- `logging.level` - Niveau de log

### data.sql
Voir : `src/main/resources/data.sql`

7 aventuriers prêchargés pour tester

---

## 📊 Statistiques

| Aspect | Nombre |
|--------|--------|
| Fichiers Java | 16 |
| Tests | 2 fichiers, 13 cas |
| Endpoints REST | 6 |
| Schémas OpenAPI | 10+ |
| Règles métier | 3 |
| Fichiers de doc | 10 |
| Scripts de test | 2 |
| **Total fichiers** | **33** |

---

## 🔍 Cheat Sheet

### Compiler
```bash
mvn clean install
```

### Démarrer
```bash
mvn spring-boot:run
```

### Tester
```bash
mvn test
```

### Créer un aventurier
```bash
curl -X POST http://localhost:8080/api/v1/aventuriers \
  -H "Content-Type: application/json" \
  -d '{"nom":"Test","caracteristiques":{"physique":10,"mental":10,"perception":10},"niveau":1,"classe":"GUERRIER"}'
```

### Voir la liste
```bash
curl http://localhost:8080/api/v1/aventuriers
```

---

## 🆘 Dépannage

### Problème : Port 8080 déjà utilisé
**Solution** : Voir [QUICKSTART.md#Dépannage](QUICKSTART.md#dépannage)

### Problème : Tests échouent
**Solution** : `mvn clean test`

### Problème : Compilation échoue
**Solution** : `mvn clean` puis `mvn install`

### Problème : Base de données ne se remplit pas
**Solution** : Vérifier `data.sql` et `application.yaml`

---

## 📞 Support

| Question | Ressource |
|----------|-----------|
| Comment démarrer ? | [GETTING_STARTED.md](GETTING_STARTED.md) |
| Comment compiler ? | [QUICKSTART.md](QUICKSTART.md) |
| Qu'est-ce que l'API ? | [README.md](README.md) |
| Comment utiliser l'API ? | [API_DOCUMENTATION.md](API_DOCUMENTATION.md) |
| Des exemples ? | [CURL_EXAMPLES.md](CURL_EXAMPLES.md) |
| Détails techniques ? | [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md) |
| Vérifier l'implémentation ? | [CHECKLIST.md](CHECKLIST.md) |

---

## 📈 Parcours Recommandé

### 1. Premier Contact (15 min)
1. [GETTING_STARTED.md](GETTING_STARTED.md)
2. [README.md](README.md) - section "Aperçu"

### 2. Installation et Démarrage (20 min)
1. [QUICKSTART.md](QUICKSTART.md)
2. Compiler et démarrer l'app

### 3. Premiers Tests (10 min)
1. Consulter [CURL_EXAMPLES.md](CURL_EXAMPLES.md)
2. Copier une requête et tester

### 4. Comprendre l'API (30 min)
1. Lire [API_DOCUMENTATION.md](API_DOCUMENTATION.md) complètement
2. Essayer chaque endpoint

### 5. Comprendre l'Architecture (1 heure)
1. Lire [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)
2. Explorer le code source
3. Consulter [CHECKLIST.md](CHECKLIST.md)

---

## ✨ Points d'Entrée Principaux

### Pour une personne pressée (15 min)
→ [GETTING_STARTED.md](GETTING_STARTED.md) + [QUICKSTART.md](QUICKSTART.md)

### Pour un utilisateur d'API (30 min)
→ [API_DOCUMENTATION.md](API_DOCUMENTATION.md) + [CURL_EXAMPLES.md](CURL_EXAMPLES.md)

### Pour un développeur (2 heures)
→ Lire tout + explorer le code + tests

### Pour un architecte (1 heure)
→ [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md) + code source

---

## 🎓 Document Maître

**LIRE EN PREMIER : [GETTING_STARTED.md](GETTING_STARTED.md)** ⭐

Ce fichier vous orientera vers les ressources appropriées selon votre besoin.

---

## 📋 Table de Référence Rapide

| Besoin | Aller à |
|--------|---------|
| Démarrer l'API | [QUICKSTART.md](QUICKSTART.md) |
| Utiliser l'API | [API_DOCUMENTATION.md](API_DOCUMENTATION.md) |
| Exemples | [CURL_EXAMPLES.md](CURL_EXAMPLES.md) |
| Architecture | [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md) |
| Vérifier | [CHECKLIST.md](CHECKLIST.md) |
| Contrat | [openapi.yaml](openapi.yaml) |
| Code | `src/main/java/` |
| Tests | `src/test/java/` |

---

**Vous êtes prêt(e) ! Commencez par [GETTING_STARTED.md](GETTING_STARTED.md) 🚀**
