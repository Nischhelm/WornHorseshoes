package wornhorseshoes.mixin.vanilla;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractHorse.class)
public abstract class RearingFix extends EntityLivingBase  {
    public RearingFix(World worldIn) {
        super(worldIn);
    }

    @ModifyExpressionValue(
            method = "onUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AbstractHorse;canPassengerSteer()Z")
    )
    private boolean whs_alsoUnRearWithNoPassenger(boolean original){
        return original || this.getPassengers().isEmpty();
    }
}
