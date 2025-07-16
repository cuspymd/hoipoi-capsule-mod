package cuspymd.hoipoi.capsule.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import cuspymd.hoipoi.capsule.data.CapsuleData;

import java.util.HashMap;
import java.util.Map;

public class CapsuleUtils {
    
    public static CapsuleData captureStructure(ServerWorld world, BlockPos startPos, BlockPos endPos) {
        Map<BlockPos, BlockState> blocks = new HashMap<>();
        
        int minX = Math.min(startPos.getX(), endPos.getX());
        int maxX = Math.max(startPos.getX(), endPos.getX());
        int minY = Math.min(startPos.getY(), endPos.getY());
        int maxY = Math.max(startPos.getY(), endPos.getY());
        int minZ = Math.min(startPos.getZ(), endPos.getZ());
        int maxZ = Math.max(startPos.getZ(), endPos.getZ());
        
        Vec3i dimensions = new Vec3i(maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1);
        
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    
                    if (!state.isAir()) {
                        BlockPos relativePos = pos.subtract(startPos);
                        blocks.put(relativePos, state);
                    }
                }
            }
        }
        
        return new CapsuleData(blocks, dimensions, "Captured Structure", "3x3x3 structure");
    }
    
    public static boolean placeStructure(ServerWorld world, BlockPos startPos, CapsuleData capsuleData) {
        if (capsuleData == null || capsuleData.isEmpty()) {
            return false;
        }
        
        if (!canPlaceStructure(world, startPos, capsuleData)) {
            return false;
        }
        
        Map<BlockPos, BlockState> blocks = capsuleData.getBlocks();
        
        for (Map.Entry<BlockPos, BlockState> entry : blocks.entrySet()) {
            BlockPos relativePos = entry.getKey();
            BlockState state = entry.getValue();
            
            BlockPos worldPos = startPos.add(relativePos);
            
            if (isValidPlacementPosition(world, worldPos)) {
                world.setBlockState(worldPos, state, Block.NOTIFY_ALL);
            }
        }
        
        return true;
    }
    
    public static boolean canPlaceStructure(World world, BlockPos startPos, CapsuleData capsuleData) {
        if (capsuleData == null || capsuleData.isEmpty()) {
            return false;
        }
        
        Vec3i dimensions = capsuleData.getDimensions();
        BlockPos endPos = startPos.add(dimensions.getX() - 1, dimensions.getY() - 1, dimensions.getZ() - 1);
        
        if (!world.isInBuildLimit(startPos) || !world.isInBuildLimit(endPos)) {
            return false;
        }
        
        return true;
    }
    
    public static boolean isValidPlacementPosition(World world, BlockPos pos) {
        if (!world.isInBuildLimit(pos)) {
            return false;
        }
        
        return true;
    }
    
    public static boolean hasObstructions(World world, BlockPos startPos, Vec3i dimensions) {
        BlockPos endPos = startPos.add(dimensions.getX() - 1, dimensions.getY() - 1, dimensions.getZ() - 1);
        
        int minX = Math.min(startPos.getX(), endPos.getX());
        int maxX = Math.max(startPos.getX(), endPos.getX());
        int minY = Math.min(startPos.getY(), endPos.getY());
        int maxY = Math.max(startPos.getY(), endPos.getY());
        int minZ = Math.min(startPos.getZ(), endPos.getZ());
        int maxZ = Math.max(startPos.getZ(), endPos.getZ());
        
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    
                    if (!state.isAir() && !state.isReplaceable()) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public static void clearArea(ServerWorld world, BlockPos startPos, Vec3i dimensions) {
        BlockPos endPos = startPos.add(dimensions.getX() - 1, dimensions.getY() - 1, dimensions.getZ() - 1);
        
        int minX = Math.min(startPos.getX(), endPos.getX());
        int maxX = Math.max(startPos.getX(), endPos.getX());
        int minY = Math.min(startPos.getY(), endPos.getY());
        int maxY = Math.max(startPos.getY(), endPos.getY());
        int minZ = Math.min(startPos.getZ(), endPos.getZ());
        int maxZ = Math.max(startPos.getZ(), endPos.getZ());
        
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
                }
            }
        }
    }
}