# Dhaka Open World

A low-poly, third-person open-world game for Android, set in Dhaka, Bangladesh.

## Phase 2 MVP Features

- **OpenGL ES 2.0 rendering pipeline**
- **City environment:** Buildings, roads, trees
- **Third-person player character** with walking animation
- **Touch controls:** Virtual joystick (left), jump/sprint buttons (right)
- **Follow camera:** Perspective projection, smooth follow
- **Performance optimized** for Android devices

## Build (no Gradle)

1. Compile Java sources:
   ```
   javac -cp $ANDROID_HOME/platforms/android-34/android.jar \
         -d app/build/classes $(find app/src/main/java -name "*.java")
   ```

2. Generate DEX:
   ```
   d8 --output app/build/dex $(find app/build/classes -name "*.class")
   ```

3. Link resources and produce APK:
   ```
   aapt2 link -I $ANDROID_HOME/platforms/android-34/android.jar \
         -R app/flat_res/*.flat \
         -o app/build/app_unsigned.apk \
         --manifest app/AndroidManifest.xml \
         --auto-add-overlay --debug-mode
   zip -j app/build/app_unsigned.apk app/build/dex/classes.dex
   ```

4. Sign APK:
   ```
   apksigner sign --ks ~/.android/debug.keystore --ks-key-alias androiddebugkey \
         --ks-pass pass:android --key-pass pass:android \
         --out app/build/app_signed.apk app/build/app_unsigned.apk
   ```

5. Verify:
   ```
   apksigner verify app/build/app_signed.apk
   ```

## Controls

| Control | Description |
|---------|-------------|
| **Virtual Joystick (Left)** | 360° player movement |
| **Sprint (Right)** | Run faster |
| **Jump (Right)** | Jump |
| **Camera Swipe** | Rotate camera view |

## Project Structure

```
app/src/main/java/com/example/dhaka/
├── MainActivity.java         # Activity entry point
├── MyGLSurfaceView.java      # OpenGL surface with touch handling
├── GameRenderer.java         # Main render loop
├── Player.java               # Player model and animation
├── Camera.java               # Third-person camera
├── TouchControls.java        # Virtual joystick and buttons
├── Environment.java          # City environment (buildings, trees)
├── Building.java             # Building geometry
├── Mesh.java                 # OpenGL mesh data
└── Shader.java               # Shader utilities
```

## Current Limitations

- Buildings are simple procedural boxes
- No vehicle system (Phase 3)
- No missions or NPC AI (Phase 3)
- Limited terrain variety

## Next Steps

- Add low-poly character models
- Implement vehicle system
- Add missions and dialogue
- Improve city details and textures
- Add day/night cycle
## License

Copyright © 2026 Dhaka Open World Team. All rights reserved.
