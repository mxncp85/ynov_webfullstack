# 🚀 Guide de Démarrage - Version H2 Complète

## ✅ Configuration 100% H2 (En Mémoire)

Votre projet utilise **uniquement H2** pour:
- ✅ Base de données SQL (aventuriers)
- ✅ Base de données pour les logs (api_logs)
- ✅ Pas besoin d'installer MongoDB ou autre !

## 🏗️ Build du Projet

```cmd
mvnw.cmd clean install -DskipTests
```

## 🎯 Démarrage de l'Application

```cmd
mvnw.cmd spring-boot:run
```

C'est tout ! Pas besoin de démarrer MongoDB ou autre service.

## 🔍 Console H2 - Tout en un seul endroit

### Accès
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (vide)

### Tables Disponibles

#### 1. Table `aventuriers` (Données métier)
```sql
SELECT * FROM aventuriers;
```
Contient les 7 aventuriers pré-chargés.

#### 2. Table `api_logs` (Logs d'API)
```sql
-- Tous les logs
SELECT * FROM api_logs ORDER BY timestamp DESC;

-- Logs par niveau
SELECT * FROM api_logs WHERE log_level = 'INFO';
SELECT * FROM api_logs WHERE log_level = 'WARN';
SELECT * FROM api_logs WHERE log_level = 'ERROR';

-- Logs d'erreurs
SELECT * FROM api_logs WHERE success = false;

-- Logs pour un endpoint spécifique
SELECT * FROM api_logs WHERE endpoint LIKE '%aventuriers%';

-- Statistiques
SELECT log_level, COUNT(*) as count 
FROM api_logs 
GROUP BY log_level;

-- Temps d'exécution moyen par endpoint
SELECT endpoint, AVG(execution_time_ms) as avg_time, COUNT(*) as count
FROM api_logs 
GROUP BY endpoint;
```

## 🧪 Tests Rapides

### 1. Lister les aventuriers
```cmd
curl http://localhost:8080/api/v1/aventuriers
```

### 2. Créer un aventurier
```cmd
curl -X POST http://localhost:8080/api/v1/aventuriers ^
-H "Content-Type: application/json" ^
-d "{\"nom\":\"Merlin\",\"description\":\"Magicien\",\"caracteristiques\":{\"physique\":10,\"mental\":20,\"perception\":18},\"classe\":\"MAGE\"}"
```

### 3. Vérifier les logs dans H2
1. Ouvrir http://localhost:8080/h2-console
2. Exécuter:
```sql
SELECT method, endpoint, status_code, success, execution_time_ms 
FROM api_logs 
ORDER BY timestamp DESC 
LIMIT 10;
```

## 📊 Structure des Tables

### Table `aventuriers`
| Colonne | Type | Description |
|---------|------|-------------|
| id | UUID | Identifiant unique |
| nom | VARCHAR(100) | Nom de l'aventurier |
| description | VARCHAR(1000) | Description |
| niveau | INTEGER | Niveau (1-20) |
| classe | VARCHAR(20) | GUERRIER, MAGE, etc. |
| caracteristiques_physique | INTEGER | Force (1-20) |
| caracteristiques_mental | INTEGER | Intelligence (1-20) |
| caracteristiques_perception | INTEGER | Perception (1-20) |
| created_at | TIMESTAMP | Date de création |
| updated_at | TIMESTAMP | Dernière modification |

### Table `api_logs`
| Colonne | Type | Description |
|---------|------|-------------|
| id | BIGINT | Identifiant auto-incrémenté |
| timestamp | TIMESTAMP | Date/heure du log |
| method | VARCHAR(10) | GET, POST, PUT, PATCH, DELETE |
| endpoint | VARCHAR(500) | Pattern de l'endpoint |
| uri | VARCHAR(500) | URI complète |
| path_variables | VARCHAR(2000) | Variables de chemin (JSON) |
| request_params | VARCHAR(2000) | Paramètres de requête (JSON) |
| request_body | TEXT | Corps de la requête (JSON) |
| response_body | TEXT | Corps de la réponse (JSON) |
| status_code | INTEGER | Code HTTP (200, 404, etc.) |
| log_level | VARCHAR(10) | INFO, WARN, ERROR |
| error_message | TEXT | Message d'erreur si applicable |
| execution_time_ms | BIGINT | Temps d'exécution en ms |
| success | BOOLEAN | true si succès, false si erreur |

