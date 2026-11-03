# Agent Startup Instructions

## Project identity
Dhaka Open World is an Android-first, low-poly, third-person open-world game inspired by Dhaka, Bangladesh.

- Canonical repository: https://github.com/ezero2Aec/Dhaka-open-world
- Default branch: `main`

Read this file and all files under `context/` before development. Inspect the actual repository. Source code is authoritative when documentation and implementation disagree; update documentation when verified facts change.

## Non-negotiable constraints
1. **Do not install or use `proot-distro`, proot, Ubuntu, Debian, or another Linux distribution.** Development is primarily in Termux on Android.
2. **Do not introduce Gradle or a Gradle Wrapper.** The current Android build is an Ant/manual Android SDK pipeline. Do not silently replace it.
3. Preserve the original Termux project, its Git history, and the separate backup. Never delete or rewrite them during routine development.
4. Never read, print, copy, commit, or expose `.env`, tokens, signing keys, or other secrets.
5. Do not claim an APK is installable, playable, release-ready, or tested unless the corresponding checks were performed and recorded.
6. Keep changes small, reviewable, and committed. Do not overwrite unrelated user work.
7. Do not add large engine binaries, export-template archives, generated APKs, build outputs, or caches to Git.
8. Use original or properly licensed assets; record provenance and licenses.

## Startup procedure
1. Confirm repository root, branch, working-tree status, and latest commit.
2. Read `README.md`, `.gitignore`, every `context/` document, Android manifest, build file, Java source, and relevant resources.
3. Inspect installed tools and versions. Do not assume SDK paths or tool availability.
4. Determine whether the task concerns the active Java/OpenGL ES Android app or the experimental Godot scaffold. Do not conflate them.
5. Define the objective and acceptance tests before editing.
6. Implement the smallest coherent change.
7. Run available static checks and build steps. Report exact blockers; never fabricate a pass.
8. Inspect APK contents, verify its signature, install it on a real Android device when possible, and test changed behavior.
9. Review `git diff`; ensure no secrets or generated files are staged. Commit and push only when the branch is synchronized and the workflow is authorized.
10. Update status and architecture documentation to reflect evidence, not intentions.

## Immediate priority
Establish a reproducible, Gradle-free Android build that puts `classes.dex` at the APK root, produces a correctly signed APK, and can be installed and launched. The existing Ant build is not yet proven correct. Do not begin major feature expansion until this is resolved.

## Proposed remote build host
A possible arrangement is the user's Android phone as controller and a friend's Android phone (reported Snapdragon Gen 5, 12 GB RAM, and over 200 GB free storage) as a remote build host over Tailscale and SSH. This is proposed, not configured or verified. Do not assume connectivity, toolchain availability, or permissions until tested.
