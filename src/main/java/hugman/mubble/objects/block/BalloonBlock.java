package hugman.mubble.objects.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class BalloonBlock extends FlyingBlock
{
    public BalloonBlock(DyeColor color)
    {
        super(FabricBlockSettings.of(Material.WOOL, color).hardness(0F).sounds(BlockSoundGroup.WOOL).nonOpaque().build());
    }
    
    @Environment(EnvType.CLIENT)
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView worldIn, BlockPos pos)
    {
       return 1.0F;
    }
    
    @Override
    public boolean isTranslucent(BlockState state, BlockView reader, BlockPos pos)
    {
    	return true;
	}

    public boolean causesSuffocation(BlockState state, BlockView worldIn, BlockPos pos)
    {
       return false;
    }

    public boolean isNormalCube(BlockState state, BlockView worldIn, BlockPos pos)
    {
       return false;
    }

    public boolean canEntitySpawn(BlockState state, BlockView worldIn, BlockPos pos, EntityType<?> type)
    {
       return false;
    }
}
