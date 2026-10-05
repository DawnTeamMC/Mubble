package fr.hugman.mubble.arcade.client.compat.controlify.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.controlify.contextual.ContextualState;
import dev.isxander.controlify.contextual.GuideRule;
import dev.isxander.controlify.contextual.RuleEngine;
import dev.isxander.controlify.gui.guide.GuideInstanceImpl;
import fr.hugman.mubble.arcade.client.compat.controlify.ArcadeGuide;
import java.util.List;
import java.util.function.Predicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** The button guide follows the arcade layout while it is in use, see {@link ArcadeGuide}. */
@Mixin(value = GuideInstanceImpl.class, remap = false)
public abstract class GuideInstanceImplMixin {
    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Ldev/isxander/controlify/contextual/RuleEngine;evaluate(Ldev/isxander/controlify/contextual/ContextualState;Ljava/util/function/Predicate;Ljava/lang/String;)Ljava/util/List;"))
    private List<GuideRule> mubble$followTheArcadeLayout(RuleEngine<GuideRule.Key, GuideRule> engine, ContextualState state, Predicate<GuideRule> filter, String label, Operation<List<GuideRule>> original) {
        return ArcadeGuide.arrange(original.call(engine, state, filter, label));
    }
}
