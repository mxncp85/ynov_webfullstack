# 🎯 TL;DR - Version Ultra-Courte

## Ce qui a été fait
✅ MongoDB **RETIRÉ**  
✅ H2 utilisé pour **TOUT** (données + logs)  
✅ Aucune installation externe nécessaire

## Démarrer
```cmd
mvnw.cmd clean install -DskipTests
mvnw.cmd spring-boot:run
```

## Console H2
http://localhost:8080/h2-console
- URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Pass: *(vide)*

## Tables
```sql
SELECT * FROM aventuriers;  -- 7 aventuriers
SELECT * FROM api_logs;     -- Logs d'API
```

## Test
```cmd
curl http://localhost:8080/api/v1/aventuriers
```

## Documentation
- **START_HERE.md** → Commencer ici
- **INDEX_DOCUMENTATION.md** → Tout trouver

**C'est tout ! 🎉**
