package com.cuspymd.hoipoi.items;

import com.cuspymd.hoipoi.data.CapsuleData;
import com.cuspymd.hoipoi.utils.CapsuleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

public class BasicCapsule extends Item {
    private static final int CAPTURE_SIZE = 3;
    
    public BasicCapsule(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        BlockPos clickedPos = context.getClickedPos();
        
        if (level.isClientSide || player == null) {
            return InteractionResult.SUCCESS;
        }
        
        if (player.isShiftKeyDown()) {
            return deployStructure(level, player, itemStack, clickedPos);
        } else {
            return captureStructure(level, player, itemStack, clickedPos);
        }
    }
    
    private InteractionResult captureStructure(Level level, Player player, ItemStack itemStack, BlockPos centerPos) {
        if (hasStoredStructure(itemStack)) {
            player.displayClientMessage(Component.translatable("item.hoipoicapsulemod.basic_capsule.already_has_structure"), false);
            return InteractionResult.FAIL;
        }
        
        try {
            CapsuleData capsuleData = CapsuleUtils.captureStructure(level, centerPos, CAPTURE_SIZE);
            
            if (capsuleData.isEmpty()) {
                player.displayClientMessage(Component.translatable("item.hoipoicapsulemod.basic_capsule.no_blocks_found"), false);
                return InteractionResult.FAIL;
            }
            
            CompoundTag tag = new CompoundTag();
            capsuleData.saveToNBT(tag);
            itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            
            player.displayClientMessage(Component.translatable("item.hoipoicapsulemod.basic_capsule.structure_captured", 
                capsuleData.getBlockCount()), false);
            
            return InteractionResult.SUCCESS;
        } catch (Exception e) {
            player.displayClientMessage(Component.translatable("item.hoipoicapsulemod.basic_capsule.capture_failed"), false);
            return InteractionResult.FAIL;
        }
    }
    
    private InteractionResult deployStructure(Level level, Player player, ItemStack itemStack, BlockPos targetPos) {
        if (!hasStoredStructure(itemStack)) {
            player.displayClientMessage(Component.translatable("item.hoipoicapsulemod.basic_capsule.no_structure"), false);
            return InteractionResult.FAIL;
        }
        
        try {
            CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
            if (customData == null) {
                return InteractionResult.FAIL;
            }
            
            CompoundTag tag = customData.copyTag();
            CapsuleData capsuleData = CapsuleData.loadFromNBT(tag);
            
            boolean success = CapsuleUtils.deployStructure(level, targetPos, capsuleData);
            
            if (success) {
                itemStack.remove(DataComponents.CUSTOM_DATA);
                player.displayClientMessage(Component.translatable("item.hoipoicapsulemod.basic_capsule.structure_deployed"), false);
                return InteractionResult.SUCCESS;
            } else {
                player.displayClientMessage(Component.translatable("item.hoipoicapsulemod.basic_capsule.deploy_failed"), false);
                return InteractionResult.FAIL;
            }
        } catch (Exception e) {
            player.displayClientMessage(Component.translatable("item.hoipoicapsulemod.basic_capsule.deploy_failed"), false);
            return InteractionResult.FAIL;
        }
    }
    
    private boolean hasStoredStructure(ItemStack itemStack) {
        CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return false;
        }
        return customData.copyTag().contains("structure_data");
    }
    
    public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (hasStoredStructure(itemStack)) {
            CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                CompoundTag tag = customData.copyTag();
                CapsuleData capsuleData = CapsuleData.loadFromNBT(tag);
                
                tooltip.add(Component.translatable("item.hoipoicapsulemod.basic_capsule.tooltip.has_structure"));
                tooltip.add(Component.translatable("item.hoipoicapsulemod.basic_capsule.tooltip.block_count", 
                    capsuleData.getBlockCount()));
                tooltip.add(Component.translatable("item.hoipoicapsulemod.basic_capsule.tooltip.size", 
                    capsuleData.getDimensions().getX(), 
                    capsuleData.getDimensions().getY(), 
                    capsuleData.getDimensions().getZ()));
            }
        } else {
            tooltip.add(Component.translatable("item.hoipoicapsulemod.basic_capsule.tooltip.empty"));
        }
        
        tooltip.add(Component.translatable("item.hoipoicapsulemod.basic_capsule.tooltip.usage1"));
        tooltip.add(Component.translatable("item.hoipoicapsulemod.basic_capsule.tooltip.usage2"));
    }
    
    @Override
    public boolean isFoil(ItemStack itemStack) {
        return hasStoredStructure(itemStack);
    }
}