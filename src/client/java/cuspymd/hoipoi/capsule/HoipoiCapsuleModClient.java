package cuspymd.hoipoi.capsule;

import cuspymd.hoipoi.capsule.item.BasicCapsule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import cuspymd.hoipoi.capsule.network.CapturePayload;
import cuspymd.hoipoi.capsule.network.PlacePayload;
import org.lwjgl.glfw.GLFW;

public class HoipoiCapsuleModClient implements ClientModInitializer {
	private static boolean isRightClickHeld = false;
	private static boolean wasRightClickPressed = false;
	
	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			handleRightClickInput(client);
		});
		
		WorldRenderEvents.AFTER_TRANSLUCENT.register((context) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			PlayerEntity player = client.player;

			if (player == null) {
				return;
			}

			if (player.getMainHandStack().getItem() instanceof BasicCapsule capsule && isRightClickHeld) {
				renderAreaPreview(context, capsule.hasStoredStructure(player.getMainHandStack()));
			}
		});
	}
	
	private void handleRightClickInput(MinecraftClient client) {
		if (client.player == null) return;
		
		boolean currentRightClick = GLFW.glfwGetMouseButton(client.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
		
		if (currentRightClick && !wasRightClickPressed) {
			isRightClickHeld = true;
		} else if (!currentRightClick && wasRightClickPressed) {
			isRightClickHeld = false;

			if (client.player.getMainHandStack().getItem() instanceof BasicCapsule capsule) {
				if (capsule.hasStoredStructure(client.player.getMainHandStack())) {
					ClientPlayNetworking.send(new PlacePayload());
				} else {
					ClientPlayNetworking.send(new CapturePayload());
				}
			}
		}
		
		wasRightClickPressed = currentRightClick;
	}
	
	private void renderAreaPreview(WorldRenderContext context, boolean hasStructure) {
		MinecraftClient client = MinecraftClient.getInstance();
		PlayerEntity player = client.player;
		if (player == null) return;
		
		Vec3d playerPos = player.getPos();
		Vec3d lookDir = player.getRotationVec(1.0f);
		
		BlockPos targetPos = new BlockPos((int) Math.floor(playerPos.x + lookDir.x * 5), 
		                                  (int) Math.floor(playerPos.y), 
		                                  (int) Math.floor(playerPos.z + lookDir.z * 5));
		
		MatrixStack matrices = context.matrixStack();
		VertexConsumerProvider.Immediate vertexConsumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLines());

		matrices.push();
		
		Vec3d cameraPos = context.camera().getPos();
		matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		
        BlockPos min = targetPos.add(-1, 0, -1);
        BlockPos max = targetPos.add(1, 2, 1);
        Box areaBox = new Box(min.getX(), min.getY(), min.getZ(), max.getX() + 1, max.getY() + 1, max.getZ() + 1);

        float r = hasStructure ? 1.0f : 0.0f;
        float g = hasStructure ? 0.0f : 1.0f;
        float b = 0.0f;
        float a = 0.5f;

        drawBox(matrices, vertexConsumer, areaBox, r, g, b, a);

		matrices.pop();
		vertexConsumers.draw();
	}

	private void drawBox(MatrixStack matrices, VertexConsumer vertexConsumer, Box box, float r, float g, float b, float a) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        // Draw the 12 lines of the box
        // Bottom face
        vertexConsumer.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).color(r, g, b, a).normal(1, 0, 0);
        vertexConsumer.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).color(r, g, b, a).normal(1, 0, 0);

        vertexConsumer.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).color(r, g, b, a).normal(0, 0, 1);
        vertexConsumer.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).color(r, g, b, a).normal(0, 0, 1);

        vertexConsumer.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).color(r, g, b, a).normal(-1, 0, 0);
        vertexConsumer.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).color(r, g, b, a).normal(-1, 0, 0);

        vertexConsumer.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).color(r, g, b, a).normal(0, 0, -1);
        vertexConsumer.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).color(r, g, b, a).normal(0, 0, -1);

        // Top face
        vertexConsumer.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ).color(r, g, b, a).normal(1, 0, 0);
        vertexConsumer.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ).color(r, g, b, a).normal(1, 0, 0);

        vertexConsumer.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ).color(r, g, b, a).normal(0, 0, 1);
        vertexConsumer.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ).color(r, g, b, a).normal(0, 0, 1);

        vertexConsumer.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ).color(r, g, b, a).normal(-1, 0, 0);
        vertexConsumer.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ).color(r, g, b, a).normal(-1, 0, 0);

        vertexConsumer.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ).color(r, g, b, a).normal(0, 0, -1);
        vertexConsumer.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ).color(r, g, b, a).normal(0, 0, -1);

        // Vertical lines
        vertexConsumer.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).color(r, g, b, a).normal(0, 1, 0);
        vertexConsumer.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ).color(r, g, b, a).normal(0, 1, 0);

        vertexConsumer.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).color(r, g, b, a).normal(0, 1, 0);
        vertexConsumer.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ).color(r, g, b, a).normal(0, 1, 0);

        vertexConsumer.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).color(r, g, b, a).normal(0, 1, 0);
        vertexConsumer.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ).color(r, g, b, a).normal(0, 1, 0);

        vertexConsumer.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).color(r, g, b, a).normal(0, 1, 0);
        vertexConsumer.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ).color(r, g, b, a).normal(0, 1, 0);
    }
}