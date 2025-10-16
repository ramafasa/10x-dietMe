#!/bin/bash

# Script to install git pre-commit hook for ktlint

HOOK_DIR=".git/hooks"
HOOK_FILE="$HOOK_DIR/pre-commit"

# Create hooks directory if it doesn't exist
mkdir -p "$HOOK_DIR"

# Create pre-commit hook
cat > "$HOOK_FILE" << 'EOF'
#!/bin/bash

echo "Running ktlint check..."

# Run ktlint check
./gradlew ktlintCheck --daemon

# Capture exit code
KTLINT_EXIT_CODE=$?

if [ $KTLINT_EXIT_CODE -ne 0 ]; then
    echo ""
    echo "❌ ktlint found code style violations!"
    echo ""
    echo "To fix automatically, run: ./gradlew ktlintFormat"
    echo "To skip this check, use: git commit --no-verify"
    echo ""
    exit 1
fi

echo "✅ ktlint check passed!"
exit 0
EOF

# Make the hook executable
chmod +x "$HOOK_FILE"

echo "✅ Git pre-commit hook installed successfully!"
echo ""
echo "The hook will run ktlint check before every commit."
echo "To bypass the hook, use: git commit --no-verify"
