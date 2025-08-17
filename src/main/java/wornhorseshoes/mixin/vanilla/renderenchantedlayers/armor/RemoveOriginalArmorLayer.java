package wornhorseshoes.mixin.vanilla.renderenchantedlayers.armor;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.passive.EntityHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityHorse.class)
public abstract class RemoveOriginalArmorLayer {
    @ModifyExpressionValue(
            method = "setHorseTexturePaths",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z")
    )
    private boolean whs_dontRenderArmorNormally(boolean original){
        return true;
    }

    @ModifyArg(
            method = "setHorseTexturePaths",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/HorseArmorType;getByOrdinal(I)Lnet/minecraft/entity/passive/HorseArmorType;")
    )
    private int whs_dontRenderArmorNormally(int ordinal){
        return 0;
    }
}
