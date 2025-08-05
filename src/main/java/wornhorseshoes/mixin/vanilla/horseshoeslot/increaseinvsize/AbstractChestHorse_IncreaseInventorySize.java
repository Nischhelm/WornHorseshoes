package wornhorseshoes.mixin.vanilla.horseshoeslot.increaseinvsize;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.passive.AbstractChestHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractChestHorse.class)
public abstract class AbstractChestHorse_IncreaseInventorySize {
    @Shadow public abstract boolean hasChest();

    @ModifyReturnValue(
            method = "getInventorySize",
            at = @At("RETURN")
    )
    private int whs_addHorseShoeSlot(int original){
        return original + (this.hasChest() ? 1 : 0);
    }
}
