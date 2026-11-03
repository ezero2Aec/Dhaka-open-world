# Dhaka Open World – Project Overview

**Goal:** An open-world 3D game inspired by classic PS2-era titles, set in Dhaka, Bangladesh.

**Platform:** Android (APK)
**Build System:** Gradle-free (aapt2, d8, apksigner)

## Core Gameplay Features

### Phase 1 ✅
- OpenGL ES 2.0 rendering
- Basic 3D scene

### Phase 2 ✅ (Current)
- Low-poly city environment
- Third-person player character
- Touchscreen controls (joystick + action buttons)
- Follow camera system

### Phase 3 (Planned)
- Vehicle system
- Traffic and pedestrian AI
- Missions and interactions
- Save/load system

## Architecture

### Core Classes
| Class | Purpose |
|-------|---------|
| `MainActivity` | Android Activity entry point |
| `MyGLSurfaceView` | OpenGL surface with multitouch support |
| `GameRenderer` | Main render loop and frame updates |
| `Player` | Character model, animation, movement |
| `Camera` | Third-person follow camera |
| `TouchControls` | Virtual joystick and action buttons |
| `Environment` | City environment manager |
| `Building` | Building geometry |
| `Mesh` | OpenGL mesh data and drawing |
| `Shader` | GLSL shader utilities |

### Build Pipeline
1. Java compilation → `.class` files
2. DEX conversion (`d8`) → `classes.dex`
3. Resource linking (`aapt2`) → `resources.ap_`
4. APK assembly (`zip`)
5. Signing (`apksigner`)

## Performance Considerations

- Indexed vertex buffers
- Reused GPU buffers
- Distance-based rendering
- Delta-time movement
- Target: 30-60 FPS on low-end devices

## File Locations

- Source: `app/src/main/java/com/example/dhaka/`
- Resources: `app/src/main/res/`
- APK: `app/build/app_signed.apk`
- Documentation: `context/`
