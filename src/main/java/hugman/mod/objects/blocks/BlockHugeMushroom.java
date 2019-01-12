package hugman.mod.objects.blocks;

import java.util.Random;

import hugman.mod.init.BlockInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHugeMushroom extends BlockBase implements IHasModel
{   
    public static final PropertyEnum<BlockHugeMushroom.EnumType> VARIANT = PropertyEnum.<BlockHugeMushroom.EnumType>create("variant", BlockHugeMushroom.EnumType.class);
    
    /** 
     * Open class - can be initialized for multiple items with variables.
     */
	public BlockHugeMushroom(String color, int light)
	{
		super(color + "_mushroom_block", Material.WOOD, 0.2f, 1f, SoundType.WOOD, light);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockHugeMushroom.EnumType.ALL_OUTSIDE));
	}
	
	@Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        switch ((BlockHugeMushroom.EnumType)state.getValue(VARIANT))
        {
            case ALL_STEM:
                return MapColor.CLOTH;
            case ALL_INSIDE:
                return MapColor.SAND;
            case STEM:
                return MapColor.SAND;
            default:
                return super.getMapColor(state, worldIn, pos);
        }
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
    	if(this == BlockInit.BLUE_MUSHROOM_BLOCK) return Item.getItemFromBlock(BlockInit.BLUE_MUSHROOM);
    	else if(this == BlockInit.GREEN_MUSHROOM_BLOCK) return Item.getItemFromBlock(BlockInit.GREEN_MUSHROOM);
    	else if(this == BlockInit.YELLOW_MUSHROOM_BLOCK) return Item.getItemFromBlock(BlockInit.YELLOW_MUSHROOM);
    	else if(this == BlockInit.PURPLE_MUSHROOM_BLOCK) return Item.getItemFromBlock(BlockInit.PURPLE_MUSHROOM);
		return null;
    }
    
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState();
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, BlockHugeMushroom.EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((BlockHugeMushroom.EnumType)state.getValue(VARIANT)).getMetadata();
    }
	
    @Override
    public int quantityDropped(Random random)
    {
        return Math.max(0, random.nextInt(10) - 7);
    }
    
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        switch (rot)
        {
            case CLOCKWISE_180:

                switch ((BlockHugeMushroom.EnumType)state.getValue(VARIANT))
                {
                    case STEM:
                        break;
                    case NORTH_WEST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST);
                    case NORTH:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH);
                    case NORTH_EAST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST);
                    case WEST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.EAST);
                    case EAST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.WEST);
                    case SOUTH_WEST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST);
                    case SOUTH:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH);
                    case SOUTH_EAST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST);
                    default:
                        return state;
                }

            case COUNTERCLOCKWISE_90:

                switch ((BlockHugeMushroom.EnumType)state.getValue(VARIANT))
                {
                    case STEM:
                        break;
                    case NORTH_WEST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST);
                    case NORTH:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.WEST);
                    case NORTH_EAST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST);
                    case WEST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH);
                    case EAST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH);
                    case SOUTH_WEST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST);
                    case SOUTH:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.EAST);
                    case SOUTH_EAST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST);
                    default:
                        return state;
                }

            case CLOCKWISE_90:

                switch ((BlockHugeMushroom.EnumType)state.getValue(VARIANT))
                {
                    case STEM:
                        break;
                    case NORTH_WEST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST);
                    case NORTH:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.EAST);
                    case NORTH_EAST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST);
                    case WEST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH);
                    case EAST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH);
                    case SOUTH_WEST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST);
                    case SOUTH:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.WEST);
                    case SOUTH_EAST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST);
                    default:
                        return state;
                }

            default:
                return state;
        }
    }
    
    @Override @SuppressWarnings("incomplete-switch")
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        BlockHugeMushroom.EnumType blockhugemushroom$enumtype = (BlockHugeMushroom.EnumType)state.getValue(VARIANT);

        switch (mirrorIn)
        {
            case LEFT_RIGHT:

                switch (blockhugemushroom$enumtype)
                {
                    case NORTH_WEST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST);
                    case NORTH:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH);
                    case NORTH_EAST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST);
                    case WEST:
                    case EAST:
                    default:
                        return super.withMirror(state, mirrorIn);
                    case SOUTH_WEST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST);
                    case SOUTH:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH);
                    case SOUTH_EAST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST);
                }

            case FRONT_BACK:

                switch (blockhugemushroom$enumtype)
                {
                    case NORTH_WEST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST);
                    case NORTH:
                    case SOUTH:
                    default:
                        break;
                    case NORTH_EAST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST);
                    case WEST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.EAST);
                    case EAST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.WEST);
                    case SOUTH_WEST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST);
                    case SOUTH_EAST:
                        return state.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST);
                }
        }

        return super.withMirror(state, mirrorIn);
    }
    
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {VARIANT});
    }
    
    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        IBlockState state = world.getBlockState(pos);
        for (IProperty prop : (java.util.Set<IProperty<?>>)state.getProperties().keySet())
        {
            if (prop.getName().equals("variant"))
            {
                world.setBlockState(pos, state.cycleProperty(prop));
                return true;
            }
        }
        return false;
    }
    
    public static enum EnumType implements IStringSerializable
    {
        NORTH_WEST(1, "north_west"),
        NORTH(2, "north"),
        NORTH_EAST(3, "north_east"),
        WEST(4, "west"),
        CENTER(5, "center"),
        EAST(6, "east"),
        SOUTH_WEST(7, "south_west"),
        SOUTH(8, "south"),
        SOUTH_EAST(9, "south_east"),
        STEM(10, "stem"),
        ALL_INSIDE(0, "all_inside"),
        ALL_OUTSIDE(14, "all_outside"),
        ALL_STEM(15, "all_stem");

        private static final BlockHugeMushroom.EnumType[] META_LOOKUP = new BlockHugeMushroom.EnumType[16];
        private final int meta;
        private final String name;

        private EnumType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.name;
        }

        public static BlockHugeMushroom.EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            BlockHugeMushroom.EnumType blockhugemushroom$enumtype = META_LOOKUP[meta];
            return blockhugemushroom$enumtype == null ? META_LOOKUP[0] : blockhugemushroom$enumtype;
        }

        public String getName()
        {
            return this.name;
        }

        static
        {
            for (BlockHugeMushroom.EnumType blockhugemushroom$enumtype : values())
            {
                META_LOOKUP[blockhugemushroom$enumtype.getMetadata()] = blockhugemushroom$enumtype;
            }
        }
    }
}
