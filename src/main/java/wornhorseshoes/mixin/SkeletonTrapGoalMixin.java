package wornhorseshoes.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.SkeletonTrapGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wornhorseshoes.config.WHSConfig;
import wornhorseshoes.util.UndeadHorses;

/** Skeleton traps: the trap horse and the three extra horses it calls can wear random armor and horseshoes. */
@Mixin(SkeletonTrapGoal.class)
public abstract class SkeletonTrapGoalMixin {

    @Shadow @Final private SkeletonHorse horse;

    @Inject(method = "tick", at = @At("HEAD"))
    private void whs$equipTrapHorse(CallbackInfo ci) {
        if (!WHSConfig.SKELETON_TRAPS_ARMORED.get() || !(this.horse.level() instanceof ServerLevel level)) return;
        DifficultyInstance difficulty = level.getCurrentDifficultyAt(this.horse.blockPosition());
        UndeadHorses.equip(this.horse, difficulty.getSpecialMultiplier());
    }

    @Inject(method = "createHorse", at = @At("RETURN"))
    private void whs$equipExtraHorse(DifficultyInstance difficulty, CallbackInfoReturnable<AbstractHorse> cir) {
        if (WHSConfig.SKELETON_TRAPS_ARMORED.get() && cir.getReturnValue() != null) UndeadHorses.equip(cir.getReturnValue(), difficulty.getSpecialMultiplier());
    }
}
