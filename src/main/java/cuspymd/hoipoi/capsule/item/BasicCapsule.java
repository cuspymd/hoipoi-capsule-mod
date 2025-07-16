package cuspymd.hoipoi.capsule.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import cuspymd.hoipoi.capsule.data.CapsuleData;
import cuspymd.hoipoi.capsule.util.CapsuleUtils;

import java.util.List;

public class BasicCapsule extends Item {
    public BasicCapsule(Settings settings) {
        super(settings);
    }
    
    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        
        if (!world.isClient()) {
            if (hasStoredStructure(itemStack)) {
                player.sendMessage(Text.literal("Right-click on a block to place the structure!"), true);
            } else {
                player.sendMessage(Text.literal("Right-click on a block to capture a 3x3x3 structure!"), true);
            }
        }
        
        return ActionResult.SUCCESS;
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.PASS;
    }
    
    public ActionResult captureStructure(World world, BlockPos centerPos, PlayerEntity player, ItemStack itemStack) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return ActionResult.FAIL;
        }
        
        BlockPos startPos = centerPos.add(-1, 0, -1);
        BlockPos endPos = centerPos.add(1, 2, 1);
        
        CapsuleData capsuleData = CapsuleUtils.captureStructure(serverWorld, startPos, endPos);
        
        if (capsuleData != null) {
            storeStructure(itemStack, capsuleData);
            player.sendMessage(Text.literal("Structure captured! (3x3x3)"), false);
            return ActionResult.SUCCESS;
        } else {
            player.sendMessage(Text.literal("Failed to capture structure!"), false);
            return ActionResult.FAIL;
        }
    }
    
    public ActionResult placeStructure(World world, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return ActionResult.FAIL;
        }
        
        CapsuleData capsuleData = getStoredStructure(itemStack);
        
        if (capsuleData != null) {
            boolean success = CapsuleUtils.placeStructure(serverWorld, pos, capsuleData);
            
            if (success) {
                clearStoredStructure(itemStack);
                player.sendMessage(Text.literal("Structure placed!"), false);
                return ActionResult.SUCCESS;
            } else {
                player.sendMessage(Text.literal("Failed to place structure!"), false);
                return ActionResult.FAIL;
            }
        }
        
        return ActionResult.FAIL;
    }
    
    private void storeStructure(ItemStack itemStack, CapsuleData capsuleData) {
        NbtCompound nbt = new NbtCompound();
        nbt.put("CapsuleData", capsuleData.toNbt());
        itemStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }
    
    private CapsuleData getStoredStructure(ItemStack itemStack) {
        NbtComponent nbtComponent = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
        if (nbtComponent != null && nbtComponent.contains("CapsuleData")) {
            return CapsuleData.fromNbt(nbtComponent.copyNbt().getCompound("CapsuleData"));
        }
        return null;
    }
    
    public boolean hasStoredStructure(ItemStack itemStack) {
        return getStoredStructure(itemStack) != null;
    }
    
    private void clearStoredStructure(ItemStack itemStack) {
        itemStack.remove(DataComponentTypes.CUSTOM_DATA);
    }
    
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (hasStoredStructure(stack)) {
            tooltip.add(Text.literal("Contains a 3x3x3 structure"));
            tooltip.add(Text.literal("Right-click to place"));
        } else {
            tooltip.add(Text.literal("Empty capsule"));
            tooltip.add(Text.literal("Right-click on a block to capture"));
        }
    }
}