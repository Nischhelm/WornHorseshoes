package wornhorseshoes.mixin.vanilla.horseshoeslot.increaseinvsize;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.passive.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorse_IncreaseInventorySize {
    @ModifyReturnValue(
            method = "getInventorySize",
            at = @At("RETURN")
    )
    private int whs_addHorseShoeSlot(int original){
        return original + 1;
    }
}
