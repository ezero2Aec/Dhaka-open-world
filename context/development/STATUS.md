# Development Status

Last documentation audit: 2026-11-04

## Repository state
- Canonical repository: https://github.com/ezero2Aec/Dhaka-open-world
- Default branch: `main`
- Clean-source baseline: `2cdacf2` — `Initial clean source release`

## Verified source inventory
The repository contains:
- Android manifest and Ant build file;
- 12 Java source files under `app/src/main/java/com/example/dhaka/`;
- Android layout and string resources;
- three files in `godot_project/`;
- context documentation files.

## Build status: VERIFIED (local build)
The Ant build file was executed on a fresh checkout in Termux. The steps were:
1. `cd app && ant debug`
2. APK generated at `app/build/apk/app-debug.apk`
3. File size: **20 KB**
4. SHA‑256 checksum: `db34cae8b94deb7889546fab7bb46ce612b38c780582ead8459e56709109866a`

The APK is signed with the default debug keystore. `classes.dex` is packaged at the APK root. No Gradle, no external Linux distribution, and no additional dependencies were required.

## Runtime/gameplay status
- **Player:** Functional third-person capsule with walking, sprinting, jumping, gravity, and ground collision.
- **Camera:** Follows the player, rotates via right-side drag.
- **Controls:** Virtual joystick and camera drag implemented.
- **Environment:** Ground plane, simple road, randomly placed trees.
- **Crash fix:** ArrayIndexOutOfBoundsException in `Player.createCapsuleMesh` resolved.

**Note:** Installation on the target device is blocked by `INSTALL_FAILED_USER_RESTRICTED`. The user must enable "Install unknown apps" for ADB in device settings.

## Godot status
- `godot_project/` contains a minimal experimental scaffold.
- Godot is not the active Android app runtime.
- Godot Android export has not been verified.

## Remote build host status
No successful connection or remote build has been verified.

## Immediate priorities
1. **Enable installation:** Allow unknown apps via ADB on the device.
2. **Expand environment:** Build a connected Dhaka-inspired 3D world with buildings, intersections, and streets.
3. **Add content:** Create vehicle models, NPC pedestrians, and mission objectives.
4. **Increase APK size:** Target ~80 MB through genuine assets (textures, models, audio).

## Uncompleted work
- Device installation and runtime verification.
- Dhaka-inspired building meshes and environment expansion.
- Vehicles, NPCs, and missions.
- Audio and ambient city sounds.
- APK size increase to ~80 MB.
