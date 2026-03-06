# Exemples de requêtes cURL pour l'API des Aventuriers

## 1. Créer un aventurier

### Créer un nouvel aventurier (niveau 1)
```bash
curl -X POST http://localhost:8080/api/v1/aventuriers \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Aragorn",
    "description": "Héritier du trône du Gondor, rôdeur des terres sauvages.",
    "caracteristiques": {
      "physique": 17,
      "mental": 14,
      "perception": 16
    },
    "niveau": 1,
    "classe": "RODEUR"
  }'
```

### Réponse réussie (201 Created)
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "nom": "Aragorn",
  "description": "Héritier du trône du Gondor, rôdeur des terres sauvages.",
  "caracteristiques": {
    "physique": 17,
    "mental": 14,
    "perception": 16
  },
  "niveau": 1,
  "classe": "RODEUR",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Erreur : Niveau initial incorrect (422)
```bash
curl -X POST http://localhost:8080/api/v1/aventuriers \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Gandalf",
    "caracteristiques": {
      "physique": 10,
      "mental": 18,
      "perception": 15
    },
    "niveau": 5,
    "classe": "MAGE"
  }'
```

Réponse:
```json
{
  "type": "about:blank",
  "title": "Erreur de validation métier",
  "status": 422,
  "detail": "Un aventurier nouvellement créé doit commencer au niveau 1"
}
```

---

## 2. Lister les aventuriers

### Lister tous les aventuriers (page 1, 20 par page)
```bash
curl -X GET http://localhost:8080/api/v1/aventuriers
```

### Lister avec pagination personnalisée
```bash
curl -X GET "http://localhost:8080/api/v1/aventuriers?page=2&limit=10"
```

### Filtrer par classe
```bash
curl -X GET "http://localhost:8080/api/v1/aventuriers?classe=GUERRIER"
```

### Filtrer par niveau
```bash
curl -X GET "http://localhost:8080/api/v1/aventuriers?niveau_min=5&niveau_max=10"
```

### Combinaison de filtres
```bash
curl -X GET "http://localhost:8080/api/v1/aventuriers?classe=MAGE&niveau_min=3&niveau_max=8&page=1&limit=5"
```

---

## 3. Récupérer un aventurier

### Par ID
```bash
curl -X GET http://localhost:8080/api/v1/aventuriers/550e8400-e29b-41d4-a716-446655440000
```

### Erreur : Non trouvé (404)
```bash
curl -X GET http://localhost:8080/api/v1/aventuriers/00000000-0000-0000-0000-000000000000
```

Réponse:
```json
{
  "type": "about:blank",
  "title": "Aventurier non trouvé",
  "status": 404,
  "detail": "Aventurier non trouvé avec l'ID : 00000000-0000-0000-0000-000000000000"
}
```

---

## 4. Mettre à jour un aventurier (PUT)

### Mise à jour complète
```bash
curl -X PUT http://localhost:8080/api/v1/aventuriers/550e8400-e29b-41d4-a716-446655440000 \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Aragorn le Grand",
    "description": "Le Roi qui a réuni les royaumes.",
    "caracteristiques": {
      "physique": 18,
      "mental": 15,
      "perception": 17
    },
    "niveau": 2,
    "classe": "RODEUR"
  }'
```

### Erreur : Baisse de niveau (422)
```bash
curl -X PUT http://localhost:8080/api/v1/aventuriers/550e8400-e29b-41d4-a716-446655440000 \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Aragorn",
    "caracteristiques": {
      "physique": 17,
      "mental": 14,
      "perception": 16
    },
    "niveau": 1,
    "classe": "RODEUR"
  }'
```

Réponse:
```json
{
  "type": "about:blank",
  "title": "Erreur de validation métier",
  "status": 422,
  "detail": "Le niveau ne peut jamais descendre. Niveau actuel : 2, Niveau demandé : 1"
}
```

### Erreur : Augmentation de plus de 1 niveau (422)
```bash
curl -X PUT http://localhost:8080/api/v1/aventuriers/550e8400-e29b-41d4-a716-446655440000 \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Aragorn",
    "caracteristiques": {
      "physique": 17,
      "mental": 14,
      "perception": 16
    },
    "niveau": 5,
    "classe": "RODEUR"
  }'
```

