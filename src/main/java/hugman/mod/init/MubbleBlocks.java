package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.objects.block.BlockEmpty;
import hugman.mod.objects.block.BlockQuestion;
import hugman.mod.objects.block.BlockSimple;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class MubbleBlocks
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block VANADIUM_BLOCK = new BlockSimple("vanadium_block", Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(5.0F, 6.0F));
    public static final Block VANADIUM_ORE = new BlockSimple("vanadium_ore", Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(3.0F));
    
    public static final Block QUESTION_BLOCK = new BlockQuestion();
    public static final Block EMPTY_BLOCK = new BlockEmpty();
}
