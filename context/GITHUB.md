# GitHub Repository and Collaboration

## Canonical repository
- Repository: https://github.com/ezero2Aec/Dhaka-open-world
- Default branch: `main`
- Clean-source baseline: `2cdacf2` — `Initial clean source release`

The repository already exists. Do not create a second repository or recreate it.

## Repository contents
The clean repository contains Android Java/OpenGL ES source, Android resources and manifest, the Ant build file, the experimental Godot scaffold, README, and project documentation.

It intentionally excludes `.env`, credentials, signing keys, APKs, generated build output, downloaded Godot binaries, export-template archives, and caches. Do not re-add these files.

## Git workflow
1. Check `git status`, current branch, and `git log -5 --oneline`.
2. Fetch and inspect remote changes before starting work.
3. Work on `main` only when clean and synchronized, or use a short-lived feature branch for substantial changes.
4. Never discard user changes or force-push without explicit authorization.
5. Inspect the full diff and staged file list. Check for secrets, APKs, large files, caches, and generated outputs.
6. Commit with a descriptive message and push to the intended branch.
7. Report commit SHA and exact files changed.

## Secrets and authentication
- Never read or print secret values from `.env`.
- Never put tokens, passwords, private keys, or keystore files in Git.
- Use the authenticated GitHub integration or the user's secure local Git credential mechanism.
- Never ask the user to paste a personal access token into chat or commit it to a file.

## APK publication
Do not publish a placeholder or unverified APK. Before publishing, record APK filename and size, SHA-256, application ID and version, min/target SDK, signature verification, root-level `classes.dex` check, and installation/launch test results including device and Android version.

Publish a verified APK as a GitHub Release asset rather than committing generated binaries to the source tree. If testing is incomplete, label the artifact experimental and state exactly what remains unverified.
