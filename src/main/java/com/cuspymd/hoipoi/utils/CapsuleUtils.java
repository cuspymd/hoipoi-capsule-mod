package com.cuspymd.hoipoi.utils;

import com.cuspymd.hoipoi.data.CapsuleData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class CapsuleUtils {
    
    public static CapsuleData captureStructure(Level level, BlockPos centerPos, int size) {
        if (level == null || centerPos == null) {
            return new CapsuleData();
        }
        
        Map<BlockPos, BlockState> blocks = new HashMap<>();
        Map<BlockPos, CompoundTag> blockEntities = new HashMap<>();
        
        int halfSize = size / 2;
        BlockPos minPos = centerPos.offset(-halfSize, -halfSize, -halfSize);
        BlockPos maxPos = centerPos.offset(halfSize, halfSize, halfSize);
        
        // Capture blocks in the 3x3x3 area
        for (int x = minPos.getX(); x <= maxPos.getX(); x++) {
            for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                for (int z = minPos.getZ(); z <= maxPos.getZ(); z++) {
                    BlockPos worldPos = new BlockPos(x, y, z);
                    BlockPos relativePos = worldPos.subtract(minPos);
                    
                    BlockState blockState = level.getBlockState(worldPos);
                    
                    // Only store non-air blocks
                    if (!blockState.isAir()) {
                        blocks.put(relativePos, blockState);
                        
                        // Capture block entity data if present
                        BlockEntity blockEntity = level.getBlockEntity(worldPos);
                        if (blockEntity != null) {
                            try {
                                CompoundTag entityTag = blockEntity.saveWithoutMetadata(level.registryAccess());
                                blockEntities.put(relativePos, entityTag);
                            } catch (Exception e) {
                                // Skip if save fails
                            }
                        }
                    }
                }
            }
        }
        
        BlockPos dimensions = new BlockPos(size, size, size);
        return new CapsuleData(blocks, blockEntities, dimensions);
    }
    
    public static boolean deployStructure(Level level, BlockPos targetPos, CapsuleData capsuleData) {
        if (level == null || targetPos == null || capsuleData == null || capsuleData.isEmpty()) {
            return false;
        }

        try {
            Map<BlockPos, BlockState> allBlocks = capsuleData.getAllBlocks();
            Map<BlockPos, CompoundTag> allBlockEntities = capsuleData.getAllBlockEntities();

            for (Map.Entry<BlockPos, BlockState> entry : allBlocks.entrySet()) {
                BlockPos relativePos = entry.getKey();
                BlockState blockState = entry.getValue();
                BlockPos worldPos = targetPos.offset(relativePos);

                if (isValidPosition(level, worldPos)) {
                    level.setBlock(worldPos, blockState, 3);

                    if (allBlockEntities.containsKey(relativePos)) {
                        CompoundTag entityData = allBlockEntities.get(relativePos);
                        BlockEntity blockEntity = BlockEntity.loadStatic(worldPos, blockState, entityData, level.registryAccess());
                        if (blockEntity != null) {
                            level.setBlockEntity(blockEntity);
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean canDeployAt(Level level, BlockPos targetPos, CapsuleData capsuleData) {
        if (level == null || targetPos == null || capsuleData == null) {
            return false;
        }
        
        Map<BlockPos, BlockState> allBlocks = capsuleData.getAllBlocks();
        for (BlockPos relativePos : allBlocks.keySet()) {
            BlockPos worldPos = targetPos.offset(relativePos);
            
            if (!isValidPosition(level, worldPos)) {
                return false;
            }
        }
        
        return true;
    }
    
    private static boolean isValidPosition(Level level, BlockPos pos) {
        // Check if position is within world bounds
        if (pos.getY() < level.getMinY() || pos.getY() >= level.getMaxY()) {
            return false;
        }
        
        // Check if chunk is loaded
        if (!level.hasChunkAt(pos)) {
            return false;
        }
        
        return true;
    }
    
    public static BlockPos calculateCenterPos(BlockPos clickedPos, int size) {
        return clickedPos.above();
    }
    
    public static boolean isValidCaptureArea(Level level, BlockPos centerPos, int size) {
        if (level == null || centerPos == null) {
            return false;
        }
        
        int halfSize = size / 2;
        BlockPos minPos = centerPos.offset(-halfSize, -halfSize, -halfSize);
        BlockPos maxPos = centerPos.offset(halfSize, halfSize, halfSize);
        
        // Check if all positions are within world bounds
        for (int x = minPos.getX(); x <= maxPos.getX(); x++) {
            for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                for (int z = minPos.getZ(); z <= maxPos.getZ(); z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!isValidPosition(level, pos)) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    public static int countNonAirBlocks(Level level, BlockPos centerPos, int size) {
        if (level == null || centerPos == null) {
            return 0;
        }
        
        int count = 0;
        int halfSize = size / 2;
        BlockPos minPos = centerPos.offset(-halfSize, -halfSize, -halfSize);
        BlockPos maxPos = centerPos.offset(halfSize, halfSize, halfSize);
        
        for (int x = minPos.getX(); x <= maxPos.getX(); x++) {
            for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                for (int z = minPos.getZ(); z <= maxPos.getZ(); z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    if (!state.isAir()) {
                        count++;
                    }
                }
            }
        }
        
        return count;
    }
}