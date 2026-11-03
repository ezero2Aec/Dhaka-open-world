# Architecture

## Active Android application
The current Android application is a native Java project using OpenGL ES 2.0. It is not currently a Godot-exported Android application.

### Runtime structure
- `MainActivity`: Android activity and lifecycle entry point.
- `MyGLSurfaceView`: OpenGL surface and touch-event integration.
- `MyRenderer`: GLSurfaceView renderer adapter.
- `GameRenderer`: game frame/update and scene rendering logic.
- `Player`: player state, movement, and character drawing.
- `Camera`: third-person view/projection or view-target calculations.
- `TouchControls`: virtual joystick and action-button state.
- `Environment`: scene/environment coordination.
- `Building`: building geometry and rendering.
- `Mesh`: vertex/index data and GPU drawing resources.
- `Shader`: GLSL program compilation/linking helpers.
- `Cube`: basic geometry/test object.

These are intended responsibilities inferred from source layout. Inspect implementations before assuming a method contract or feature is complete.

## Android build pipeline
The current build definition is `app/build.xml`, an Apache Ant file. The intended toolchain is:
1. Compile Android XML resources using AAPT2.
2. Compile Java against the selected Android SDK's `android.jar`.
3. Package compiled classes into a JAR.
4. Convert bytecode to DEX using D8.
5. Link manifest and resources using AAPT2.
6. Place `classes.dex` at the root of the APK.
7. Sign with apksigner.
8. Verify signature and inspect package contents.

### Known build-file defects and risks
The checked-in Ant file:
- hard-codes SDK, keystore, and executable paths under one Termux installation;
- embeds a debug keystore path and debug password assumptions;
- invokes AAPT2 link with precompiled resources but does not clearly establish complete resource-symbol handling;
- uses `jar uf` with an absolute DEX path, which previously resulted in DEX being nested under a filesystem path inside the APK instead of at the APK root;
- has not been demonstrated to produce a valid, installable APK from a fresh checkout.

The Ant file is a starting point, not a validated build system. Fix these issues without Gradle. Use a packaging method that explicitly writes `classes.dex` to the archive root and verify with `unzip -l`.

## Godot status
`godot_project/` contains a minimal scaffold (`project.godot`, `Main.tscn`, and `Cube.gd`). It is experimental and separate from the native Java app.

Downloaded Linux ARM64 Godot executables and archives were intentionally excluded from GitHub. A Linux binary is not automatically executable in Termux; do not assume compatibility. Do not install proot or a Linux distribution to run it. Do not migrate to Godot unless Android export is demonstrated under the no-Gradle/no-proot constraints and migration is explicitly approved.

## Optional remote build topology
Proposed setup:
- controller: user's Android phone running Termux and the coding agent;
- build worker: friend's Android phone running Termux and required Android build tools;
- secure connectivity: Tailscale plus SSH.

This is not yet verified. Keep source in Git and use a controlled checkout/build directory on the worker. Do not share private keys, tokens, or unrestricted shell access. Validate network access, toolchain, reproducibility, and artifact transfer before relying on it.

## Performance architecture
- Use delta time for simulation.
- Keep GL object creation/deletion on a valid GL context.
- Recreate GPU resources after context loss.
- Avoid per-frame allocations.
- Reuse vertex/index buffers and shader programs.
- Cull distant/off-screen objects and bound scene complexity.
- Profile on actual Android devices; do not infer performance from source inspection.
