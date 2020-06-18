package hugman.mubble.init;

import hugman.mubble.Mubble;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class MubbleTabs {
	public static final ItemGroup COSTUMES = FabricItemGroupBuilder.build(new Identifier(Mubble.MOD_ID, "costumes"),
			() -> new ItemStack(MubbleCostumes.CAPPY)
	);

	public static final ItemGroup INSTRUMENTS = FabricItemGroupBuilder.build(new Identifier(Mubble.MOD_ID, "instruments"),
			() -> new ItemStack(MubbleItems.JINGLE_BELLS)
	);
}