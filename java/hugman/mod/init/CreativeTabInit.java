package hugman.mod.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/** 
 * Init class - used to initialize creative tabs.
 */
public class CreativeTabInit 
{
    public static final CreativeTabs SUPER_MARIO = new CreativeTabs("super_mario")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(ItemInit.SUPER_MUSHROOM);
    	}
    };
    public static final CreativeTabs TETRIS = new CreativeTabs("tetris")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(BlockInit.LIGHT_BLUE_TETRIS_BLOCK);
    	}
    };
    public static final CreativeTabs CASTLEVANIA = new CreativeTabs("castlevania")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(BlockInit.VAMPIRE_STONE);
    	}
    };
    public static final CreativeTabs SONIC = new CreativeTabs("sonic")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(BlockInit.GREEN_HILL_GRASS_BLOCK);
    	}
    };
    public static final CreativeTabs UNDERTALE_DELTARUNE = new CreativeTabs("undertale_deltarune")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(ItemInit.ANNOYING_DOG);
    	}
    };
    public static final CreativeTabs CELESTE = new CreativeTabs("celeste")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(BlockInit.CYAN_BRICKS);
    	}
    };
    public static final CreativeTabs PUYO_PUYO = new CreativeTabs("puyo_puyo")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(BlockInit.RED_PUYO);
    	}
    };
    public static final CreativeTabs MUBBLE_MISC = new CreativeTabs("mubble_misc")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(CostumeInit.TOP_HAT);
    	}
    };
    public static final CreativeTabs YOUTUBE = new CreativeTabs("youtube")
    {
    	@Override
    	public ItemStack createIcon()
    	{
    		return new ItemStack(BlockInit.REWIND_BLOCK);
    	}
    };
}