# 🚀 Quick Fix for Java 25 Error

## The Error You Got
```
java: error: release version 25 not supported
Module adventures SDK 17 is not compatible with the source version 25.
```

## Quick Fix (3 Steps)

### Step 1: Run the cleanup script
```cmd
FIX_JAVA_25_ISSUE.cmd
```
Press Enter and wait for it to complete.

### Step 2: Close and reopen IntelliJ
- Close IntelliJ IDEA completely
- Wait 5 seconds
- Reopen IntelliJ IDEA

### Step 3: Reload Maven projects
- Wait for IntelliJ to finish indexing
- Right-click on the project root
- Select: Maven → Reload Projects
- OR: Menu → Maven → Reload All Maven Projects

## That's it! ✅

Your project should now compile with Java 17 instead of Java 25.

## What was fixed?
The pom.xml was updated to explicitly tell Maven to compile with Java 17:
- Added `<release>17</release>` to maven-compiler-plugin
- Added `<maven.compiler.release>17</maven.compiler.release>` property
- Reset IntelliJ IDE state to prevent cached configuration issues

## Verify it works
Run this command:
```cmd
adventures\mvnw.cmd clean compile
```

Should complete without the "release version 25" error.

## Need more details?
See: **JAVA_25_TO_17_FIX.md**
