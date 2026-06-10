package gay.sylv.infinidye.block;

import static gay.sylv.infinidye.Infinidye.modId;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class ModBlocks {
	private ModBlocks() {
	}

	public static void initialize() {
	}

	private static <T extends Block> T register(String path, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties properties) {
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, modId(path));
		T block = blockFactory.apply(properties.setId(key));
		return Registry.register(BuiltInRegistries.BLOCK, key, block);
	}

	private static <B extends Block, I extends BlockItem> BlockWithItem<B, I> register(
			String path,
			Function<BlockBehaviour.Properties, B> blockFactory,
			BlockBehaviour.Properties blockProperties,
			Function<Item.Properties, I> itemFactory,
			Item.Properties itemProperties
	) {
		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, modId(path));
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, modId(path));
		B block = blockFactory.apply(blockProperties.setId(blockKey));
		I item = itemFactory.apply(itemProperties.setId(itemKey).useBlockDescriptionPrefix());
		return new BlockWithItem<>(block, item);
	}

	public record BlockWithItem<B extends Block, I extends BlockItem>(B block, I item) {
	}
}
