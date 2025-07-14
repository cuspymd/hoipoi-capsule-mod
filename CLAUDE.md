# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Minecraft mod called "Hoipoi Capsule Mod" inspired by Dragon Ball's capsule technology. The mod allows players to capture and deploy structures using capsules, similar to how Hoipoi capsules work in the anime.

**Key Details:**
- **Platform**: NeoForge 21.7.3-beta
- **Minecraft Version**: 1.21.7
- **Language**: Java 21
- **Package**: com.cuspymd.hoipoi
- **Mod ID**: hoipoicapsulemod

## Build Commands

All commands should be run from the project root directory:

**Development:**
```bash
./gradlew runClient      # Run the mod in development client
./gradlew runServer      # Run the mod in development server  
./gradlew runData        # Run data generation
./gradlew runGameTestServer # Run game tests
```

**Building:**
```bash
./gradlew build          # Build the mod jar
./gradlew clean          # Clean build artifacts
./gradlew --refresh-dependencies # Refresh dependencies cache
```

**IDE Support:**
```bash
./gradlew genEclipseRuns # Generate Eclipse run configurations
./gradlew genIntellijRuns # Generate IntelliJ run configurations
```

## Code Architecture

### Core Components

**Main Mod Class (`HoipoiCapsuleMod.java`)**
- Registers items, blocks, and creative tabs
- Handles mod initialization and common setup
- Contains deferred registers for items and blocks

**Items Package (`com.cuspymd.hoipoi.items`)**
- `BasicCapsule.java` - Main capsule item that captures/deploys structures
- Handles right-click interactions (capture) and Shift+right-click (deploy)
- Currently supports 3x3x3 structure capture

**Data Package (`com.cuspymd.hoipoi.data`)**
- `CapsuleData.java` - Core data structure for storing captured structures
- Handles NBT serialization/deserialization of block data
- Stores blocks, block entities, dimensions, and metadata

**Utils Package (`com.cuspymd.hoipoi.utils`)**
- `CapsuleUtils.java` - Utility functions for structure capture and deployment
- Contains the core logic for scanning and placing blocks

### Key Systems

**Structure Capture System:**
- Captures 3x3x3 area around clicked position
- Stores block states and block entity data
- Saves to NBT format in item's custom data component

**Structure Deployment System:**
- Deploys stored structure at target position
- Handles block placement and block entity restoration
- Clears capsule data after successful deployment

**Creative Tab Integration:**
- Custom creative tab "capsule_tab" for capsule items
- Integrated with existing creative mode tabs

## Development Workflow

1. **Adding New Features**: Follow the existing package structure
2. **Item Registration**: Add new items to `HoipoiCapsuleMod.java` deferred registers
3. **Data Components**: Use modern DataComponents API instead of legacy NBT
4. **Localization**: Add translation keys to `src/main/resources/assets/hoipoicapsulemod/lang/en_us.json`

## Important Notes

- This is currently Phase 1 implementation with basic 3x3x3 capture
- The mod plan (`ModPlan.md`) outlines future phases including region selection, GUI, and advanced features
- Uses modern NeoForge APIs including DataComponents for item data storage
- Block entities are preserved during capture/deployment
- The mod includes both client and server-side components

## Testing

- Use `./gradlew runClient` for quick testing during development
- Test both creative and survival modes
- Verify structure capture and deployment work correctly
- Check block entity data preservation (chests, signs, etc.)

## File Structure

```
src/main/java/com/cuspymd/hoipoi/
├── HoipoiCapsuleMod.java      # Main mod class
├── Config.java                # Configuration handling
├── HoipoiCapsuleModClient.java # Client-side initialization
├── data/
│   └── CapsuleData.java       # Structure data management
├── items/
│   └── BasicCapsule.java      # Capsule item implementation
└── utils/
    └── CapsuleUtils.java      # Core utility functions
```