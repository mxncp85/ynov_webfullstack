# 🚀 Adventures API - Guide Rapide

## ✅ Solution 100% H2 (En Mémoire)

**Aucune installation externe nécessaire !**
- ✅ Pas de MongoDB
- ✅ Pas de Docker
- ✅ Tout en RAM avec H2

---

## 🏗️ Démarrage en 2 Commandes

```cmd
mvnw.cmd clean install -DskipTests
mvnw.cmd spring-boot:run
```

**C'est tout ! L'application est prête.**

---

## 🔍 Console H2 - Tout en Un

**URL:** http://localhost:8080/h2-console

**Connexion:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: *(laisser vide)*

### Tables Disponibles

#### 1. `aventuriers` - Données métier
```sql
SELECT * FROM aventuriers;
```
7 aventuriers pré-chargés (Aragorn, Legolas, Gimli, etc.)

#### 2. `api_logs` - Logs d'API
```sql
-- Tous les logs
SELECT * FROM api_logs ORDER BY timestamp DESC;

-- Statistiques
SELECT log_level, COUNT(*) FROM api_logs GROUP BY log_level;

-- Erreurs
SELECT * FROM api_logs WHERE success = false;
```

---

## 🧪 Test Rapide

```cmd
curl http://localhost:8080/api/v1/aventuriers
```

Puis vérifier dans H2 Console:
```sql
SELECT * FROM api_logs ORDER BY timestamp DESC LIMIT 1;
```

---

## 📊 Endpoints API

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/v1/aventuriers` | Liste tous (pagination + filtres) |
| POST | `/api/v1/aventuriers` | Créer un aventurier |
| GET | `/api/v1/aventuriers/{id}` | Obtenir par ID |
| PUT | `/api/v1/aventuriers/{id}` | Mise à jour complète |
| PATCH | `/api/v1/aventuriers/{id}` | Mise à jour partielle |
| DELETE | `/api/v1/aventuriers/{id}` | Supprimer |

---

## 🎯 Fonctionnalités

### SQL (Table `aventuriers`)
- ✅ Base H2 en mémoire
- ✅ CRUD complet
- ✅ Pagination & filtres
- ✅ Validation métier
- ✅ 7 aventuriers pré-chargés

### Logging (Table `api_logs`)
- ✅ Tous les appels API loggés
- ✅ Paramètres entrée/sortie
- ✅ Temps d'exécution
- ✅ Niveaux: INFO/WARN/ERROR
- ✅ Console + H2

---

## 📚 Documentation Complète

- **H2_FINAL_IMPLEMENTATION.md** - Guide détaillé
- **H2_COMPLETE_GUIDE.md** - Requêtes SQL utiles
- **test_h2_complete.ps1** - Tests automatiques

---

## ✨ Avantages

1. **Simple** - Aucune dépendance externe
2. **Rapide** - Démarre en secondes
3. **Propre** - Reset automatique à chaque démarrage
4. **Visible** - Console H2 pour tout voir
5. **Complet** - Données + Logs au même endroit

---

**🎉 Profitez de votre API !**
