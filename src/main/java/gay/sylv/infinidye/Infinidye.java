package gay.sylv.infinidye;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.resources.Identifier;

import gay.sylv.infinidye.attachment.ModAttachments;
import gay.sylv.infinidye.block.ModBlocks;
import gay.sylv.infinidye.item.ModItems;

public class Infinidye implements ModInitializer {
	public static final String MOD_ID = "infinidye";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModAttachments.initialize();
		ModItems.initialize();
		ModBlocks.initialize();
	}

	public static Identifier modId(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
