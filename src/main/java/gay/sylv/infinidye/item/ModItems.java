package gay.sylv.infinidye.item;

import static gay.sylv.infinidye.Infinidye.modId;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class ModItems {
	public static final InfinidyeItem INFINIDYE = register(
			"infinidye",
			InfinidyeItem::new,
			new Item.Properties()
	);

	private ModItems() {
	}

	public static void initialize() {
	}

	private static <T extends Item> T register(String path, Function<Item.Properties, T> itemFactory, Item.Properties properties) {
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, modId(path));
		T item = itemFactory.apply(properties.setId(itemKey));
		return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
	}
}
