package wornhorseshoes.mixin.modcompat.potioncore;

import com.tmtravlr.potioncore.PotionCoreEventHandler;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.util.ISkipsPotionCoreHandler;

@Mixin(PotionCoreEventHandler.class)
public class PotionCoreEventHandlerMixin {
    @Inject(
            method = "onLivingJump",
            at = @At("HEAD"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private static void whs_skipPotionCoreHandlerConditionally(LivingEvent.LivingJumpEvent event, CallbackInfo ci){
        if(((ISkipsPotionCoreHandler) event).whs$getSkipsPotionCore()) ci.cancel();
    }
}
