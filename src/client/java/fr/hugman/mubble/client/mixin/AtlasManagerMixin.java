package fr.hugman.mubble.client.mixin;

import fr.hugman.mubble.Mubble;
import net.minecraft.client.texture.AtlasManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(AtlasManager.class)
public class AtlasManagerMixin {
    @Mutable
    @Shadow private static List<AtlasManager.Metadata> ATLAS_METADATA;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void appendCustomAtlasMetadata(CallbackInfo ci) {
        List<AtlasManager.Metadata> modifiable = new ArrayList<>(ATLAS_METADATA);
        modifiable.add(new AtlasManager.Metadata(
			Mubble.id("textures/atlas/power_ups.png"),
			Mubble.id("power_ups"),
            false
        ));
        ATLAS_METADATA = List.copyOf(modifiable);
    }
}
