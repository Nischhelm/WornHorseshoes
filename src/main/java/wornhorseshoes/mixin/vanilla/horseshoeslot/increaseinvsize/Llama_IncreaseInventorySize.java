package wornhorseshoes.mixin.vanilla.horseshoeslot.increaseinvsize;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityLlama.class)
public abstract class Llama_IncreaseInventorySize extends AbstractChestHorse {
    public Llama_IncreaseInventorySize(World worldIn) {
        super(worldIn);
    }

    @ModifyReturnValue(
            method = "getInventorySize",
            at = @At("RETURN")
    )
    private int whs_addHorseShoeSlot(int original){
        return original + (this.hasChest() ? 1 : 0);
    }
}
