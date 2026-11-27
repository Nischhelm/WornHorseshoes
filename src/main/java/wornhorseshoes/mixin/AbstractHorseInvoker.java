package wornhorseshoes.mixin;

import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Calls AbstractHorse's protected updateContainerEquipment (re-reads saddle and armor from the inventory). */
@Mixin(AbstractHorse.class)
public interface AbstractHorseInvoker {
    @Invoker("updateContainerEquipment")
    void whs$updateContainerEquipment();
}
