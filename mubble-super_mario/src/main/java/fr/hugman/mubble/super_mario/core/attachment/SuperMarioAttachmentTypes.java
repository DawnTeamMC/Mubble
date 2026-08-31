package fr.hugman.mubble.super_mario.core.attachment;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.world.entity.freeze.FreezeState;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class SuperMarioAttachmentTypes {
    /**
     * The block of ice an entity is trapped in, absent as long as it is not frozen.
     * <p>
     * It is synced to every client and not only to the frozen entity itself: whoever is looking at it
     * has to see the ice cube around it, and the frozen entity is very much not the only one looking.
     */
    public static final AttachmentType<FreezeState> FREEZE = AttachmentRegistry.<FreezeState>builder()
            .persistent(FreezeState.CODEC)
            .syncWith(FreezeState.STREAM_CODEC, AttachmentSyncPredicate.all())
            .buildAndRegister(SuperMario.id("freeze"));
}
