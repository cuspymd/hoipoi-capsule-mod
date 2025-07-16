package cuspymd.hoipoi.capsule.registry;

import java.util.function.Function;

import cuspymd.hoipoi.capsule.HoipoiCapsuleMod;
import cuspymd.hoipoi.capsule.item.BasicCapsule;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItems {
    
    public static final Item BASIC_CAPSULE = register("basic_capsule", 
            BasicCapsule::new, new Item.Settings().maxCount(1));
    
    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
		// Create the item key.
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(HoipoiCapsuleMod.MOD_ID, name));

		// Create the item instance.
		Item item = itemFactory.apply(settings.registryKey(itemKey));

		// Register the item.
		Registry.register(Registries.ITEM, itemKey, item);

		return item;
	}
    
    public static void initialize() {
        HoipoiCapsuleMod.LOGGER.info("Registering items for " + HoipoiCapsuleMod.MOD_ID);
    }
}