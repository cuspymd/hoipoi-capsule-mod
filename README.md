# Hoipoi Capsule Mod

A Minecraft mod inspired by Dragon Ball's Hoipoi capsule technology that allows players to capture and deploy structures using capsules.

## Features

### Current Features (Phase 1)
- **Basic Capsule**: Capture and deploy 3x3x3 structures
- **Structure Storage**: Stores blocks and block entities (chests, signs, etc.) in NBT format
- **Visual Feedback**: Capsules with stored structures have an enchanted glow effect
- **Intuitive Controls**: Right-click to capture, Shift+right-click to deploy
- **Creative Tab**: Dedicated creative mode tab for capsule items

### Planned Features
- **Region Selection**: Choose custom areas with a selection wand
- **Multiple Capsule Types**: Small (8x8x8), Medium (16x16x16), Large (32x32x32)
- **Structure Preview**: See structures before placement
- **GUI Interface**: Name and manage stored structures
- **Advanced Features**: Entity storage, rotation, collision detection

## Installation

1. Download the mod jar from the releases page
2. Install NeoForge 21.7.3-beta or later for Minecraft 1.21.7
3. Place the mod jar in your `mods` folder
4. Launch Minecraft and enjoy!

## Usage

### Basic Capsule Usage

1. **Craft a Basic Capsule** (recipe available in-game)
2. **Capture a Structure**:
   - Right-click on a block with an empty capsule
   - A 3x3x3 area centered on the clicked block will be captured
   - The capsule will glow to indicate it contains a structure
3. **Deploy the Structure**:
   - Hold Shift and right-click with the filled capsule
   - The structure will be placed at the clicked location
   - The capsule will become empty after deployment

### Tips
- Capsules show structure information in their tooltip
- Block entities (chests, signs, etc.) are preserved during capture/deployment
- Only non-air blocks are captured to save space

## Development

### Requirements
- Java 21 or higher
- NeoForge 21.7.3-beta
- Minecraft 1.21.7

### Building from Source

```bash
# Clone the repository
git clone <repository-url>
cd hoipoi-capsule-mod

# Build the mod
./gradlew build

# Run in development environment
./gradlew runClient
```

### Development Commands

```bash
./gradlew runClient           # Run client for testing
./gradlew runServer           # Run server for testing
./gradlew runData             # Generate data files
./gradlew runGameTestServer   # Run automated tests
./gradlew clean               # Clean build artifacts
./gradlew --refresh-dependencies # Refresh dependency cache
```

### IDE Setup

The usual recommendation for an IDE is either IntelliJ IDEA or Eclipse. 

If you're missing libraries in your IDE or run into problems:
1. Run `./gradlew --refresh-dependencies` to refresh the local cache
2. Run `./gradlew clean` to reset everything (this does not affect your code)
3. Restart your IDE and reimport the project

## Project Structure

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

## Development Roadmap

### Phase 1: Basic Capsule System ✅
- [x] Basic capsule item with 3x3x3 capture
- [x] Structure storage in NBT format
- [x] Block entity preservation
- [x] Basic UI feedback

### Phase 2: Region Selection System (In Progress)
- [ ] Capsule wand for area selection
- [ ] Visual selection indicators
- [ ] Size limitation system

### Phase 3: Capsule Types & GUI
- [ ] Multiple capsule sizes
- [ ] Structure preview interface
- [ ] Capsule naming and descriptions

### Phase 4: Advanced Features
- [ ] Structure rotation
- [ ] Collision detection
- [ ] Deployment preview
- [ ] Entity storage

### Phase 5: Optimization & Polish
- [ ] Performance improvements
- [ ] Multiplayer synchronization
- [ ] Advanced compression

## Technical Details

- **Platform**: NeoForge 21.7.3-beta
- **Minecraft Version**: 1.21.7
- **Java Version**: 21
- **Data Storage**: Modern DataComponents API
- **Package**: com.cuspymd.hoipoi

## Mapping Names

This mod uses the official mapping names from Mojang for methods and fields in the Minecraft codebase. These names are covered by a specific license. For the latest license text, refer to: https://github.com/NeoForged/NeoForm/blob/main/Mojang.md

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## Resources

- **NeoForge Documentation**: https://docs.neoforged.net/
- **NeoForged Discord**: https://discord.neoforged.net/
- **Project Planning**: See `ModPlan.md` for detailed development plans

## License

All Rights Reserved

---

*Inspired by Dragon Ball's Hoipoi capsule technology - bringing the convenience of portable structures to Minecraft!*