## 🎯 Endpoints API

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/v1/aventuriers` | Liste paginée + filtres |
| POST | `/api/v1/aventuriers` | Créer un aventurier |
| GET | `/api/v1/aventuriers/{id}` | Obtenir par ID |
| PUT | `/api/v1/aventuriers/{id}` | Mise à jour complète |
| PATCH | `/api/v1/aventuriers/{id}` | Mise à jour partielle |
| DELETE | `/api/v1/aventuriers/{id}` | Supprimer |

## 📈 Exemples de Requêtes SQL Utiles

### Statistiques des Logs

```sql
-- Nombre total de requêtes
SELECT COUNT(*) as total_requests FROM api_logs;

-- Taux de succès
SELECT 
    SUM(CASE WHEN success = true THEN 1 ELSE 0 END) as success_count,
    SUM(CASE WHEN success = false THEN 1 ELSE 0 END) as error_count,
    ROUND(100.0 * SUM(CASE WHEN success = true THEN 1 ELSE 0 END) / COUNT(*), 2) as success_rate
FROM api_logs;

-- Top 5 endpoints les plus lents
SELECT endpoint, MAX(execution_time_ms) as max_time, AVG(execution_time_ms) as avg_time
FROM api_logs
GROUP BY endpoint
ORDER BY avg_time DESC
LIMIT 5;

-- Logs des dernières 5 minutes
SELECT * FROM api_logs 
WHERE timestamp > DATEADD('MINUTE', -5, CURRENT_TIMESTAMP())
ORDER BY timestamp DESC;

-- Erreurs par type
SELECT log_level, status_code, COUNT(*) as count
FROM api_logs
WHERE success = false
GROUP BY log_level, status_code;
```

### Statistiques des Aventuriers

```sql
-- Aventuriers par classe
SELECT classe, COUNT(*) as count
FROM aventuriers
GROUP BY classe;

-- Moyenne des niveaux par classe
SELECT classe, AVG(niveau) as niveau_moyen
FROM aventuriers
GROUP BY classe;

-- Top 5 aventuriers les plus puissants
SELECT nom, classe, niveau, 
       (caracteristiques_physique + caracteristiques_mental + caracteristiques_perception) as total_stats
FROM aventuriers
ORDER BY total_stats DESC
LIMIT 5;
```

## ✨ Avantages de la Solution H2 Complète

1. ✅ **Pas d'installation externe** - Tout en mémoire
2. ✅ **Console unifiée** - Tout visible au même endroit
3. ✅ **Requêtes SQL puissantes** - Analyse facile des logs
4. ✅ **Redémarrage propre** - Données réinitialisées à chaque démarrage
5. ✅ **Parfait pour le développement** - Rapide et simple
6. ✅ **Tests automatiques** - Pas de dépendances externes

## 🔄 Cycle de Vie des Données

### Au Démarrage
1. H2 crée automatiquement les tables (DDL auto-create)
2. `data.sql` charge les 7 aventuriers pré-définis
3. La table `api_logs` est créée vide

### Pendant l'Exécution
1. Chaque appel API est loggé dans `api_logs`
2. Les opérations CRUD modifient `aventuriers`
3. Tout est visible en temps réel dans H2 Console

### À l'Arrêt
1. Toutes les données sont perdues (mémoire RAM)
2. Au prochain démarrage, tout redémarre proprement

## 🐛 Dépannage

### H2 Console ne s'affiche pas
**Vérifier:** application.yaml contient:
```yaml
spring:
  h2:
    console:
      enabled: true
```

### Tables non créées
**Vérifier:** application.yaml contient:
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create-drop
```

### Données non chargées
**Vérifier:** application.yaml contient:
```yaml
spring:
  sql:
    init:
      mode: always
```

## 🎉 Commandes Essentielles

```cmd
# Build
mvnw.cmd clean install -DskipTests

# Démarrer
mvnw.cmd spring-boot:run

# Tester
curl http://localhost:8080/api/v1/aventuriers

# Console H2
http://localhost:8080/h2-console
```

---

**Tout est maintenant en H2 ! Simple, rapide, efficace. Pas d'installation externe nécessaire !** 🎊
