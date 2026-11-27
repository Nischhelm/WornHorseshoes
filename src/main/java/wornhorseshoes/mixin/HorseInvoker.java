package wornhorseshoes.mixin;

import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Markings;
import net.minecraft.world.entity.animal.horse.Variant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Lets the temple encounter give Juan his fixed coat (Horse only exposes the colour setter publicly). */
@Mixin(Horse.class)
public interface HorseInvoker {
    @Invoker("setVariantAndMarkings")
    void whs$setVariantAndMarkings(Variant variant, Markings markings);
}
