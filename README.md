# HoiPoi Capsule Mod

A Minecraft mod inspired by Dragon Ball's Hoi-Poi capsules that allows you to store and deploy building structures anywhere in the world.

## 📋 Overview

The HoiPoi Capsule Mod brings the convenience of portable architecture to Minecraft. Store your buildings, structures, and creations in small capsules and deploy them instantly wherever you need them.

**Key Features:**
- 🏠 Store buildings and structures in portable capsules
- 🚀 Instant deployment anywhere in the world
- 📦 Compact NBT-based storage system
- 🔄 Perfect structure reconstruction
- 🌐 Multiplayer support

## 🎯 Current Status

**Phase 1: Basic Capsule System (✅ Complete)**
- Basic Capsule item with 3x3x3 capture capability
- Right-click to capture structures
- Right-click to place stored structures
- NBT data storage system

**Phase 2: Hold-based Region Selection (🔄 In Progress)**
- Real-time area preview while holding right-click
- Dynamic region positioning based on player look direction
- Mouse wheel distance adjustment
- Automatic terrain collision handling

**Planned Features:**
- Multiple capsule types (Small, Medium, Large)
- GUI system for capsule management
- Structure rotation and collision detection
- Block entity preservation (chests, furnaces, etc.)
- Entity storage (animals, mobs)

## 🚀 Installation

### Requirements
- Minecraft 1.21.4
- Fabric Loader 0.16.14+
- Fabric API 0.119.3+1.21.4

### Installation Steps
1. Download the mod file from releases
2. Install Fabric Loader for Minecraft 1.21.4
3. Download and install Fabric API
4. Place the mod file in your `mods` folder
5. Launch Minecraft with Fabric profile

## 🎮 How to Use

### Basic Usage

1. **Craft a Basic Capsule:**
   ```
   [I] [C] [I]
   [C] [G] [C]
   [I] [C] [I]
   
   I = Iron Ingot
   C = Capsule Core
   G = Glass
   ```

2. **Capture a Structure:**
   - Hold the capsule in your hand
   - Right-click on the center block of the structure you want to capture
   - The capsule will store a 3x3x3 area around the clicked block

3. **Deploy a Structure:**
   - Hold the capsule containing a structure
   - Right-click where you want to place the structure
   - The structure will be deployed instantly

### Capsule Core Recipe
```
[R] [D] [R]
[D] [E] [D]
[R] [D] [R]

R = Redstone
D = Diamond
E = Ender Pearl
```

## 🛠️ Development

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

### Project Structure
```
src/
├── main/
│   ├── java/cuspymd/hoipoi/capsule/
│   │   ├── HoipoiCapsuleMod.java          # Main mod class
│   │   ├── item/BasicCapsule.java         # Capsule item implementation
│   │   ├── data/CapsuleData.java          # Structure data storage
│   │   ├── util/CapsuleUtils.java         # Capture/place logic
│   │   ├── registry/ModItems.java         # Item registration
│   │   └── network/                       # Network packet handling
│   └── resources/
│       ├── fabric.mod.json                # Mod configuration
│       ├── assets/                        # Textures and models
│       └── data/                          # Recipes and data
```

## 🔧 Technical Details

### Data Storage
- Structures are stored using NBT (Named Binary Tag) format
- Relative positioning ensures portability
- BlockState information preserved for accurate reconstruction
- Metadata includes dimensions, timestamp, and block count

### Network Communication
- Client-server packet system for multiplayer support
- Server-side validation for all operations
- Efficient data synchronization

### Performance Considerations
- Optimized NBT storage format
- Efficient block scanning algorithms
- Future: Compression and chunked processing for large structures

## 📚 API Documentation

### CapsuleData Class
Main data structure for storing captured structures:
```java
public class CapsuleData {
    private Map<BlockPos, BlockState> blocks;
    private Vec3i dimensions;
    private String name;
    private String description;
    private long timestamp;
}
```

### CapsuleUtils Class
Utility methods for structure operations:
```java
public static CapsuleData captureStructure(ServerWorld world, BlockPos startPos, BlockPos endPos)
public static boolean placeStructure(ServerWorld world, BlockPos startPos, CapsuleData capsuleData)
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

### Development Guidelines
- Follow existing code style and patterns
- Add comments for complex logic
- Test in both single-player and multiplayer
- Update documentation as needed

## 📄 License

This project is licensed under CC0-1.0 License - see the LICENSE file for details.

## 🔮 Future Roadmap

### Phase 3: Multiple Capsule Types (Planned)
- Small Capsule (8x8x8)
- Medium Capsule (16x16x16)
- Large Capsule (32x32x32)
- Creative Capsule (unlimited size)

### Phase 4: Advanced Features (Planned)
- Structure rotation (90-degree increments)
- Collision detection and warnings
- Block entity data preservation
- GUI for capsule management

### Phase 5: Optimization & Special Features (Planned)
- Entity storage (mobs, animals)
- Structure compression algorithms
- Performance optimizations
- Advanced multiplayer synchronization

## 💡 Credits

- Inspired by Dragon Ball's Hoi-Poi capsules
- Built with Fabric modding framework
- Community feedback and suggestions

## 🐛 Bug Reports

Please report bugs and issues on the GitHub issue tracker. Include:
- Minecraft version
- Mod version
- Fabric Loader version
- Detailed reproduction steps
- Log files if applicable

---

**Transform your Minecraft building experience with portable architecture!**