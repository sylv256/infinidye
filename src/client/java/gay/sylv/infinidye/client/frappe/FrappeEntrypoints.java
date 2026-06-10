package gay.sylv.infinidye.client.frappe;

import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterialRegistryEntrypoint;

@SuppressWarnings("UnstableApiUsage")
public class FrappeEntrypoints implements TerrainMaterialRegistryEntrypoint {
	@Override
	public void onTerrainMaterialRegistry() {
		Materials.initialize();
	}
}
