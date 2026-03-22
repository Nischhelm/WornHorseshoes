package wornhorseshoes.mixin.vanilla.accessors;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(EntityHorse.class)
public interface HorseArmorAccessor {
    @Accessor(value = "HORSE_ARMOR_STACK", remap = false)
    static DataParameter<ItemStack> getArmorStack() {
        throw new AssertionError("Unable to access EntityHorse.HORSE_ARMOR_STACK");
    }
    @Accessor("ARMOR_MODIFIER_UUID")
    static UUID getAmorModifierUUID() {
        throw new AssertionError("Unable to access EntityHorse.ARMOR_MODIFIER_UUID");
    }
}
