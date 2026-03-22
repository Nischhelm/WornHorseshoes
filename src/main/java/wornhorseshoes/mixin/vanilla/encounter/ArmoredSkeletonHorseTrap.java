package wornhorseshoes.mixin.vanilla.encounter;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.ai.EntityAISkeletonRiders;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.world.DifficultyInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wornhorseshoes.util.HorseEquipUtil;

@Mixin(EntityAISkeletonRiders.class)
public abstract class ArmoredSkeletonHorseTrap {
    @Shadow @Final private EntitySkeletonHorse horse;

    @Inject(
            method = "createHorse",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z")
    )
    private void whs_addArmor_additionalHorses(DifficultyInstance difficulty, CallbackInfoReturnable<AbstractHorse> cir, @Local EntitySkeletonHorse horse) {
        HorseEquipUtil.equipHorse(horse, difficulty.getClampedAdditionalDifficulty());
    }

    @Inject(
            method = "updateTask",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addWeatherEffect(Lnet/minecraft/entity/Entity;)Z", ordinal = 0)
    )
    private void whs_addArmor_thisHorse(CallbackInfo ci, @Local DifficultyInstance difficulty) {
        HorseEquipUtil.equipHorse(this.horse, difficulty.getClampedAdditionalDifficulty());
    }
}
