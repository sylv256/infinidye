package gay.sylv.infinidye.client.frappe;

import static gay.sylv.infinidye.Infinidye.modId;

import java.util.function.Consumer;

import gay.sylv.frappe.api.ext.material.Material;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterialExtension;

@SuppressWarnings("UnstableApiUsage")
public class Materials {
	public static final TerrainMaterial RAINBOW = register(
			"rainbow",
			builder -> builder
					.complexity(Material.Complexity.COMPLEX)
					.label("Infinidye Rainbow")
	);

	public static void initialize() {
	}

	private static TerrainMaterial register(String path, Consumer<TerrainMaterial.Builder> builderConsumer) {
		TerrainMaterial.Builder builder = TerrainMaterial.Builder.of(modId(path));
		builderConsumer.accept(builder);
		TerrainMaterial terrainMaterial = builder.build();
		TerrainMaterialExtension.registerMaterial(terrainMaterial);
		return terrainMaterial;
	}
}
