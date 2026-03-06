# 🐛 Column Name Fix - @Embeddable Fields

## Problem
```
org.h2.jdbc.JdbcSQLSyntaxErrorException: Column "CARACTERISTIQUES_PHYSIQUE" not found
```

The INSERT statement was using wrong column names for the embedded Caracteristiques object.

---

## Root Cause

The Aventurier entity uses `@Embeddable` for Caracteristiques:

```java
@Embedded
private Caracteristiques caracteristiques;
```

When a class is `@Embeddable`, its fields are embedded directly into the parent table with their own column names, NOT prefixed with the field name.

### Actual Table Structure
```
Table: aventuriers
├── id (UUID, Primary Key)
├── nom (VARCHAR)
├── description (VARCHAR)
├── niveau (INTEGER)
├── classe (VARCHAR)
├── physique (INTEGER)          ← From Caracteristiques
├── mental (INTEGER)             ← From Caracteristiques
├── perception (INTEGER)         ← From Caracteristiques
├── created_at (TIMESTAMP)
└── updated_at (TIMESTAMP)
```

### What The Insert Was Using (WRONG)
```
caracteristiques_physique   ❌
caracteristiques_mental     ❌
caracteristiques_perception ❌
```

### What It Should Use (CORRECT)
```
physique        ✅
mental          ✅
perception      ✅
```

---

## Solution Applied ✅

Updated `src/main/resources/import.sql` to use correct column names:

```sql
-- BEFORE (WRONG):
INSERT INTO aventuriers (id, nom, ..., caracteristiques_physique, caracteristiques_mental, ...)

-- AFTER (CORRECT):
INSERT INTO aventuriers (id, nom, ..., physique, mental, perception, ...)
```

---

## Understanding @Embeddable

### How @Embeddable Works

When you have:
```java
@Entity
public class Aventurier {
    @Embedded
    private Caracteristiques caracteristiques;
}

@Embeddable
public class Caracteristiques {
    private Integer physique;
    private Integer mental;
    private Integer perception;
}
```

The database table created is:
```sql
CREATE TABLE aventuriers (
    id UUID PRIMARY KEY,
    physique INTEGER,      -- NOT caracteristiques_physique
    mental INTEGER,        -- NOT caracteristiques_mental
    perception INTEGER,    -- NOT caracteristiques_perception
    ...
)
```

### Key Points
- ✅ @Embeddable fields are flattened into the parent table
- ✅ Column names use the field names directly
- ✅ No prefix is added automatically
- ✅ You CAN customize with @AttributeOverride if needed

---

## Testing the Fix

### Step 1: Clean Build
```bash
cd C:\Users\ASUS\Downloads\adventures\adventures
mvnw clean compile
```

### Step 2: Run Application
```bash
mvnw spring-boot:run
```

### Expected Output
```
✅ Application started successfully
✅ Tomcat started on port(s): 8080
✅ No SQL errors
```

### Step 3: Verify Data
```bash
curl http://localhost:8080/aventuriers
```

Should return JSON with 7 adventurers!

---

## File Changed

**`src/main/resources/import.sql`**
- Changed column names from `caracteristiques_*` to actual field names
- All 7 test records updated with correct syntax

---

## Status ✅

**Column Names Fixed!**
- The INSERT statement now uses correct column names
- Application should start successfully
- Data will load properly
- API will return adventurers

---

## Additional Notes

### If You Want Custom Column Names
You can customize @Embeddable column names using @AttributeOverride:

```java
@Entity
public class Aventurier {
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "physique", column = @Column(name = "force")),
        @AttributeOverride(name = "mental", column = @Column(name = "intelligence")),
        @AttributeOverride(name = "perception", column = @Column(name = "sagesse"))
    })
    private Caracteristiques caracteristiques;
}
```

But the current code doesn't use this, so we use the direct field names.

---

**Next Step:** Run `mvnw spring-boot:run` again!

Your application should now start successfully! 🚀
