# Development Rules

## Build System — Strict Requirement

DO NOT USE GRADLE.

Forbidden:
- gradlew
- gradlew.bat
- Gradle wrapper
- Gradle build scripts
- Gradle-based build pipelines

Choose an Android-compatible alternative build pipeline.

Possible tools, depending on the selected engine:
- Android SDK Build Tools
- AAPT2
- Java compiler
- D8
- Android NDK
- CMake
- Ninja
- APK signing tools
- Engine-specific Android export tools that do not invoke Gradle

Do not assume these tools are installed. Inspect the environment first.

If the selected engine requires Gradle for Android export, do not silently use it. Find a genuinely Gradle-free alternative or reconsider the engine.

## Development Requirements

1. Inspect the existing project before making changes.
2. Choose an engine that supports Android APK output without Gradle.
3. Build a small playable MVP first.
4. Test every major feature.
5. Fix build errors before expanding.
6. Preserve existing work.
7. Use original or properly licensed assets.
8. Keep source code organized.
9. Document all build commands.
10. Never claim success without testing.
11. Never commit secrets or .env files.
12. Optimize for real Android hardware.