Réponse:
```json
{
  "type": "about:blank",
  "title": "Erreur de validation métier",
  "status": 422,
  "detail": "Le niveau ne peut pas monter de plus de 1 à la fois. Niveau actuel : 2, Niveau demandé : 5"
}
```

---

## 5. Mettre à jour partiellement un aventurier (PATCH)

### Augmenter le niveau de 1
```bash
curl -X PATCH http://localhost:8080/api/v1/aventuriers/550e8400-e29b-41d4-a716-446655440000 \
  -H "Content-Type: application/json" \
  -d '{
    "niveau": 3
  }'
```

### Modifier uniquement les caractéristiques
```bash
curl -X PATCH http://localhost:8080/api/v1/aventuriers/550e8400-e29b-41d4-a716-446655440000 \
  -H "Content-Type: application/json" \
  -d '{
    "caracteristiques": {
      "physique": 19,
      "perception": 18
    }
  }'
```

### Modifier plusieurs champs
```bash
curl -X PATCH http://localhost:8080/api/v1/aventuriers/550e8400-e29b-41d4-a716-446655440000 \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Aragorn II",
    "description": "Roi du Gondor et d'Arnor",
    "niveau": 4,
    "classe": "PALADIN"
  }'
```

---

## 6. Supprimer un aventurier

### Suppression réussie
```bash
curl -X DELETE http://localhost:8080/api/v1/aventuriers/550e8400-e29b-41d4-a716-446655440000
```

Réponse: `204 No Content` (pas de contenu)

### Erreur : Aventurier introuvable (404)
```bash
curl -X DELETE http://localhost:8080/api/v1/aventuriers/00000000-0000-0000-0000-000000000000
```

Réponse:
```json
{
  "type": "about:blank",
  "title": "Aventurier non trouvé",
  "status": 404,
  "detail": "Aventurier non trouvé avec l'ID : 00000000-0000-0000-0000-000000000000"
}
```

---

## 7. Erreurs de validation des données

### Champ obligatoire manquant (400)
```bash
curl -X POST http://localhost:8080/api/v1/aventuriers \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Gandalf"
  }'
```

Réponse:
```json
{
  "type": "about:blank",
  "title": "Erreur de validation",
  "status": 400,
  "detail": "Les données fournies sont invalides",
  "fieldErrors": {
    "caracteristiques": "Les caractéristiques sont obligatoires",
    "niveau": "Le niveau est obligatoire",
    "classe": "La classe est obligatoire"
  }
}
```

### Valeurs invalides pour les caractéristiques (400)
```bash
curl -X POST http://localhost:8080/api/v1/aventuriers \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Gandalf",
    "caracteristiques": {
      "physique": 25,
      "mental": 18,
      "perception": 15
    },
    "niveau": 1,
    "classe": "MAGE"
  }'
```

Réponse:
```json
{
  "type": "about:blank",
  "title": "Erreur de validation",
  "status": 400,
  "detail": "Les données fournies sont invalides",
  "fieldErrors": {
    "caracteristiques.physique": "La valeur doit être comprise entre 1 et 20"
  }
}
```

### Nom trop court (400)
```bash
curl -X POST http://localhost:8080/api/v1/aventuriers \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "G",
    "caracteristiques": {
      "physique": 10,
      "mental": 18,
      "perception": 15
    },
    "niveau": 1,
    "classe": "MAGE"
  }'
```

Réponse:
```json
{
  "type": "about:blank",
  "title": "Erreur de validation",
  "status": 400,
  "detail": "Les données fournies sont invalides",
  "fieldErrors": {
    "nom": "Le nom doit faire entre 2 et 100 caractères"
  }
}
```

---

## Notes

- Remplacez `http://localhost:8080` par l'URL appropriée de votre serveur
- Utilisez `--verbose` avec curl pour voir les headers complets
- Les IDs utilisés ici sont des exemples ; utilisez les IDs réels retournés par le serveur
