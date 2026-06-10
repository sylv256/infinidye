package gay.sylv.infinidye.attachment;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import gay.sylv.infinidye.ExtraByteBufCodecs;

public record InfinidyeAttachment(int[] colors, int nonZeroCount, int identity, BlockPos lastChangedPos) {
	public static final Codec<InfinidyeAttachment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT_STREAM.xmap(IntStream::toArray, IntStream::of)
					.fieldOf("colors")
					.forGetter(InfinidyeAttachment::colors),
			Codec.INT.fieldOf("non_zero_count").forGetter(InfinidyeAttachment::nonZeroCount)
	).apply(instance, InfinidyeAttachment::of));
	public static final StreamCodec<FriendlyByteBuf, InfinidyeAttachment> STREAM_CODEC = StreamCodec.composite(
			ExtraByteBufCodecs.VAR_INT_ARRAY, InfinidyeAttachment::colors,
			ByteBufCodecs.VAR_INT, InfinidyeAttachment::nonZeroCount,
			BlockPos.STREAM_CODEC, InfinidyeAttachment::lastChangedPos,
			InfinidyeAttachment::of
	);
	private static final Random RANDOM = ThreadLocalRandom.current();

	public static InfinidyeAttachment of(int[] colors, int nonZeroCount) {
		return new InfinidyeAttachment(colors, nonZeroCount, RANDOM.nextInt(), BlockPos.ZERO);
	}

	public static InfinidyeAttachment of(int[] colors, int nonZeroCount, BlockPos lastChangedPos) {
		return new InfinidyeAttachment(colors, nonZeroCount, RANDOM.nextInt(), lastChangedPos);
	}

	public static InfinidyeAttachment update(int[] colors, int nonZeroCount, int previousIdentity, BlockPos lastChangedPos) {
		int identity1 = RANDOM.nextInt();

		while (identity1 == previousIdentity) {
			identity1 = RANDOM.nextInt();
		}

		return new InfinidyeAttachment(colors, nonZeroCount, identity1, lastChangedPos);
	}

	public static InfinidyeAttachment empty(BlockPos lastChangedPos) {
		return of(new int[0], 0, lastChangedPos);
	}
}
