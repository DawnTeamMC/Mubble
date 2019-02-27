package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.objects.item.ItemSimple;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class MubbleItems
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item YELLOW_COIN = new ItemSimple("yellow_coin");
	public static final Item RED_COIN = new ItemSimple("red_coin");
	public static final Item BLUE_COIN = new ItemSimple("blue_coin");
	
	public static final Item SUPER_MUSHROOM = new ItemSimple("super_mushroom");
	public static final Item CAPE_FEATHER = new ItemSimple("cape_feather");
	public static final Item SUPER_CROWN = new ItemSimple("super_crown");
	
    public static void registerBlocks(RegistryEvent.Register<Item> event)
    {
    	event.getRegistry().registerAll(MubbleItems.ITEMS.toArray(new Item[0]));
    }
}
