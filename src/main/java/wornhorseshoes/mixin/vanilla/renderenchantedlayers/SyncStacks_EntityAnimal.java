package wornhorseshoes.mixin.vanilla.renderenchantedlayers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.config.folders.HorseshoesConfig;
import wornhorseshoes.util.IHorseStackGetter;

@Mixin(EntityAnimal.class)
public abstract class SyncStacks_EntityAnimal extends Entity {
    public SyncStacks_EntityAnimal(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void whs_registerSaddleDataParameter(World worldIn, CallbackInfo ci){
        if((EntityAnimal) (Object) this instanceof AbstractHorse){
        this.dataManager.register(IHorseStackGetter.SADDLE_STACK, ItemStack.EMPTY);
        if(HorseshoesConfig.canShoeHorse((AbstractHorse) (Object) this))
            this.dataManager.register(IHorseStackGetter.HORSESHOE_STACK, ItemStack.EMPTY);
        }
    }
}
