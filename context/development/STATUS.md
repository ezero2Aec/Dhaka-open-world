# Development Status

Last documentation audit: 2026-11-03

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

## Build status: NOT VERIFIED
The Ant build file exists, but the clean source baseline has not been proven to produce a correctly structured, installable APK from a fresh checkout.

Known issues:
1. `app/build.xml` contains absolute paths tied to one Termux installation.
2. Its DEX packaging command previously placed DEX under a nested absolute path in the APK instead of the APK root.
3. Resource linking and generated resource-symbol handling need verification.
4. A prior small APK was identified as a placeholder/non-installable artifact. Do not use or publish it.
5. No APK in this clean repository is an authoritative release artifact.

Do not report successful build, install, launch, or gameplay tests until each has been performed and recorded separately.

## Runtime/gameplay status
The Java source includes renderer, player, camera, touch controls, environment, building, mesh, and shader classes. This inventory does not establish that each system works.

Previous audit investigation targets included:
- shader initialization relative to GL context creation;
- surface dimension and projection initialization;
- multitouch pointer tracking and action-button state;
- player jump/movement state;
- camera target/look-at calculations;
- building mesh validity and render submission;
- ground/road mesh indices and collision behavior.

These are investigation targets, not confirmed current defects. Re-check source before modifying.

No complete playable open-world experience has been verified. Vehicles, traffic AI, pedestrian AI, missions, save/load, and a populated Dhaka city are planned unless demonstrated in code and on-device tests.

## Godot status
- `godot_project/` is a minimal experimental scaffold.
- Godot is not the active Android app runtime.
- Linux ARM64 Godot binaries and export archives are not stored in this repository.
- No Godot Android export has been verified.
- Do not use proot or install a Linux distribution.

## Remote build host status
The user is considering a friend's Android phone (reported Snapdragon Gen 5, 12 GB RAM, and over 200 GB free storage) as a remote build worker using Tailscale and SSH. This is a proposal only. No successful connection, remote toolchain audit, or remote build has been verified.

## Immediate priorities
1. Make the Ant/manual build portable without Gradle.
2. Correct APK assembly so `classes.dex` is at archive root.
3. Build, sign, inspect, and install the APK.
4. Test launch, rendering, controls, lifecycle, and a small playable block on real hardware.
5. Update this file with exact commands, tool versions, APK checksum, device details, and test outcomes.
