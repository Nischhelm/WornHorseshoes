package wornhorseshoes.mixin.vanilla.armor;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.config.folders.HorseArmorConfig;

@Mixin(EntityHorse.class)
public abstract class HorseArmorAutoStats {
    @Inject(
            method = "setHorseArmorStack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/HorseArmorType;getProtection()I"),
            cancellable = true
    )
    private void whs_cancelOriginalStatHandling(ItemStack stack, CallbackInfo ci){
        ResourceLocation loc = stack.getItem().getRegistryName();
        if(loc == null) return;
        if(!HorseArmorConfig.itemStats.containsKey(loc.toString())) return;
        ci.cancel();
    }
}
