package com.cuspymd.hoipoi.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class CapsuleData {
    private Map<BlockPos, BlockState> blocks;
    private Map<BlockPos, CompoundTag> blockEntities;
    private BlockPos dimensions;
    private String name;
    private String description;
    private long timestamp;
    
    public CapsuleData() {
        this.blocks = new HashMap<>();
        this.blockEntities = new HashMap<>();
        this.dimensions = BlockPos.ZERO;
        this.name = "";
        this.description = "";
        this.timestamp = System.currentTimeMillis();
    }
    
    public CapsuleData(Map<BlockPos, BlockState> blocks, Map<BlockPos, CompoundTag> blockEntities, 
                      BlockPos dimensions) {
        this.blocks = blocks != null ? blocks : new HashMap<>();
        this.blockEntities = blockEntities != null ? blockEntities : new HashMap<>();
        this.dimensions = dimensions;
        this.name = "";
        this.description = "";
        this.timestamp = System.currentTimeMillis();
    }
    
    public void setBlock(BlockPos pos, BlockState state) {
        if (state != null && !state.isAir()) {
            blocks.put(pos, state);
        }
    }
    
    public void setBlockEntity(BlockPos pos, CompoundTag entityData) {
        if (entityData != null) {
            blockEntities.put(pos, entityData);
        }
    }
    
    public BlockState getBlock(BlockPos pos) {
        return blocks.getOrDefault(pos, Blocks.AIR.defaultBlockState());
    }
    
    public CompoundTag getBlockEntity(BlockPos pos) {
        return blockEntities.get(pos);
    }
    
    public Map<BlockPos, BlockState> getAllBlocks() {
        return new HashMap<>(blocks);
    }
    
    public Map<BlockPos, CompoundTag> getAllBlockEntities() {
        return new HashMap<>(blockEntities);
    }
    
    public boolean isEmpty() {
        return blocks.isEmpty();
    }
    
    public int getBlockCount() {
        return blocks.size();
    }
    
    public BlockPos getDimensions() {
        return dimensions;
    }
    
    public void setDimensions(BlockPos dimensions) {
        this.dimensions = dimensions;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name != null ? name : "";
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public void saveToNBT(CompoundTag tag) {
        CompoundTag structureData = new CompoundTag();
        
        // Save dimensions
        structureData.putInt("width", dimensions.getX());
        structureData.putInt("height", dimensions.getY());
        structureData.putInt("depth", dimensions.getZ());
        
        // Save blocks
        ListTag blocksList = new ListTag();
        for (Map.Entry<BlockPos, BlockState> entry : blocks.entrySet()) {
            CompoundTag blockTag = new CompoundTag();
            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue();
            
            blockTag.putInt("x", pos.getX());
            blockTag.putInt("y", pos.getY());
            blockTag.putInt("z", pos.getZ());
            
            ResourceLocation blockName = BuiltInRegistries.BLOCK.getKey(state.getBlock());
            blockTag.putString("block", blockName.toString());
            
            blocksList.add(blockTag);
        }
        structureData.put("blocks", blocksList);
        
        // Save block entities
        ListTag blockEntitiesList = new ListTag();
        for (Map.Entry<BlockPos, CompoundTag> entry : blockEntities.entrySet()) {
            CompoundTag beTag = new CompoundTag();
            BlockPos pos = entry.getKey();
            CompoundTag entityData = entry.getValue();
            
            beTag.putInt("x", pos.getX());
            beTag.putInt("y", pos.getY());
            beTag.putInt("z", pos.getZ());
            beTag.put("data", entityData.copy());
            
            blockEntitiesList.add(beTag);
        }
        structureData.put("block_entities", blockEntitiesList);
        
        // Save metadata
        CompoundTag metadata = new CompoundTag();
        metadata.putString("name", name);
        metadata.putString("description", description);
        metadata.putLong("timestamp", timestamp);
        structureData.put("metadata", metadata);
        
        tag.put("structure_data", structureData);
    }
    
    public static CapsuleData loadFromNBT(CompoundTag tag) {
        if (!tag.contains("structure_data")) {
            return new CapsuleData();
        }
        
        CompoundTag structureData = tag.getCompound("structure_data").orElse(new CompoundTag());
        
        // Load dimensions
        int width = structureData.getInt("width").orElse(0);
        int height = structureData.getInt("height").orElse(0);
        int depth = structureData.getInt("depth").orElse(0);
        BlockPos dimensions = new BlockPos(width, height, depth);
        
        Map<BlockPos, BlockState> blocks = new HashMap<>();
        Map<BlockPos, CompoundTag> blockEntities = new HashMap<>();
        
        // Load blocks
        if (structureData.contains("blocks")) {
            ListTag blocksList = structureData.getList("blocks").orElse(new ListTag());
            for (int i = 0; i < blocksList.size(); i++) {
                CompoundTag blockTag = blocksList.getCompound(i).orElse(new CompoundTag());
                
                int x = blockTag.getInt("x").orElse(0);
                int y = blockTag.getInt("y").orElse(0);
                int z = blockTag.getInt("z").orElse(0);
                BlockPos pos = new BlockPos(x, y, z);
                
                String blockName = blockTag.getString("block").orElse("");
                ResourceLocation blockId = ResourceLocation.parse(blockName);
                Block block = BuiltInRegistries.BLOCK.get(blockId).map(holder -> holder.value()).orElse(Blocks.AIR);
                
                if (block != null && block != Blocks.AIR) {
                    BlockState state = block.defaultBlockState();
                    blocks.put(pos, state);
                }
            }
        }
        
        // Load block entities
        if (structureData.contains("block_entities")) {
            ListTag blockEntitiesList = structureData.getList("block_entities").orElse(new ListTag());
            for (int i = 0; i < blockEntitiesList.size(); i++) {
                CompoundTag beTag = blockEntitiesList.getCompound(i).orElse(new CompoundTag());
                
                int x = beTag.getInt("x").orElse(0);
                int y = beTag.getInt("y").orElse(0);
                int z = beTag.getInt("z").orElse(0);
                BlockPos pos = new BlockPos(x, y, z);
                
                CompoundTag entityData = beTag.getCompound("data").orElse(new CompoundTag());
                blockEntities.put(pos, entityData);
            }
        }
        
        CapsuleData capsuleData = new CapsuleData(blocks, blockEntities, dimensions);
        
        // Load metadata
        if (structureData.contains("metadata")) {
            CompoundTag metadata = structureData.getCompound("metadata").orElse(new CompoundTag());
            capsuleData.setName(metadata.getString("name").orElse(""));
            capsuleData.setDescription(metadata.getString("description").orElse(""));
            capsuleData.setTimestamp(metadata.getLong("timestamp").orElse(0L));
        }
        
        return capsuleData;
    }
}