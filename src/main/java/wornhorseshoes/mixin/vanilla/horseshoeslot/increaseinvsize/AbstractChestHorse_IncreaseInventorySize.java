package wornhorseshoes.mixin.vanilla.horseshoeslot.increaseinvsize;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import wornhorseshoes.config.folders.HorseshoesConfig;

@Mixin(AbstractChestHorse.class)
public abstract class AbstractChestHorse_IncreaseInventorySize extends AbstractHorse {
    public AbstractChestHorse_IncreaseInventorySize(World worldIn) {
        super(worldIn);
    }

    @Shadow public abstract boolean hasChest();

    @ModifyReturnValue(
            method = "getInventorySize",
            at = @At("RETURN")
    )
    //Seems stupid but if it doesn't have a chest attached, AbstractHorse is handling it
    private int whs_addHorseShoeSlot(int original){
        if(!HorseshoesConfig.canShoeHorse(this)) return original;
        return original + (this.hasChest() ? 1 : 0);
    }
}
