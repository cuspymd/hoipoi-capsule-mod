package cuspymd.hoipoi.capsule.data;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.HashMap;
import java.util.Map;

public class CapsuleData {
    private final Map<BlockPos, BlockState> blocks;
    private final Vec3i dimensions;
    private final String name;
    private final String description;
    private final long timestamp;
    
    public CapsuleData(Map<BlockPos, BlockState> blocks, Vec3i dimensions, String name, String description) {
        this.blocks = blocks;
        this.dimensions = dimensions;
        this.name = name;
        this.description = description;
        this.timestamp = System.currentTimeMillis();
    }
    
    public Map<BlockPos, BlockState> getBlocks() {
        return blocks;
    }
    
    public Vec3i getDimensions() {
        return dimensions;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public int getBlockCount() {
        return blocks.size();
    }
    
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        
        nbt.putString("name", name);
        nbt.putString("description", description);
        nbt.putLong("timestamp", timestamp);
        nbt.putIntArray("dimensions", new int[]{dimensions.getX(), dimensions.getY(), dimensions.getZ()});
        
        NbtList blockList = new NbtList();
        for (Map.Entry<BlockPos, BlockState> entry : blocks.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue();
            
            NbtCompound blockNbt = new NbtCompound();
            blockNbt.putIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
            blockNbt.put("state", NbtHelper.fromBlockState(state));
            
            blockList.add(blockNbt);
        }
        nbt.put("blocks", blockList);
        
        return nbt;
    }
    
    public static CapsuleData fromNbt(NbtCompound nbt) {
        String name = nbt.getString("name");
        String description = nbt.getString("description");
        int[] dimensionArray = nbt.getIntArray("dimensions");
        Vec3i dimensions = new Vec3i(dimensionArray[0], dimensionArray[1], dimensionArray[2]);
        
        Map<BlockPos, BlockState> blocks = new HashMap<>();
        
        NbtList blockList = nbt.getList("blocks", NbtCompound.COMPOUND_TYPE);
        for (int i = 0; i < blockList.size(); i++) {
            NbtCompound blockNbt = blockList.getCompound(i);
            
            int[] posArray = blockNbt.getIntArray("pos");
            BlockPos pos = new BlockPos(posArray[0], posArray[1], posArray[2]);
            
            BlockState state = NbtHelper.toBlockState(Registries.BLOCK, blockNbt.getCompound("state"));
            if (state == null) {
                state = Blocks.AIR.getDefaultState();
            }
            
            blocks.put(pos, state);
        }
        
        CapsuleData capsuleData = new CapsuleData(blocks, dimensions, name, description);
        return capsuleData;
    }
    
    public boolean isEmpty() {
        return blocks.isEmpty();
    }
    
    public boolean isValidSize(int maxSize) {
        return dimensions.getX() <= maxSize && dimensions.getY() <= maxSize && dimensions.getZ() <= maxSize;
    }
    
    @Override
    public String toString() {
        return String.format("CapsuleData{name='%s', dimensions=%s, blocks=%d, timestamp=%d}", 
                            name, dimensions, blocks.size(), timestamp);
    }
}