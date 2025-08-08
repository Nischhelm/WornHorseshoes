package wornhorseshoes.mixin.vanilla.elytra;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityHorse.class)
public abstract class ElytraOnHorse {
    @ModifyReturnValue(
            method = "isArmor",
            at = @At("RETURN")
    )
    private boolean whs_allowElytra(boolean original, ItemStack stack){
        return original || stack.getItem() instanceof ItemElytra;
    }
}
