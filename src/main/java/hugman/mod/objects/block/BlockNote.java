package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.Reference;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleSounds;
import hugman.mod.init.MubbleTabs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Particles;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockNote extends Block
{    
    public BlockNote(String name)
    {
        super(Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(1.4F, 2.0F));
        setRegistryName(Reference.MOD_ID, name);
        Item.Properties blocks = new Item.Properties().group(MubbleTabs.MUBBLE_BLOCKS);
        
		MubbleBlocks.BLOCKS.add(this);
		MubbleItems.ITEMS.add(new ItemBlock(this, blocks).setRegistryName(this.getRegistryName()));
    }
    
    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
    	entityIn.fall(fallDistance, 0.0F);
    }
    
    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
    	if (Math.abs(entityIn.motionY) < 0.45D) entityIn.motionY = 0.5D;
    }
    
    @Override
    public void onLanded(IBlockReader blockreader, Entity entityIn)
    {
    	World worldIn = entityIn.getEntityWorld();
    	launch(worldIn, entityIn);
    }
    
    public void launch(World worldIn, Entity entityIn)
    {
        if(entityIn instanceof EntityLivingBase)
        {
        	BlockPos pos = new BlockPos(entityIn).down();
            final double x = pos.getX() + 0.5D;
            final double y = pos.getY() + 0.5D;
            final double z = pos.getZ() + 0.5D;
            Random rand = new Random();
        	if(!entityIn.isSneaking())
    		{
    	        worldIn.playSound((EntityPlayer)null, x, y, z, MubbleSounds.BLOCK_NOTE_BLOCK_JUMP_HIGH, SoundCategory.BLOCKS, 1f, 1f);
    	        for (int i = 0; i < rand.nextInt(5) + 1; i++)
    	        {
    	        	worldIn.spawnParticle(Particles.NOTE, x + (rand.nextInt(7) - 3) / 10D, y + 0.6D, z + (rand.nextInt(7) - 3) / 10D, (rand.nextInt(7) - 3) / 10D, 0.2D, (rand.nextInt(7) - 3) / 10D);
            	}
    			if(this == MubbleBlocks.NOTE_BLOCK) entityIn.motionY = 0.9D;
    			if(this == MubbleBlocks.SUPER_NOTE_BLOCK) entityIn.motionY = 1.5D;
    		}
    		else if(entityIn.isSneaking())
    		{
    			worldIn.playSound((EntityPlayer)null, x, y, z, MubbleSounds.BLOCK_NOTE_BLOCK_JUMP_LOW, SoundCategory.BLOCKS, 1f, 1f);
    	        for (int i = 0; i < rand.nextInt(1) + 1; i++)
    	        {
    	        	worldIn.spawnParticle(Particles.NOTE, x, y + 0.6D, z, (rand.nextInt(7) - 3) / 10D, 0.2D, (rand.nextInt(7) - 3) / 10D);
            	}
    	        entityIn.motionY = 0.5D;
    		}
    		else
    		{
    			entityIn.motionY = 0D;
    		}
        }
    }
}
