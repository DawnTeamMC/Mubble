package hugman.mubble.objects.world.feature.tree.template;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;

public class LargeTreeFeature extends AbstractTreeFeature<BranchedTreeFeatureConfig>
{
    public LargeTreeFeature(Function<Dynamic<?>, ? extends BranchedTreeFeatureConfig> configFactory, boolean notify, Block log, Block leaves, Block sapling)
    {
        super(configFactory);
    }

    @Override
    public boolean generate(ModifiableTestableWorld worldIn, Random rand, BlockPos position, Set<BlockPos> changedBlocks, Set<BlockPos> leavesPositions, BlockBox box, BranchedTreeFeatureConfig config)
    {
        int i = rand.nextInt(3) + rand.nextInt(2) + 6;
        int j = position.getX();
        int k = position.getY();
        int l = position.getZ();
        if (k >= 1 && k + i + 1 < 256) {
          BlockPos blockpos = position.down();
          if (!isNaturalDirtOrGrass(worldIn, blockpos)) {
             return false;
          } else if (!this.func_214615_a(worldIn, position, i)) {
             return false;
          } else {
             this.setToDirt(worldIn, blockpos);
             this.setToDirt(worldIn, blockpos.east());
             this.setToDirt(worldIn, blockpos.south());
             this.setToDirt(worldIn, blockpos.south().east());
             Direction direction = Direction.Type.HORIZONTAL.random(rand);
             int i1 = i - rand.nextInt(4);
             int j1 = 2 - rand.nextInt(3);
             int k1 = j;
             int l1 = l;
             int i2 = k + i - 1;

             for(int j2 = 0; j2 < i; ++j2) {
                if (j2 >= i1 && j1 > 0) {
                    k1 += direction.getOffsetX();
                    l1 += direction.getOffsetZ();
                    --j1;
                }

                int k2 = k + j2;
                BlockPos blockpos1 = new BlockPos(k1, k2, l1);
                if (isAirOrLeaves(worldIn, blockpos1)) {
                    this.func_214616_a(changedBlocks, worldIn, rand, blockpos1, box, config);
                    this.func_214616_a(changedBlocks, worldIn, rand, blockpos1.east(), box, config);
                    this.func_214616_a(changedBlocks, worldIn, rand, blockpos1.south(), box, config);
                    this.func_214616_a(changedBlocks, worldIn, rand, blockpos1.east().south(), box, config);
                }
             }

             for(int j3 = -2; j3 <= 0; ++j3) {
                for(int i4 = -2; i4 <= 0; ++i4) {
                    int l4 = -1;
                    this.func_214617_a(worldIn, rand, k1 + j3, i2 + l4, l1 + i4, box, changedBlocks, config);
                    this.func_214617_a(worldIn, rand, 1 + k1 - j3, i2 + l4, l1 + i4, box, changedBlocks, config);
                    this.func_214617_a(worldIn, rand, k1 + j3, i2 + l4, 1 + l1 - i4, box, changedBlocks, config);
                    this.func_214617_a(worldIn, rand, 1 + k1 - j3, i2 + l4, 1 + l1 - i4, box, changedBlocks, config);
                    if ((j3 > -2 || i4 > -1) && (j3 != -1 || i4 != -2)) {
                      l4 = 1;
                      this.func_214617_a(worldIn, rand, k1 + j3, i2 + l4, l1 + i4, box, changedBlocks, config);
                      this.func_214617_a(worldIn, rand, 1 + k1 - j3, i2 + l4, l1 + i4, box, changedBlocks, config);
                      this.func_214617_a(worldIn, rand, k1 + j3, i2 + l4, 1 + l1 - i4, box, changedBlocks, config);
                      this.func_214617_a(worldIn, rand, 1 + k1 - j3, i2 + l4, 1 + l1 - i4, box, changedBlocks, config);
                    }
                }
             }

             if (rand.nextBoolean()) {
                this.func_214617_a(worldIn, rand, k1, i2 + 2, l1, box, changedBlocks, config);
                this.func_214617_a(worldIn, rand, k1 + 1, i2 + 2, l1, box, changedBlocks, config);
                this.func_214617_a(worldIn, rand, k1 + 1, i2 + 2, l1 + 1, box, changedBlocks, config);
                this.func_214617_a(worldIn, rand, k1, i2 + 2, l1 + 1, box, changedBlocks, config);
             }

             for(int k3 = -3; k3 <= 4; ++k3) {
                for(int j4 = -3; j4 <= 4; ++j4) {
                    if ((k3 != -3 || j4 != -3) && (k3 != -3 || j4 != 4) && (k3 != 4 || j4 != -3) && (k3 != 4 || j4 != 4) && (Math.abs(k3) < 3 || Math.abs(j4) < 3)) {
                      this.func_214617_a(worldIn, rand, k1 + k3, i2, l1 + j4, box, changedBlocks, config);
                    }
                }
             }

             for(int l3 = -1; l3 <= 2; ++l3) {
                for(int k4 = -1; k4 <= 2; ++k4) {
                    if ((l3 < 0 || l3 > 1 || k4 < 0 || k4 > 1) && rand.nextInt(3) <= 0) {
                      int i5 = rand.nextInt(3) + 2;

                      for(int l2 = 0; l2 < i5; ++l2) {
                         this.func_214616_a(changedBlocks, worldIn, rand, new BlockPos(j + l3, i2 - l2 - 1, l + k4), box, config);
                      }

                      for(int j5 = -1; j5 <= 1; ++j5) {
                         for(int i3 = -1; i3 <= 1; ++i3) {
                            this.func_214617_a(worldIn, rand, k1 + l3 + j5, i2, l1 + k4 + i3, box, changedBlocks, config);
                         }
                      }

                      for(int k5 = -2; k5 <= 2; ++k5) {
                         for(int l5 = -2; l5 <= 2; ++l5) {
                            if (Math.abs(k5) != 2 || Math.abs(l5) != 2) {
                                this.func_214617_a(worldIn, rand, k1 + l3 + k5, i2 - 1, l1 + k4 + l5, box, changedBlocks, config);
                            }
                         }
                      }
                    }
                }
             }

             return true;
          }
        } else {
          return false;
        }
    }

