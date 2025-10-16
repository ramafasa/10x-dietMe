# ktlint Configuration Guide

## Overview

ktlint has been configured with **strict validation** for the DietMe project. It enforces the official Kotlin coding conventions and includes experimental rules for enhanced code quality.

## Gradle Tasks

### Check code style
```bash
./gradlew ktlintCheck
```
Validates all Kotlin files against ktlint rules. Fails the build if violations are found.

### Auto-fix violations
```bash
./gradlew ktlintFormat
```
Automatically fixes most code style violations. Run this before committing code.

### Check specific source set
```bash
./gradlew ktlintMainSourceSetCheck    # Check main sources only
./gradlew ktlintTestSourceSetCheck    # Check test sources only
```

## Configuration

### Location: `build.gradle.kts`
- ktlint version: **1.5.0**
- Experimental rules: **enabled**
- Fail on violations: **yes**
- Auto-generated code: **excluded**

### Location: `.editorconfig`
Strict rules enabled:
- No wildcard imports
- No unused imports
- No trailing spaces
- No consecutive blank lines
- Max line length: 120 characters
- Trailing commas required
- Proper indentation (tabs, 4 spaces)
- Function/class signature formatting

## CI/CD Integration

### GitHub Actions (`.github/workflows/ci.yml`)
- Runs `ktlintCheck` on every push and PR
- Blocks merges if violations exist
- Uploads ktlint reports as artifacts on failure

## Git Pre-commit Hook (Optional)

### Install the hook
```bash
./scripts/install-git-hooks.sh
```

This will:
- Run ktlint check before every commit
- Prevent commits with code style violations
- Suggest running `ktlintFormat` to fix issues

### Bypass the hook (not recommended)
```bash
git commit --no-verify
```

## IDE Integration

### IntelliJ IDEA / Android Studio
1. Install the "ktlint" plugin from marketplace
2. The `.editorconfig` file will be automatically recognized
3. Enable "Format on Save" (optional):
   - Settings → Tools → Actions on Save
   - Enable "Reformat code"

### VS Code
1. Install "Kotlin Language" extension
2. Install "EditorConfig for VS Code" extension
3. ktlint will use the `.editorconfig` automatically

## Common Violations & Fixes

### No wildcard imports
❌ `import kotlin.collections.*`
✅ `import kotlin.collections.List`

### Trailing commas
❌ `data class User(val name: String, val age: Int)`
✅ `data class User(val name: String, val age: Int,)`

### No blank lines before closing brace
❌
```kotlin
class User {
    val name: String

}
```
✅
```kotlin
class User {
    val name: String
}
```

### Max line length
Lines should not exceed 120 characters. Break long lines:
```kotlin
// ❌ Too long
fun longFunction(param1: String, param2: String, param3: String, param4: String, param5: String): String { ... }

// ✅ Properly wrapped
fun longFunction(
    param1: String,
    param2: String,
    param3: String,
    param4: String,
    param5: String,
): String { ... }
```

## Reports

ktlint generates reports in:
```
build/reports/ktlint/
├── ktlintMainSourceSetCheck/
│   └── ktlintMainSourceSetCheck.txt
└── ktlintTestSourceSetCheck/
    └── ktlintTestSourceSetCheck.txt
```

## Troubleshooting

### "Task failed with exit code 1"
Run `./gradlew ktlintFormat` to auto-fix most issues.

### False positives
If ktlint flags valid code, you can disable specific rules in `.editorconfig`:
```
ktlint_standard_some-rule = disabled
```

### IDE formatting conflicts
Ensure your IDE uses the `.editorconfig` settings and not custom formatting rules.

## Best Practices

1. Run `./gradlew ktlintFormat` before committing
2. Install the pre-commit hook for automatic validation
3. Configure your IDE to use `.editorconfig`
4. Review ktlint reports in CI/CD before merging PRs
5. Never use `--no-verify` to skip checks (except emergencies)

## Resources

- [ktlint Documentation](https://pinterest.github.io/ktlint/)
- [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- [EditorConfig Specification](https://editorconfig.org/)
