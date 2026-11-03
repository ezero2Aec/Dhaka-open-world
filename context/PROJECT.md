# Dhaka Open World — Project Definition

## Product vision
Dhaka Open World is an Android-first, low-poly, third-person open-world game inspired by Dhaka, Bangladesh. The intended experience is a compact, explorable city with recognizable local atmosphere and accessible mobile controls—not an immediate attempt to simulate an entire city.

## Delivery target
- Primary platform: Android
- Deliverable: installable APK
- Active implementation: Java + OpenGL ES 2.0
- Package ID observed in the existing manifest/APK audit: `com.example.dhaka` (do not change without a migration plan)
- Build approach: Ant plus Android SDK command-line tools; Gradle is prohibited.
- Godot: experimental project scaffold only; not the active Android app pipeline.

## Current source layout
```text
app/
  AndroidManifest.xml
  build.xml
  src/main/java/com/example/dhaka/
    MainActivity.java
    MyGLSurfaceView.java
    MyRenderer.java
    GameRenderer.java
    Player.java
    Camera.java
    TouchControls.java
    Environment.java
    Building.java
    Mesh.java
    Shader.java
    Cube.java
  src/main/res/
    layout/activity_main.xml
    values/strings.xml
godot_project/
  project.godot
  Main.tscn
  Cube.gd
context/
  project instructions, architecture, roadmap, and status
```

## Implemented code versus verified gameplay
The source tree contains classes for an OpenGL renderer, player, camera, touch controls, environment, buildings, meshes, and shaders. Their presence does **not** prove that all features work together.

Previously observed capabilities include a basic OpenGL ES 2.0 scene and early third-person/gameplay scaffolding. The project has not been demonstrated to be a complete, tested open-world game. Do not describe vehicles, traffic, pedestrians, missions, save/load, a populated city, or polished character animation as implemented unless code inspection and device tests establish that status.

## Core systems
- Android lifecycle and surface management
- OpenGL ES 2.0 renderer
- Player movement and character representation
- Third-person camera
- Multi-touch virtual controls
- Environment and building geometry
- Mesh and shader utilities

## Product priorities
1. Reproducible APK build and correct APK structure.
2. Stable launch, lifecycle, rendering, and touch input on a real Android device.
3. A small, coherent Dhaka-inspired playable block.
4. Reliable player movement, camera, collision, and interaction.
5. A basic vehicle loop only after the on-foot loop is stable.
6. Expand city content, traffic, pedestrians, missions, audio, and persistence incrementally.

## Performance and scope
Optimize for real Android devices. Start with a compact scene and measurable geometry/draw budgets. Prefer low-poly geometry, batching, frustum/distance culling, and reusable GPU buffers. Measure frame time, memory, and thermal behavior before claiming performance. Keep the APK below the previously requested 50 MB target where feasible; report actual size.

## Repository
Canonical source: https://github.com/ezero2Aec/Dhaka-open-world
