package cuspymd.hoipoi.capsule;

import cuspymd.hoipoi.capsule.network.CapturePayload;
import cuspymd.hoipoi.capsule.network.PlacePayload;
import cuspymd.hoipoi.capsule.item.BasicCapsule;
import cuspymd.hoipoi.capsule.registry.ModItemGroups;
import cuspymd.hoipoi.capsule.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HoipoiCapsuleMod implements ModInitializer {
	public static final String MOD_ID = "hoipoi-capsule-mod";
	public static final Identifier CAPTURE_PACKET_ID = Identifier.of(MOD_ID, "capture");
	public static final Identifier PLACE_PACKET_ID = Identifier.of(MOD_ID, "place");

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Initializing HoiPoi Capsule Mod");
		
		ModItems.initialize();
		ModItemGroups.initialize();

		PayloadTypeRegistry.playC2S().register(CapturePayload.ID, CapturePayload.CODEC);
		PayloadTypeRegistry.playC2S().register(PlacePayload.ID, PlacePayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(CapturePayload.ID, (payload, context) -> {
			context.server().execute(() -> {
				if (context.player().getMainHandStack().getItem() instanceof BasicCapsule capsule) {
					double reachDistance = 5.0; // Or get from player attributes
					var hitResult = context.player().raycast(reachDistance, 1.0f, false);
					var pos = ((net.minecraft.util.hit.BlockHitResult)hitResult).getBlockPos();
					capsule.captureStructure(context.player().getWorld(), pos, context.player(), context.player().getMainHandStack());
				}
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(PlacePayload.ID, (payload, context) -> {
			context.server().execute(() -> {
				if (context.player().getMainHandStack().getItem() instanceof BasicCapsule capsule) {
					double reachDistance = 5.0; // Or get from player attributes
					var hitResult = context.player().raycast(reachDistance, 1.0f, false);
					var pos = ((net.minecraft.util.hit.BlockHitResult)hitResult).getBlockPos();
					capsule.placeStructure(context.player().getWorld(), pos.up(), context.player(), context.player().getMainHandStack());
				}
			});
		});
		
		LOGGER.info("HoiPoi Capsule Mod initialized successfully!");
	}
}