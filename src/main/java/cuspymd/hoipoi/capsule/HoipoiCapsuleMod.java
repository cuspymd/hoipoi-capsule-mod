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
					// Use the same calculation as client preview
					var playerPos = context.player().getPos();
					var lookDir = context.player().getRotationVec(1.0f);
					var targetPos = new net.minecraft.util.math.BlockPos(
						(int) Math.floor(playerPos.x + lookDir.x * 5), 
						(int) Math.floor(playerPos.y), 
						(int) Math.floor(playerPos.z + lookDir.z * 5)
					);
					capsule.captureStructure(context.player().getWorld(), targetPos, context.player(), context.player().getMainHandStack());
				}
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(PlacePayload.ID, (payload, context) -> {
			context.server().execute(() -> {
				if (context.player().getMainHandStack().getItem() instanceof BasicCapsule capsule) {
					// Use the same calculation as client preview
					var playerPos = context.player().getPos();
					var lookDir = context.player().getRotationVec(1.0f);
					var targetPos = new net.minecraft.util.math.BlockPos(
						(int) Math.floor(playerPos.x + lookDir.x * 5), 
						(int) Math.floor(playerPos.y), 
						(int) Math.floor(playerPos.z + lookDir.z * 5)
					);
					capsule.placeStructure(context.player().getWorld(), targetPos, context.player(), context.player().getMainHandStack());
				}
			});
		});
		
		LOGGER.info("HoiPoi Capsule Mod initialized successfully!");
	}
}