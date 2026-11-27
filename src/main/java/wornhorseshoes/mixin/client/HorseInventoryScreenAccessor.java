package wornhorseshoes.mixin.client;

import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** The horse screen keeps its horse private; the slot background and stats need it. */
@Mixin(HorseInventoryScreen.class)
public interface HorseInventoryScreenAccessor {
    @Accessor("horse")
    AbstractHorse whs$getHorse();
}
