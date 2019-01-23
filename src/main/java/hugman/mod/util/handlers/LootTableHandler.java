package hugman.mod.util.handlers;

import hugman.mod.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class LootTableHandler
{
	//ENTITIES
		//TOAD
	public static final ResourceLocation TOAD = LootTableList.register(new ResourceLocation(Reference.MODID, "entities/toad/normal"));
	public static final ResourceLocation RED_TOAD = LootTableList.register(new ResourceLocation(Reference.MODID, "entities/toad/species/red"));
	public static final ResourceLocation BLUE_TOAD = LootTableList.register(new ResourceLocation(Reference.MODID, "entities/toad/species/blue"));
	public static final ResourceLocation GREEN_TOAD = LootTableList.register(new ResourceLocation(Reference.MODID, "entities/toad/species/green"));
	public static final ResourceLocation YELLOW_TOAD = LootTableList.register(new ResourceLocation(Reference.MODID, "entities/toad/species/yellow"));
	public static final ResourceLocation PURPLE_TOAD = LootTableList.register(new ResourceLocation(Reference.MODID, "entities/toad/species/purple"));
	public static final ResourceLocation CAPTAIN_TOAD = LootTableList.register(new ResourceLocation(Reference.MODID, "entities/toad/brigade/captain"));
	public static final ResourceLocation HINT_TOAD = LootTableList.register(new ResourceLocation(Reference.MODID, "entities/toad/brigade/hint"));
	public static final ResourceLocation BANKTOAD = LootTableList.register(new ResourceLocation(Reference.MODID, "entities/toad/brigade/bank"));
	public static final ResourceLocation YELLOWB_TOAD = LootTableList.register(new ResourceLocation(Reference.MODID, "entities/toad/brigade/yellow"));
	public static final ResourceLocation MAILTOAD = LootTableList.register(new ResourceLocation(Reference.MODID, "entities/toad/brigade/mail"));
		//OTHER
	public static final ResourceLocation CHINCHO = LootTableList.register(new ResourceLocation(Reference.MODID, "entities/chincho"));
}
