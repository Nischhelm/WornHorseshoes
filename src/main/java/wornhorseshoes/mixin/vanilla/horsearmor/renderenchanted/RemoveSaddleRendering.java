package wornhorseshoes.mixin.vanilla.horsearmor.renderenchanted;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.model.ModelHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModelHorse.class)
public abstract class RemoveSaddleRendering {
    @ModifyExpressionValue(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AbstractHorse;isHorseSaddled()Z")
    )
    private boolean whs_dontRenderOriginalSaddle(boolean original){
        return false;
    }
}
