# ✅ Java 25 to Java 17 Compatibility Fix

## Problem
IntelliJ IDEA was reporting:
```
java: error: release version 25 not supported
Module adventures SDK 17 is not compatible with the source version 25.
```

## Root Cause
The IDE's internal compiler configuration had the source version set to Java 25, while the module SDK was Java 17. This mismatch prevented compilation.

## Solution Applied

### 1. Updated pom.xml
Added explicit Java version configuration to ensure Maven uses Java 17:

**Changes:**
- Added property: `<maven.compiler.release>17</maven.compiler.release>`
- Updated maven-compiler-plugin to include: `<release>17</release>`
- Set source and target to 17

**Why this works:**
- The `<release>` parameter is the most important. It tells Maven to compile code compatible with Java 17 release
- This prevents any ambiguity about which Java version to use
- Maven Compiler Plugin 3.11.0 properly handles the release flag

### 2. Reset IntelliJ Configuration
- Simplified `workspace.xml` to remove cached IDE state
- Verified `misc.xml` has `languageLevel="JDK_17"` and `project-jdk-name="17"`
- Verified `compiler.xml` has `target="17"`

### 3. Cleanup Required
Run the provided script: **FIX_JAVA_25_ISSUE.cmd**

This script:
1. Removes IntelliJ caches
2. Removes build artifacts
3. Resets project state
4. Runs Maven clean
5. Prepares for fresh reload

## How to Apply the Fix

### Option A: Automatic (Recommended)
```cmd
FIX_JAVA_25_ISSUE.cmd
```

### Option B: Manual
1. Close IntelliJ IDEA completely
2. Delete:
   - `.idea/caches/` folder
   - `.idea/shelf/` folder
   - `target/` folder
   - `adventures/target/` folder
   - `out/` folder
3. Reopen IntelliJ IDEA
4. Wait for indexing to complete
5. Right-click project → Maven → Reload Projects

## Verification
After applying the fix:

1. **Check Maven Compiler Plugin:**
   ```bash
   adventures/mvnw.cmd clean compile
   ```
   Should compile without "release version 25" error

2. **Check IntelliJ:**
   - File → Project Structure → Project
   - Verify: Project SDK = 17
   - Verify: Project language level = 17
   
   - File → Project Structure → Modules → adventures
   - Verify: Module SDK = 17
   - Verify: Language level = 17

3. **Build the project:**
   - Maven → Reload All Maven Projects
   - Run → Build Project
   - Should complete without errors

## Files Modified
1. `adventures/pom.xml` - Updated maven-compiler-plugin and properties
2. `.idea/workspace.xml` - Reset IDE state

## Files Created
1. `FIX_JAVA_25_ISSUE.cmd` - Automated cleanup script

## Why Java 17?
- Spring Boot 3.4.3 (used in this project) officially supports Java 17+
- Java 17 is an LTS (Long Term Support) release
- Java 25 is a preview/development version and not stable for production
- The project's pom.xml is configured for Java 17

## Additional Notes
- No code changes required
- No dependency updates needed
- The OpenAPI generator will work correctly with Java 17
- H2 database and all other dependencies are compatible with Java 17

## Troubleshooting
If you still see the error after applying the fix:

1. **Invalidate Caches:**
   - File → Invalidate Caches → Invalidate and Restart

2. **Check Environment:**
   - Open terminal
   - Run: `java -version`
   - Should show Java 17.x

3. **Reimport Project:**
   - Delete `.idea` folder completely
   - Open `adventures/pom.xml` as a project
   - Select "Open as Project"
   - Let IntelliJ rebuild from scratch

4. **Nuclear Option:**
   - Run: `NUCLEAR_CLEAN.cmd`
   - Delete entire `.idea` folder
   - Restart IntelliJ
   - Reimport the project

---
**Last Updated:** March 6, 2026