    private boolean func_214615_a(ModifiableTestableWorld p_214615_1_, BlockPos p_214615_2_, int p_214615_3_) {
        int i = p_214615_2_.getX();
        int j = p_214615_2_.getY();
        int k = p_214615_2_.getZ();
        BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();

        for(int l = 0; l <= p_214615_3_ + 1; ++l) {
          int i1 = 1;
          if (l == 0) {
             i1 = 0;
          }

          if (l >= p_214615_3_ - 1) {
             i1 = 2;
          }

          for(int j1 = -i1; j1 <= i1; ++j1) {
             for(int k1 = -i1; k1 <= i1; ++k1) {
                if (!canTreeReplace(p_214615_1_, blockpos$mutableblockpos.set(i + j1, j + l, k + k1))) {
                    return false;
                }
             }
          }
        }

        return true;
    }

    private void func_214616_a(Set<BlockPos> p_214616_1_, ModifiableTestableWorld world, Random random, BlockPos pos, BlockBox box, BranchedTreeFeatureConfig config) {
        if (canTreeReplace(world, pos)) {
          this.setLogBlockState(world, random, pos, p_214616_1_, box, config);
        }

    }

    private void func_214617_a(ModifiableTestableWorld world, Random random, int p_214617_2_, int p_214617_3_, int p_214617_4_, BlockBox box, Set<BlockPos> p_214617_6_, BranchedTreeFeatureConfig config) {
        BlockPos pos = new BlockPos(p_214617_2_, p_214617_3_, p_214617_4_);
        if (isAir(world, pos)) {
          this.setLeavesBlockState(world, random, pos, p_214617_6_, box, config);
        }

    }
}