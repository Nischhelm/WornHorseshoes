package wornhorseshoes.mixin.vanilla.enchantment;

import net.minecraft.entity.passive.AbstractHorse;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.util.ISkipsPotionCoreHandler;

@Mixin(AbstractHorse.class)
public abstract class FireLivingJump {

    @Inject(
            method = "travel",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/passive/AbstractHorse;jumpPower:F"
            ),
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/init/SoundEvents;ENTITY_HORSE_JUMP:Lnet/minecraft/util/SoundEvent;"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AbstractHorse;getAIMoveSpeed()F")
            )
    )
    public void whs_fireLivingJumpEvent(float strafe, float vertical, float forward, CallbackInfo ci){
        LivingEvent.LivingJumpEvent event = new LivingEvent.LivingJumpEvent((AbstractHorse)(Object)this);
        ((ISkipsPotionCoreHandler) event).whs$setSkipsPotionCore();
        MinecraftForge.EVENT_BUS.post(event);
    }
}
