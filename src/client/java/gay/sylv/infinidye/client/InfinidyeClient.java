package gay.sylv.infinidye.client;

import java.util.function.Predicate;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;

import gay.sylv.frappe.api.ext.quad_view.FrappeMutableQuadView;
import gay.sylv.frappe.api.ext.terrain_material.MQV_ExtTerrainMaterial;
import gay.sylv.infinidye.attachment.ModAttachments;
import gay.sylv.infinidye.client.frappe.Materials;
import gay.sylv.infinidye.item.InfinidyeItem;

@SuppressWarnings("UnstableApiUsage")
public class InfinidyeClient implements ClientModInitializer {
	private static final Identifier FRAPPE_DEFAULT_TERRAIN_MATERIAL = Identifier.fromNamespaceAndPath("frappe-ext-terrain-material", "default");

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ModelLoadingPlugin.register(ctx -> ctx.modifyBlockModelAfterBake()
				.register((model, _) -> new WrapperBlockStateModel(model) {
					@Override
					public void emitQuads(
							QuadEmitter emitter,
							BlockAndTintGetter level,
							BlockPos pos,
							BlockState state,
							RandomSource random,
							Predicate<@Nullable Direction> cullTest
					) {
						AttachmentTarget attachmentTarget = level.frappe$getAttachmentTargetChunkAt(pos);

						if (attachmentTarget == null || !attachmentTarget.hasAttached(ModAttachments.INFINIDYE)) {
							super.emitQuads(emitter, level, pos, state, random, cullTest);
							return;
						}

						ChunkAccess chunk = level.frappe$getChunkAt(pos);

						if (chunk == null) {
							super.emitQuads(emitter, level, pos, state, random, cullTest);
							return;
						}

						int[] colors = attachmentTarget.getAttachedOrThrow(ModAttachments.INFINIDYE).colors();
						int color = colors[InfinidyeItem.getColorIndex(pos, chunk)];

						emitter.pushTransform(quad -> {
							if (color == 0) {
								return true;
							}

							MQV_ExtTerrainMaterial materialQuad = FrappeMutableQuadView.of(quad)
									.as(MQV_ExtTerrainMaterial.class);

							// don't override others' complex terrain materials
							if (!materialQuad.frappe$terrainMaterial().shaderId().equals(FRAPPE_DEFAULT_TERRAIN_MATERIAL) && !materialQuad.frappe$terrainMaterial().simple()) {
								return true;
							}

							materialQuad.frappe$terrainMaterial(Materials.RAINBOW);

							for (int i = 0; i < 4; i++) {
								materialQuad.frappe$uv(i, ARGB.redFloat(color), ARGB.greenFloat(color));
								materialQuad.color(i, ARGB.color(ARGB.blue(color), materialQuad.color(i)));
							}

							return true;
						});
						super.emitQuads(emitter, level, pos, state, random, cullTest);
						emitter.popTransform();
					}
				}));

		ClientChunkEvents.CHUNK_LOAD.register((level, chunk) -> {
			chunk.onAttachedSet(ModAttachments.INFINIDYE)
					.register(((_, newValue) -> {
						if (newValue == null) {
							return;
						}

						ChunkPos pos = chunk.getPos();
						Minecraft.getInstance().levelRenderer.setSectionDirty(pos.x(), chunk.getSectionYFromSectionIndex(chunk.getSectionIndex(newValue.lastChangedPos().getY())), pos.z());
					}));
		});
	}
}
