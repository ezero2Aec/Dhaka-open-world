# Development Roadmap

Priorities are ordered by dependency. Do not skip build reliability to add visible features.

## Phase 0 — Repository and environment hygiene
- [x] Create a clean source-only GitHub repository.
- [x] Keep the original Termux project and its backup separate.
- [x] Exclude secrets, APKs, generated outputs, and downloaded Godot binaries.
- [ ] Confirm development workspaces use the canonical repository.
- [ ] Keep documentation synchronized with verified implementation.

## Phase 1 — Reproducible Android build
- [ ] Inspect installed Android SDK, platform, Build Tools, Java, Ant, AAPT2, D8, and apksigner.
- [ ] Remove hard-coded machine-specific paths without introducing Gradle.
- [ ] Correct resource compilation/linking and manifest handling.
- [ ] Correct D8 invocation and APK assembly.
- [ ] Ensure `classes.dex` is at the APK root.
- [ ] Sign and verify the APK.
- [ ] Inspect APK metadata, contents, and size.
- [ ] Install and launch on a physical Android device.
- [ ] Document a clean, repeatable build from a fresh checkout.

**Exit condition:** a reproducible build produces a correctly structured, signed APK that installs and launches on a physical device.

## Phase 2 — Stable playable on-foot MVP
- [ ] Verify OpenGL context creation and shader initialization order.
- [ ] Verify surface-size handling and projection updates.
- [ ] Implement stable frame-time-based movement.
- [ ] Fix multitouch pointer tracking and simultaneous joystick/action input.
- [ ] Verify camera target, orientation, and follow behavior.
- [ ] Implement collision against ground and buildings.
- [ ] Ensure environment and building meshes are valid and rendered.
- [ ] Add pause/resume and surface recreation handling.
- [ ] Test crashes, controls, and sustained performance on-device.

**Exit condition:** the player can move around a small city block, camera and controls work reliably, and the app survives normal pause/resume.

## Phase 3 — Dhaka neighborhood vertical slice
- [ ] Build a compact, navigable street layout.
- [ ] Add low-poly buildings, sidewalks, road markings, and local visual details.
- [ ] Add basic lighting, sky, and environmental atmosphere.
- [ ] Establish collision and navigation boundaries.
- [ ] Optimize geometry, draw calls, memory, and frame time.

## Phase 4 — Vehicles and interactions
- [ ] Add one vehicle with basic handling.
- [ ] Implement entering and exiting.
- [ ] Add a minimal interaction system.
- [ ] Test controls and camera in on-foot and vehicle modes.

## Phase 5 — Living city and gameplay
- [ ] Add limited traffic and pedestrian systems with performance budgets.
- [ ] Add missions and objective tracking.
- [ ] Add audio and effects.
- [ ] Add save/load with versioned data.
- [ ] Expand shops, landmarks, and neighborhoods incrementally.

## Phase 6 — Release
- [ ] Set a production application ID and versioning plan.
- [ ] Create icon, screenshots, and release metadata.
- [ ] Audit permissions, privacy, licensing, and crash behavior.
- [ ] Build and sign a release APK securely.
- [ ] Verify signature, structure, install, launch, and gameplay.
- [ ] Record size and SHA-256.
- [ ] Publish the verified APK as a GitHub Release asset.
- [ ] Document installation instructions and known limitations.

## Optional infrastructure — Remote Android build host
- [ ] Evaluate Tailscale and SSH between the user's controller phone and the friend's Android phone.
- [ ] Confirm both Termux environments, storage, network stability, and toolchains.
- [ ] Restrict SSH access; do not expose the build host publicly.
- [ ] Test a small build before moving the full workflow.
- [ ] Treat this as optional infrastructure, not a prerequisite for local source development.
