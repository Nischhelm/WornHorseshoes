package wornhorseshoes.mixin.vanilla.accessors;

import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.inventory.ContainerHorseChest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractHorse.class)
public interface HorseInventoryAccessor {
    @Accessor("horseChest")
    ContainerHorseChest getHorseChest();
}
