package gay.sylv.infinidye.item;

import static net.minecraft.core.SectionPos.sectionRelative;

import java.util.Arrays;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;

import gay.sylv.infinidye.attachment.InfinidyeAttachment;
import gay.sylv.infinidye.attachment.ModAttachments;

public class InfinidyeItem extends Item {
	public InfinidyeItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();

		if (!level.isClientSide() && level.getServer() != null) {
			DyedItemColor dyedItemColor = context.getItemInHand().get(DataComponents.DYED_COLOR);
			int color;

			if (dyedItemColor == null) {
				color = 0;
			} else {
				color = dyedItemColor.rgb();
			}

			setDyeColor(context.getClickedPos(), level, color);
		}

		return InteractionResult.SUCCESS_SERVER;
	}

	public static void setDyeColor(
			BlockPos blockPos,
			Level level,
			int color
	) {
		ChunkAccess chunk = level.getChunkAt(blockPos);
		int arrayLength = 4096 * chunk.getSectionsCount();

		// skip sending unnecessary packets and saving unnecessary data
		//  if there already is no attachment
		if (color == 0 && !chunk.hasAttached(ModAttachments.INFINIDYE)) {
			return;
		}

		InfinidyeAttachment attachment = chunk.getAttachedOrGet(
				ModAttachments.INFINIDYE,
				() -> InfinidyeAttachment.of(new int[arrayLength], 0, blockPos)
		);
		int[] colors = attachment.colors();

		if (colors.length < arrayLength) {
			colors = Arrays.copyOf(colors, arrayLength);
		}

		int nonZeroCount = attachment.nonZeroCount();
		int previousColor = colors[getColorIndex(blockPos, chunk)];

		if (previousColor == 0 && color != 0) {
			nonZeroCount++;
		} else if (previousColor != 0 && color == 0) {
			nonZeroCount--;
		}

		// don't waste space; delete unused data
		if (nonZeroCount == 0) {
			chunk.setAttached(ModAttachments.INFINIDYE, InfinidyeAttachment.empty(blockPos));
			chunk.setAttached(ModAttachments.INFINIDYE, null);
			return;
		}

		colors[getColorIndex(blockPos, chunk)] = color;
		chunk.setAttached(
				ModAttachments.INFINIDYE,
				InfinidyeAttachment.update(colors, nonZeroCount, attachment.identity(), blockPos)
		);
	}

	public static int getColorIndex(
			BlockPos blockPos,
			ChunkAccess chunk
	) {
		return sectionRelative(blockPos.getX()) * 256 + sectionRelative(blockPos.getY()) * 16 + sectionRelative(blockPos.getZ()) + chunk.getSectionIndex(blockPos.getY()) * 4096;
	}
}
