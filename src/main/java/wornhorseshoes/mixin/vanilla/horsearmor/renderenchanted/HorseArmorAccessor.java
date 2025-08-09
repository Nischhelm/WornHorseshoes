package wornhorseshoes.mixin.vanilla.horsearmor.renderenchanted;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityHorse.class)
public interface HorseArmorAccessor {
    @Accessor(value = "HORSE_ARMOR_STACK", remap = false)
    DataParameter<ItemStack> getArmorStack();
}
