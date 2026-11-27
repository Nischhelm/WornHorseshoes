package wornhorseshoes.mixin;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wornhorseshoes.util.UndeadHorses;

/** Zombie horses that spawned with a zombie rider despawn like monsters (animals normally never do). */
@Mixin(Animal.class)
public abstract class AnimalMixin {
    @Inject(method = "removeWhenFarAway", at = @At("RETURN"), cancellable = true)
    private void whs$riderHorseDespawns(double distance, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof AbstractHorse horse && UndeadHorses.canDespawn(horse)) cir.setReturnValue(true);
    }
}
