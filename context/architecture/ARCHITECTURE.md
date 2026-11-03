# Dhaka Open World — Architecture

## Build System

**Gradle-Free Android Build Pipeline**

- **Tool**: Apache Ant + Android SDK command-line tools
- **Process**:
  1. `aapt2 compile` - Compile resources to flat format
  2. `aapt2 link` - Link resources, generate R.java
  3. `javac` - Compile Java source code
  4. `d8` - Convert bytecode to DEX (Android runtime)
  5. `zip` - Package APK components
  6. `apksigner` - Sign APK with debug key

- **Location**: `app/build.xml`

## Project Structure

```
app/
├── AndroidManifest.xml      # Android application manifest
├── build.xml                # Ant build configuration
├── build/                   # Build output
│   ├── classes/             # Compiled Java classes
│   └── dex/                 # DEX output (for runtime)
└── src/main/
    ├── java/com/example/dhaka/
    │   └── MainActivity.java    # Main activity
    └── res/
        ├── layout/
        │   └── activity_main.xml
        └── values/
            └── strings.xml
```

## Engine

**Godot 4.7.2** (downloaded, pending integration)

- Linux arm64 binary available (not functional on Termux without glibc)
- Export templates downloaded (1.2GB)
- Minimal Godot project created: `godot_project/`

## Build Limitations

1. **d8 compilation** - DEX conversion failing on current environment
2. **APK format** - Current APK uses classes.jar instead of classes.dex
3. **Godot integration** - Requires full Linux environment (proot setup in progress)

## Next Steps

1. Complete proot + Ubuntu setup for Godot export
2. Create proper classes.dex using d8 with correct flags
3. Integrate Godot with Android export preset
4. Build 3D Dhaka environment

## Tools Available

- aapt2 (Resource compiler/linker)
- d8 (DEX compiler)  
- apksigner (APK signing)
- javac (Java compiler)
- jar/zip (Packaging)
- ant (Build automation)
