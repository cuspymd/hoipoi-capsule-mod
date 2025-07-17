# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Minecraft mod for Fabric 1.21.4 called "HoiPoi Capsule Mod", inspired by Dragon Ball's Hoi-Poi capsules. The mod allows players to store building structures in small capsules and redeploy them anywhere in the world.

## Build Commands

**Primary build command:**
```bash
./gradlew build
```

**Run the mod in development:**
```bash
./gradlew runClient
```

**Clean build artifacts:**
```bash
./gradlew clean
```

**Generate sources:**
```bash
./gradlew genSources
```

## Project Structure

**Core mod files:**
- `src/main/java/cuspymd/hoipoi/capsule/` - Main mod package
- `src/main/resources/fabric.mod.json` - Fabric mod configuration
- `src/main/resources/assets/hoipoi-capsule-mod/` - Assets (textures, models, lang files)
- `src/main/resources/data/hoipoi-capsule-mod/` - Data files (recipes)

**Key Java packages:**
- `item/` - Item implementations (BasicCapsule)
- `data/` - Data structures (CapsuleData for storing structure information)
- `util/` - Utility classes (CapsuleUtils for capture/place logic)
- `registry/` - Registration classes (ModItems, ModItemGroups)
- `network/` - Network packet handling (CapturePayload, PlacePayload)
- `mixin/` - Mixin classes for game modifications

## Architecture Overview

**Core Components:**
- **BasicCapsule**: The main item that stores and deploys structures
- **CapsuleData**: Data structure storing block information, dimensions, and metadata
- **CapsuleUtils**: Contains logic for capturing and placing structures
- **Network System**: Client-server communication for structure operations

**Data Flow:**
1. Player uses capsule → Client sends packet to server
2. Server processes capture/place operation via CapsuleUtils
3. Structure data stored in NBT format within item stack
4. Placement reconstructs blocks from stored data

**Current Implementation Status:**
- Phase 1: Basic 3x3x3 capture/place system (✓ Implemented)
- Phase 2: Hold-based region selection with real-time preview (🔄 In Progress)
- Phase 3: Multiple capsule types and GUI (⏳ Planned)
- Phase 4: Advanced features like rotation and collision detection (⏳ Planned)
- Phase 5: Entity storage and optimization (⏳ Planned)

## Development Configuration

**Target Minecraft Version:** 1.21.4
**Fabric Loader:** 0.16.14
**Yarn Mappings:** 1.21.4+build.8
**Fabric API:** 0.119.3+1.21.4
**Java Version:** 21

**Mod Information:**
- Mod ID: `hoipoi-capsule-mod`
- Package: `cuspymd.hoipoi.capsule`
- Main Class: `HoipoiCapsuleMod`
- Client Class: `HoipoiCapsuleModClient`

## Key Implementation Details

**NBT Data Storage:**
- Structure data is stored in ItemStack NBT under "CapsuleData"
- Uses relative positioning for portability
- Stores BlockState information for accurate reconstruction

**Network Communication:**
- `CapturePayload` - Client to server capture request
- `PlacePayload` - Client to server placement request
- Server-side validation for all operations

**Current Limitations:**
- Fixed 3x3x3 capture size
- No preview system yet
- No collision detection
- Single capsule type only

## Future Development Roadmap

According to ModPlan.md, the next major features to implement:

1. **Hold-based Region Selection** - Real-time area preview while holding right-click
2. **Multiple Capsule Types** - Small (8x8x8), Medium (16x16x16), Large (32x32x32)
3. **GUI System** - Capsule information and configuration interface
4. **Advanced Features** - Rotation, collision detection, block entity preservation
5. **Optimization** - Performance improvements and entity storage

## Testing

**Manual Testing:**
- Run `./gradlew runClient` to test in development environment
- Create capsule using recipe system
- Test capture and placement in various scenarios

**Focus Areas:**
- NBT data integrity
- Network synchronization
- Structure reconstruction accuracy
- Performance with larger structures