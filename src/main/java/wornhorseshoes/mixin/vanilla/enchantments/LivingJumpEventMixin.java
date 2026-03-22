package wornhorseshoes.mixin.vanilla.enchantments;

import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import wornhorseshoes.util.ISkipsPotionCoreHandler;

@Mixin(LivingEvent.LivingJumpEvent.class)
public class LivingJumpEventMixin implements ISkipsPotionCoreHandler {
    @Unique private boolean whs$skipsPotionCore = false;

    @Override
    public boolean whs$getSkipsPotionCore() {
        return this.whs$skipsPotionCore;
    }

    @Override
    public void whs$setSkipsPotionCore() {
        this.whs$skipsPotionCore = true;
    }
}
