package hugman.mod.util.handlers;

import hugman.mod.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class LootTableHandler
{
	public static final ResourceLocation TOAD = LootTableList.register(new ResourceLocation(Reference.MODID, "entities/toad"));
}
