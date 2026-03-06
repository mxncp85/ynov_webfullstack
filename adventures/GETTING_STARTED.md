# 👋 Bienvenue - Où Commencer ?

Vous venez de recevoir une **API REST CRUD complète** pour la gestion des aventuriers. Voici comment l'explorer et l'utiliser.

---

## ⏱️ 5 Minutes pour Comprendre

### Ce que c'est ?
Une API REST qui permet de gérer une liste d'aventuriers avec :
- Création (niveau 1)
- Lecture (par ID ou liste)
- Mise à jour (complète ou partielle)
- Suppression

### Exemple Simple
```bash
# Créer un aventurier
curl -X POST http://localhost:8080/api/v1/aventuriers \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Aragorn",
    "caracteristiques": {"physique": 17, "mental": 14, "perception": 16},
    "niveau": 1,
    "classe": "RODEUR"
  }'

# Réponse :
{
  "id": "550e8400-...",
  "nom": "Aragorn",
  "niveau": 1,
  ...
}

# Augmenter le niveau
curl -X PATCH http://localhost:8080/api/v1/aventuriers/550e8400-... \
  -H "Content-Type: application/json" \
  -d '{"niveau": 2}'
```

---

## 📖 Lire dans Cet Ordre

### 1. **README.md** (5 min) 📄
Vue d'ensemble générale du projet
- Quoi ? Pourquoi ? Comment ?
- Règles métier
- Endpoints
- Architecture

### 2. **QUICKSTART.md** (10 min) 🚀
Guide de démarrage rapide
- Prérequis
- Compiler et démarrer
- Premiers tests
- Dépannage

### 3. **API_DOCUMENTATION.md** (15 min) 📚
Référence complète de l'API
- Tous les endpoints détaillés
- Paramètres et réponses
- Gestion des erreurs
- Codes HTTP

### 4. **CURL_EXAMPLES.md** (10 min) 💡
Exemples pratiques de requêtes
- Créer, lire, mettre à jour, supprimer
- Erreurs et comment les gérer
- Filtres et pagination

### 5. **IMPLEMENTATION_SUMMARY.md** (optionnel) 🏗️
Détails techniques pour développeurs
- Architecture
- Fichiers créés
- Technologies
- Règles métier en détail

---

## 🎯 Pour Les Impatients (15 minutes)

```bash
# 1. Ouvrir un terminal dans le dossier 'adventures'

# 2. Compiler
mvn clean install

# 3. Démarrer l'API
mvn spring-boot:run

# 4. Dans un autre terminal, tester
curl http://localhost:8080/api/v1/aventuriers

# 5. Voir 7 aventuriers de démarrage ! ✅
```

---

## 📁 Structure Importante

```
adventures/
├── README.md ..................... Vue générale
├── QUICKSTART.md ................. Démarrage rapide ⭐
├── API_DOCUMENTATION.md .......... Documentation API ⭐
├── CURL_EXAMPLES.md .............. Exemples ⭐
├── IMPLEMENTATION_SUMMARY.md ..... Détails techniques
├── CHECKLIST.md .................. Vérification
├── INVENTORY.md .................. Fichiers créés
├── COMPLETION_SUMMARY.md ......... Résumé final
├── openapi.yaml .................. Contrat API
├── pom.xml ....................... Dépendances Maven
├── src/main/
│   ├── java/com/ynov/adventures/
│   │   ├── controller/ ........... Endpoints REST
│   │   ├── service/ .............. Logique métier
│   │   ├── domain/ ............... Entités JPA
│   │   ├── dto/ .................. DTOs
│   │   ├── repository/ ........... Accès données
│   │   └── exception/ ............ Gestion erreurs
│   └── resources/
│       ├── application.yaml ....... Configuration
│       └── data.sql ............... Données de test
└── src/test/ ..................... Tests unitaires
```

---

## 🎯 Quick Facts

| Aspect | Détail |
|--------|--------|
| **Endpoints** | 6 (GET list, POST create, GET id, PUT update, PATCH partial, DELETE) |
| **Règles métier** | 3 (niveau 1 création, niveau ne descend pas, max +1) |
| **Validations** | Double layer (DTO + service) |
| **Codes HTTP** | 200, 201, 204, 400, 404, 422, 500 |
| **Erreurs** | Au format ProblemDetail |
| **Tests** | 13 cas couverts |
| **Documentation** | 8 fichiers Markdown |
| **Base de données** | H2 en mémoire (dev) |
| **Langage** | Java 25 |
| **Framework** | Spring Boot 4.0.3 |

