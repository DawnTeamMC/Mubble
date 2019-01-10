package hugman.mod.entity;

import javax.annotation.Nullable;

import hugman.mod.objects.blocks.BlockFlying;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFlyingBlock extends Entity
{
    private IBlockState flyTile;
    public int flyTime;
    public boolean shouldDropItem = true;
    private boolean dontSetBlock;
    public NBTTagCompound tileEntityData;
    protected static final DataParameter<BlockPos> ORIGIN = EntityDataManager.<BlockPos>createKey(EntityFlyingBlock.class, DataSerializers.BLOCK_POS);
    
    public EntityFlyingBlock(World worldIn)
    {
        super(worldIn);
    }

    public EntityFlyingBlock(World worldIn, double x, double y, double z, IBlockState flyingBlockState)
    {
        super(worldIn);
        this.flyTile = flyingBlockState;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.setPosition(x, y + (double)((1.0F - this.height) / 2.0F), z);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.setOrigin(new BlockPos(this));
    }
    
    @Override
    public boolean canBeAttackedWithItem()
    {
        return true;
    }
    
    public void setOrigin(BlockPos p_184530_1_)
    {
        this.dataManager.set(ORIGIN, p_184530_1_);
    }

    @SideOnly(Side.CLIENT)
    public BlockPos getOrigin()
    {
        return (BlockPos)this.dataManager.get(ORIGIN);
    }
    
    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    protected void entityInit()
    {
        this.dataManager.register(ORIGIN, BlockPos.ORIGIN);
    }
    
    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }
    
    @Override
    public void onUpdate()
    {
        Block block = this.flyTile.getBlock();

        if (this.flyTile.getMaterial() == Material.AIR)
        {
            this.setDead();
        }
        else
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;

            if (this.flyTime++ == 0)
            {
                BlockPos blockpos = new BlockPos(this);

                if (this.world.getBlockState(blockpos).getBlock() == block)
                {
                    this.world.setBlockToAir(blockpos);
                }
                else if (!this.world.isRemote)
                {
                    this.setDead();
                    return;
                }
            }

            if (!this.hasNoGravity())
            {
                this.motionY -= 0.03999999910593033D;
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

            if (!this.world.isRemote)
            {
                BlockPos blockpos1 = new BlockPos(this);
                boolean flag = this.flyTile.getBlock() == Blocks.CONCRETE_POWDER;
                boolean flag1 = flag && this.world.getBlockState(blockpos1).getMaterial() == Material.WATER;
                double d0 = this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ;

                if (flag && d0 > 1.0D)
                {
                    RayTraceResult raytraceresult = this.world.rayTraceBlocks(new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ), new Vec3d(this.posX, this.posY, this.posZ), true);

                    if (raytraceresult != null && this.world.getBlockState(raytraceresult.getBlockPos()).getMaterial() == Material.WATER)
                    {
                        blockpos1 = raytraceresult.getBlockPos();
                        flag1 = true;
                    }
                }

                if (!this.onGround && !flag1)
                {
                    if (this.flyTime > 100 && !this.world.isRemote && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.flyTime > 600)
                    {
                        if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops"))
                        {
                            this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.flyTile)), 0.0F);
                        }

                        this.setDead();
                    }
                }
                else
                {
                    IBlockState iblockstate = this.world.getBlockState(blockpos1);

                    if (this.world.isAirBlock(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ)))
                    if (!flag1 && BlockFlying.canFlyThrough(this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ))))
                    {
                        this.onGround = false;
                        return;
                    }

                    this.motionX *= 0.699999988079071D;
                    this.motionZ *= 0.699999988079071D;
                    this.motionY *= 0.5D;

                    if (iblockstate.getBlock() != Blocks.PISTON_EXTENSION)
                    {
                        this.setDead();

                        if (!this.dontSetBlock)
                        {
                            if (this.world.mayPlace(block, blockpos1, true, EnumFacing.UP, (Entity)null) && (flag1 || !BlockFlying.canFlyThrough(this.world.getBlockState(blockpos1.up()))) && this.world.setBlockState(blockpos1, this.flyTile, 3))
                            {
                                if (block instanceof BlockFlying)
                                {
                                    ((BlockFlying)block).onEndFlying(this.world, blockpos1, this.flyTile, iblockstate);
                                }

                                if (this.tileEntityData != null && block.hasTileEntity(this.flyTile))
                                {
                                    TileEntity tileentity = this.world.getTileEntity(blockpos1);

                                    if (tileentity != null)
                                    {
                                        NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());

                                        for (String s : this.tileEntityData.getKeySet())
                                        {
                                            NBTBase nbtbase = this.tileEntityData.getTag(s);

                                            if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s))
                                            {
                                                nbttagcompound.setTag(s, nbtbase.copy());
                                            }
                                        }

                                        tileentity.readFromNBT(nbttagcompound);
                                        tileentity.markDirty();
                                    }
                                }
                            }
                            else if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops"))
                            {
                                this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.flyTile)), 0.0F);
                            }
                        }
                        else if (block instanceof BlockFlying)
                        {
                            ((BlockFlying)block).onBroken(this.world, blockpos1);
                        }
                    }
                }
            }

            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= 0.9800000190734863D;
        }
    }

    public static void registerFixesFlyingBlock(DataFixer fixer)
    {
    }
    
    @Override
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        Block block = this.flyTile != null ? this.flyTile.getBlock() : Blocks.AIR;
        ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(block);
        compound.setString("Block", resourcelocation == null ? "" : resourcelocation.toString());
        compound.setByte("Data", (byte)block.getMetaFromState(this.flyTile));
        compound.setInteger("Time", this.flyTime);
        compound.setBoolean("DropItem", this.shouldDropItem);

        if (this.tileEntityData != null)
        {
            compound.setTag("TileEntityData", this.tileEntityData);
        }
    }
    
    @Override
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        int i = compound.getByte("Data") & 255;

        if (compound.hasKey("Block", 8))
        {
            this.flyTile = Block.getBlockFromName(compound.getString("Block")).getStateFromMeta(i);
        }
        else if (compound.hasKey("TileID", 99))
        {
            this.flyTile = Block.getBlockById(compound.getInteger("TileID")).getStateFromMeta(i);
        }
        else
        {
            this.flyTile = Block.getBlockById(compound.getByte("Tile") & 255).getStateFromMeta(i);
        }

        this.flyTime = compound.getInteger("Time");
        Block block = this.flyTile.getBlock();

        if (compound.hasKey("DropItem", 99))
        {
            this.shouldDropItem = compound.getBoolean("DropItem");
        }

        if (compound.hasKey("TileEntityData", 10))
        {
            this.tileEntityData = compound.getCompoundTag("TileEntityData");
        }

        if (block == null || block.getDefaultState().getMaterial() == Material.AIR)
        {
            this.flyTile = Blocks.SAND.getDefaultState();
        }
    }

    @Override
    public void addEntityCrashInfo(CrashReportCategory category)
    {
        super.addEntityCrashInfo(category);

        if (this.flyTile != null)
        {
            Block block = this.flyTile.getBlock();
            category.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(block)));
            category.addCrashSection("Immitating block data", Integer.valueOf(block.getMetaFromState(this.flyTile)));
        }
    }

    @SideOnly(Side.CLIENT)
    public World getWorldObj()
    {
        return this.world;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire()
    {
        return false;
    }

    @Nullable
    public IBlockState getBlock()
    {
        return this.flyTile;
    }

    @Override
    public boolean ignoreItemEntityData()
    {
        return true;
    }
}
