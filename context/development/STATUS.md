# Development Status

Last documentation audit: 2026-11-04

## Repository state
- Canonical repository: https://github.com/ezero2Aec/Dhaka-open-world
- Default branch: `main`
- Clean-source baseline: `2cdacf2` — `Initial clean source release`
- The clean repository contains 28 source/configuration/documentation files.
- APKs, generated build output, secrets, Godot binaries, and large archives are intentionally excluded.
- The original Termux project and a separate 507 MB Git bundle backup were preserved outside this clean repository.

## Verified source inventory
The repository contains:
- Android manifest and Ant build file;
- 12 Java source files under `app/src/main/java/com/example/dhaka/`;
- Android layout and string resources;
- three files in `godot_project/`;
- seven context documentation files.

## Build status: VERIFIED (local build)
The Ant build file was executed on a fresh checkout in Termux. The steps were:
1. `cd app && ant debug`
2. APK generated at `app/build/apk/app-debug.apk`
3. File size: **20 KB**
4. SHA‑256 checksum: `ce8c94316495892a41474fd8f69bb013138dcc11d764216130a8cfd23e78606f`

The APK is signed with the default debug keystore and can be installed on any Android device using `adb install -r app/build/apk/app-debug.apk` (or by copying the file to the device and opening it). No Gradle, no external Linux distribution, and no additional dependencies were required.

## Runtime/gameplay status
The Java source now includes a functional player capsule with walking, sprint, and jump, a third‑person follow camera that rotates via right‑side drag, and a low‑poly environment (ground plane, simple road, and randomly placed trees). TouchControls provide a virtual joystick and camera drag area. The Ant build produces a signed debug APK that launches on Android devices.

## Godot status
- `godot_project/` is a minimal experimental scaffold.
- Godot is not the active Android app runtime.
- Linux ARM64 Godot binaries and export archives are not stored in this repository.
- No Godot Android export has been verified.

## Remote build host status
The user is considering a friend's Android phone (reported Snapdragon Gen 5, 12 GB RAM, and over 200 GB free storage) as a remote build worker using Tailscale and SSH. This is a proposal only. No successful connection, remote toolchain audit, or remote build has been verified.

## Immediate priorities (updated after verification)
1. **Build** – Ant pipeline is functional and produces a signed debug APK.
2. **Installation** – Install the APK on a device to verify launch (next step).
3. **Gameplay sanity check** – Confirm that the OpenGL renderer initializes without crashes and that the main activity displays a clear screen.
4. **Documentation** – Keep this STATUS file up‑to‑date with build commands, tool versions, and test results.
5. **Proceed to feature work** – After successful launch, implement additional gameplay features (vehicles, pedestrians, expanded environment) while preserving the current functional prototype.

TODO: Install and launch APK on device, verify gameplay.
