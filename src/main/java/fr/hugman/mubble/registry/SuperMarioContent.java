package fr.hugman.mubble.registry;

import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.block.DawnBlockSettings;
import fr.hugman.mubble.Mubble;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class SuperMarioContent {
	public static final ItemGroup GROUP = FabricItemGroup.builder(Mubble.id("super_mario"))
			.displayName(Text.translatable("item_group.mubble.super_mario"))
			.icon(() -> new ItemStack(SuperMarioContent.QUESTION_BLOCK))
			.entries((enabledFeatures, entries, operatorEnabled) -> {
				entries.add(SuperMarioContent.QUESTION_BLOCK);
			})
			.build();

	public static final Block QUESTION_BLOCK = new Block(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.YELLOW).item());

	public static void init() {
		Registrar.add(Mubble.id("question_block"), QUESTION_BLOCK);
	}
}