---

## 🔥 Les 3 Règles à Retenir

### 1. Création au niveau 1
```
POST /aventuriers avec {"niveau": 1} ✅
POST /aventuriers avec {"niveau": 5} ❌ (422)
```

### 2. Le niveau ne descend jamais
```
PATCH niveau 3 → 2 ❌ (422)
PATCH niveau 3 → 3 ✅
PATCH niveau 3 → 4 ✅
```

### 3. Max +1 à la fois
```
PATCH niveau 3 → 4 ✅
PATCH niveau 3 → 5 ❌ (422)
PATCH niveau 3 → 6 ❌ (422)
```

---

## 💻 Tester en 1 Minute

```bash
# Terminal 1
mvn spring-boot:run

# Terminal 2
# Voir la liste
curl http://localhost:8080/api/v1/aventuriers

# Créer (copier-coller)
curl -X POST http://localhost:8080/api/v1/aventuriers \
  -H "Content-Type: application/json" \
  -d '{"nom":"Test","caracteristiques":{"physique":10,"mental":10,"perception":10},"niveau":1,"classe":"GUERRIER"}'

# Vous avez maintenant un aventurier ! 🎉
```

---

## ❓ Questions Fréquentes

### Q: Comment démarrer l'API ?
**A:** Lire [QUICKSTART.md](QUICKSTART.md)

### Q: Où trouver les exemples d'API ?
**A:** Voir [CURL_EXAMPLES.md](CURL_EXAMPLES.md)

### Q: Comment fonctionnent les règles métier ?
**A:** Consulter [API_DOCUMENTATION.md](API_DOCUMENTATION.md) section "Règles Métier"

### Q: Quel code quand je ne trouve pas une ressource ?
**A:** 404 Not Found (consulter [API_DOCUMENTATION.md](API_DOCUMENTATION.md))

### Q: Comment augmenter le niveau d'un aventurier ?
**A:** PATCH avec `{"niveau": currentLevel + 1}` (voir [CURL_EXAMPLES.md](CURL_EXAMPLES.md))

### Q: Qu'est-ce que ProblemDetail ?
**A:** Format standard pour les erreurs HTTP (voir [API_DOCUMENTATION.md](API_DOCUMENTATION.md))

---

## 🎓 Parcours d'Apprentissage

### Niveau 1 : Utilisateur (20 min)
- [ ] Lire README.md
- [ ] Lire QUICKSTART.md
- [ ] Tester avec curl
- [ ] Essayer CURL_EXAMPLES.md

### Niveau 2 : Développeur (1 heure)
- [ ] Lire API_DOCUMENTATION.md complètement
- [ ] Explorer le code Java
- [ ] Essayer les tests : `mvn test`
- [ ] Lire IMPLEMENTATION_SUMMARY.md

### Niveau 3 : Contributeur (2 heures)
- [ ] Lire tout le code source
- [ ] Comprendre les tests
- [ ] Modifier et tester
- [ ] Ajouter des fonctionnalités

---

## 🚀 5 Étapes pour Commencer

1. **Lire** README.md (5 min)
2. **Compiler** : `mvn clean install` (2 min)
3. **Démarrer** : `mvn spring-boot:run` (1 min)
4. **Tester** : Copier une requête de CURL_EXAMPLES.md (2 min)
5. **Lire** QUICKSTART.md pour plus (10 min)

**Total : ~20 minutes pour être fonctionnel** ✅

---

## 📞 Besoin d'Aide ?

### Pour démarrer
→ Voir **[QUICKSTART.md](QUICKSTART.md)**

### Pour l'API
→ Voir **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)**

### Pour des exemples
→ Voir **[CURL_EXAMPLES.md](CURL_EXAMPLES.md)**

### Pour la technique
→ Voir **[IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)**

### Pour vérifier tout
→ Voir **[CHECKLIST.md](CHECKLIST.md)**

---

## ✨ Le Plus Important

> **Cette implémentation est complète, testée, documentée et prête à l'emploi.**
>
> Vous pouvez l'utiliser immédiatement sans modification.

---

## 🎯 Prochaine Étape

**Ouvrez [QUICKSTART.md](QUICKSTART.md) maintenant pour démarrer ! 🚀**

---

*Bon développement ! 👨‍💻*
