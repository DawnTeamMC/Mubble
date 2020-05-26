package hugman.mubble.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@Mixin(Entity.class)
public interface EntityAccessor
{
	@Accessor("lastNetherPortalPosition")
	BlockPos getLastNetherPortalPosition();
	
	@Accessor("lastNetherPortalPosition")
	void setLastNetherPortalPosition(BlockPos pos);
	
	@Accessor("lastNetherPortalDirectionVector")
	void setLastNetherPortalDirectionVector(Vec3d vec);
	
	@Accessor("lastNetherPortalDirection")
	void setLastNetherPortalDirection(Direction dir);
	
	@Accessor("inNetherPortal")
	void setInNetherPortal(boolean inNetherPortal);
}
