package cuspymd.hoipoi.capsule.registry;

import cuspymd.hoipoi.capsule.HoipoiCapsuleMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    
    public static final RegistryKey<ItemGroup> HOIPOI_CAPSULE_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(HoipoiCapsuleMod.MOD_ID, "hoipoi_capsule_group"));
    public static final ItemGroup HOIPOI_CAPSULE_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            HOIPOI_CAPSULE_GROUP_KEY,
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemGroup.hoipoi-capsule-mod"))
                    .icon(() -> new ItemStack(ModItems.BASIC_CAPSULE))
                    .build()
    );
    
    public static void initialize() {
        HoipoiCapsuleMod.LOGGER.info("Registering item groups for " + HoipoiCapsuleMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(HOIPOI_CAPSULE_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(ModItems.BASIC_CAPSULE);
        });
    }
}