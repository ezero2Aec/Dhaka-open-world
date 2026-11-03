# Technical Decisions Log

## Build system
- **Decision**: Use Ant with the provided `app/build.xml` for all Android builds.
- **Reason**: The project historically uses a manual Ant pipeline; Gradle is prohibited by project policy.
- **Implication**: All developers must have Ant, Java 8, and the Android SDK installed on Termux.

## Dependency management
- **Decision**: No external Gradle dependencies, no Worktrunk, no proot‑distro, no additional Linux distributions.
- **Reason**: Keep the environment lightweight on Android/Termux.

## Source of truth
- **Decision**: The `context/` directory holds all documentation, architecture, roadmap, and status information.
- **Reason**: Guarantees that future AI sessions have a single authoritative source.

## Credentials handling
- **Decision**: Store any development secrets in a root‑level `.env` file, never commit it, and provide a `.env.example` with placeholder keys.
- **Reason**: Prevent accidental leakage of API keys or signing credentials.

## Godot scaffold
- **Decision**: Preserve the `godot_project/` directory unchanged; do not delete or overwrite it.
- **Reason**: It may be needed for future asset creation or engine migration.

## APK generation
- **Decision**: Generate a debug APK via `ant debug`; do not modify the signing process.
- **Reason**: The build script already signs with the default debug keystore, satisfying the “no secret exposure” rule.

## Git workflow
- **Decision**: Consolidate all work onto the `main` branch; delete obsolete branches only after confirming no unique work is lost.
- **Reason**: Simplifies history and avoids accidental loss of work.

*All decisions are recorded here for future reference and must be kept in sync with the codebase.*
