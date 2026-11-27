package wornhorseshoes.mixin;

import net.minecraft.world.entity.animal.horse.Horse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.util.HorseGear;

/** Horses: armor toughness from [horse_armor] armorStats, after vanilla sets the armor (and its armor bonus). */
@Mixin(Horse.class)
public abstract class HorseMixin {
    @Inject(method = "updateContainerEquipment", at = @At("TAIL"))
    private void whs$toughness(CallbackInfo ci) {
        Horse self = (Horse) (Object) this;
        if (!self.level().isClientSide) HorseGear.applyToughness(self);
    }
}
