# Development Rules

## Hard constraints
- Required deliverable: Android APK.
- Do not use Gradle, `gradlew`, Gradle Wrapper, Gradle build scripts, or a Gradle-based pipeline.
- Do not install or use proot, `proot-distro`, Ubuntu, Debian, or another Linux distribution.
- Develop primarily in Termux on Android. A remote Android build host may be explored through Tailscale/SSH only after explicit connectivity and toolchain checks.
- Preserve the original project and its history. Do not destructively reset it.
- Never commit `.env`, credentials, signing keys, private data, APKs, generated build output, downloaded engine binaries, or export-template archives.

## Engineering workflow
1. Inspect before editing. Read relevant source and context.
2. Verify installed Android SDK, Build Tools, Java, Ant, AAPT2, D8, and apksigner versions and paths.
3. Keep Java + OpenGL ES 2.0 as the active app unless a deliberate, documented migration is approved.
4. Treat `godot_project/` as an experimental scaffold, not the active Android runtime or a proven export pipeline.
5. Make one logically bounded change at a time.
6. Compile after code changes; test behavior changes on a real Android device.
7. Validate APK structure: root-level `classes.dex`, manifest, resources, package ID, SDK levels, and signature.
8. Record commands, tool versions, APK size, SHA-256, and test results. Distinguish compile, package, signature, install, launch, and gameplay tests.
9. Update documentation only to describe verified state.
10. Review `git diff --check` and `git status`; exclude secrets and generated files.
11. Commit coherent changes. Never force-push to resolve an unexplained rejection.

## Build-system requirements
The current `app/build.xml` uses Ant and Android command-line tools, but contains absolute Termux paths and has not been proven to create a correctly structured, installable APK. The presence of this file or a successful Ant target is not proof of a valid app.

The build must:
- compile Java against the selected Android SDK platform;
- compile and link resources with AAPT2;
- generate DEX with D8;
- place `classes.dex` at the APK root, not under an absolute filesystem path;
- sign with apksigner;
- verify the signature and inspect package contents.

Do not put passwords such as `pass:android` or debug keystores in committed build configuration. Keep the local debug key outside the repository and use a secure local mechanism for credentials.

## Quality and performance
- Optimize for real Android hardware.
- Use delta time for movement and simulation.
- Avoid per-frame allocations and repeated GPU resource creation.
- Reuse buffers and resources; manage OpenGL resources in the correct GL context.
- Handle lifecycle events, surface recreation, pointer IDs, and pause/resume.
- Test simultaneous joystick and action-button input.
- Measure frame time and memory before claiming performance targets.

## Asset and content rules
Use original, public-domain, or appropriately licensed assets. Do not copy proprietary game assets, maps, music, characters, or branding. Do not present fictional content as an accurate real-world Dhaka map.
