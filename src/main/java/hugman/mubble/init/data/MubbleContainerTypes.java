package hugman.mubble.init.data;

import java.util.ArrayList;
import java.util.List;

import hugman.mubble.Mubble;
import hugman.mubble.objects.container.TimeswapTableContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

public class MubbleContainerTypes
{
	/* All Content Bag */
    public static final List<ContainerType<?>> CONTAINER_TYPES = new ArrayList<ContainerType<?>>();
    
    public static final ContainerType<TimeswapTableContainer> TIMESWAP_TABLE = register("timeswap_table", TimeswapTableContainer::new);
    
    private static <T extends Container> ContainerType<T> register(String name, ContainerType.IFactory<T> factory)
    {
    	ContainerType<T> fType = new ContainerType<>(factory);
    	fType.setRegistryName(Mubble.MOD_ID, name);
    	CONTAINER_TYPES.add(fType);
    	return fType;
    }
}