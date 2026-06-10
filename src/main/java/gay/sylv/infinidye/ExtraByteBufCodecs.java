package gay.sylv.infinidye;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public final class ExtraByteBufCodecs {
	public static final StreamCodec<FriendlyByteBuf, int[]> VAR_INT_ARRAY = new StreamCodec<>() {
		@Override
		public int[] decode(FriendlyByteBuf input) {
			return input.readVarIntArray();
		}

		@Override
		public void encode(
				FriendlyByteBuf output,
				int[] value
		) {
			output.writeVarIntArray(value);
		}
	};

	private ExtraByteBufCodecs() {
	}
}
